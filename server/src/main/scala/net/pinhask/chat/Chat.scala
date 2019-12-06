package net.pinhask.chat

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import scala.io.StdIn

object Chat {
  sealed trait ChatCommand
  final case class CreateUserSession(name: String) extends ChatCommand

  def apply(): Behavior[ChatCommand] =
    Behaviors.setup { ctx =>
      val chatRoom = ctx.spawn(ChatRoom(), "chatRoom")
      Behaviors.receiveMessage {
        case CreateUserSession(name) =>
          Behaviors.same
      }

  }

  def main(args: Array[String]) {
    val chatActor: Behavior[ChatCommand] = Chat()
    implicit val chatSystem: ActorSystem[ChatCommand] = ActorSystem(chatActor, "chat-app")
    val wsHandler = WSServer()

//    import system.dispatcher // for the future transformations
//    bindingFuture
//      .flatMap(_.unbind()) // trigger unbinding from the port
//      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
