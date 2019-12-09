package net.pinhask.chat

import akka.actor.ActorRef
import akka.actor.typed.ActorSystem
import akka.{Done, NotUsed, actor}
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import net.pinhask.chat.Chat.{ChatCommand, CreateUserSession}
import org.reactivestreams.Publisher

import scala.concurrent.duration._
import scala.util.{Failure, Success}

object WSServer {
  def apply(chatSystem: ActorSystem[ChatCommand]) = {
    val system = ActorSystem[Done](Behaviors.setup[Done] { ctx =>
      // http doesn't know about akka typed so create untyped system/materializer
      implicit val untypedSystem: actor.ActorSystem = ctx.system.toClassic
      import untypedSystem.dispatcher
      import Directives._
      // The Greeter WebSocket Service expects a "name" per message and
      // returns a greeting message for that name


      //#websocket-routing
      val route =
        path("greeter" / Segment) { clientName =>
          get {
            val (down: ActorRef, publisher: Publisher[TextMessage]) = Source
              .actorRef[TextMessage](1000, OverflowStrategy.fail)
              .alsoTo(Sink.onComplete {
                case Success(a) =>
                  ctx.log.info(s"Completed successfully ${a}")
                case Failure(ex) =>
                  ctx.log.error(s"Completed with failure : $ex")
              })
              .toMat(Sink.asPublisher(fanout = false))(Keep.both)
              .run()
            val wsClient = ctx.spawn(WSClient(), s"WSClient_$clientName")
            val clientSession = ctx.spawn(WSClientSession(down, wsClient), s"WSClientSession_$clientName")
            val source: Source[TextMessage, NotUsed] =
              Source
                .fromPublisher(publisher)
            chatSystem ! CreateUserSession(clientName, clientSession)
            val greeterWebSocketService: Flow[Message, TextMessage, NotUsed] =
              Flow[Message]
                .mapConcat {
                  case tm: TextMessage =>
                    ctx.log.info(s"Got msg from socket $tm")
                    //                    wsClient ! WSClient.MessageFromWSClient(s"$tm")
                    tm.textStream.map(wsClient ! WSClient.MessageFromWSClient(_)).to(Sink.ignore).run()
                    Nil
                }
                .merge(source)
                .watchTermination() { (nu, done) =>
                  done.onComplete {
                    case Success(a) =>
                      ctx.log.info(s"Completed successfully ${clientName}")
                      ctx.stop(wsClient)
                      ctx.stop(clientSession)
                      untypedSystem.stop(down)
                    case Failure(ex) =>
                      ctx.log.error(s"Completed with failure  ${clientName} : $ex")
                  }
                  nu
                }
            handleWebSocketMessages(greeterWebSocketService)
          }
        }
      //#websocket-routing

      val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

      println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
      untypedSystem
        .whenTerminated
        .zip(bindingFuture)
        .map(_._2.terminate(5.seconds))
        .map(println)

      Behaviors.same
    }, "as")
    system
  }
}