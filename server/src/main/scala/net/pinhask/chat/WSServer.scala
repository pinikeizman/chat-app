package net.pinhask.chat

import akka.actor.typed.ActorSystem
import akka.{Done, NotUsed, actor}
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

object WSServer {
  def apply() = {
    val system = ActorSystem[Done](Behaviors.setup[Done] { ctx =>
      // http doesn't know about akka typed so create untyped system/materializer
      implicit val untypedSystem: actor.ActorSystem = ctx.system.toClassic
//      implicit val materializer: ActorMaterializer = ActorMaterializer()(ctx.system.toClassic)
      import Directives._
      // The Greeter WebSocket Service expects a "name" per message and
      // returns a greeting message for that name
      val greeterWebSocketService: Flow[Message, TextMessage, NotUsed] =
      Flow[Message]
        .collect {
          case tm: TextMessage => TextMessage(Source.single("Hello ") ++ tm.textStream)
          case message: BinaryMessage =>
            message.dataStream.runWith(Sink.ignore)
            TextMessage(Source.single("Expects Text message."))
        }

      //#websocket-routing
      val route =
        path("greeter") {
          get {
            println("connected")
            handleWebSocketMessages(greeterWebSocketService)
          }
        }
      //#websocket-routing

      val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

      println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

      Behaviors.same
    }, "as")
  }
}