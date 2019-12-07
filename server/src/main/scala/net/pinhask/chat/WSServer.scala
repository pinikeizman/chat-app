package net.pinhask.chat

import akka.actor.typed.ActorSystem
import akka.{Done, NotUsed, actor}
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.Directives
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import net.pinhask.chat.Chat.{ChatCommand, CreateUserSession}
import net.pinhask.chat.ChatRoom.{MessagePosted, SessionEvent, SessionGranted}
import scala.concurrent.duration._

object WSServer {
  def apply(chatSystem: ActorSystem[ChatCommand]) = {
    val system = ActorSystem[Done](Behaviors.setup[Done] { ctx =>
      // http doesn't know about akka typed so create untyped system/materializer
      implicit val untypedSystem: actor.ActorSystem = ctx.system.toClassic
      import Directives._
      // The Greeter WebSocket Service expects a "name" per message and
      // returns a greeting message for that name


      //#websocket-routing
      val route =
        path("greeter" / Segment) { clientName =>
          get {
            val (down, publisher) = Source
              .actorRef[SessionEvent](1000, OverflowStrategy.fail)
              .toMat(Sink.asPublisher(fanout = false))(Keep.both)
              .run()
            val source =
              Source
                .fromPublisher(publisher)
                .map {
                  case MessagePosted(screenName, message) => TextMessage(s"${screenName}: ${message}")
                  case SessionGranted(handler) => TextMessage(s"${handler}")
                }
            println(s"asking session for client ${clientName}")
            chatSystem ! CreateUserSession(clientName, down)
            val greeterWebSocketService: Flow[Message, TextMessage, NotUsed] =
              Flow[Message]
                .mapConcat{
                  case tm: TextMessage =>

                    Nil//TextMessage(Source.single("Hello ") ++ tm.textStream)
                }
                .merge(source)
            handleWebSocketMessages(greeterWebSocketService)
          }
        }
      //#websocket-routing

      val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

      println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
      untypedSystem
        .whenTerminated
        .zip(bindingFuture)
        .map(_._2.terminate(5.seconds))(untypedSystem.dispatcher)
        .map(println)(untypedSystem.dispatcher)

      Behaviors.same
    }, "as")
    system
  }
}