package net.pinhask.chat


import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.http.scaladsl.server.Directives

import scala.io.StdIn

object Chat {
  def main(args: Array[String]) {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    import Directives._

    // The Greeter WebSocket Service expects a "name" per message and
    // returns a greeting message for that name
    val greeterWebSocketService =
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
          handleWebSocketMessages(greeterWebSocketService)
        }
      }
    //#websocket-routing

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()

    import system.dispatcher // for the future transformations
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
