package net.pinhask.chat

import akka.Done
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import net.pinhask.chat.ChatRoom.{GetSession, SessionEvent}

import scala.io.StdIn

object Chat {
  sealed trait ChatCommand
  final case class CreateUserSession(roomName: String, replayTo: ActorRef[SessionEvent]) extends ChatCommand

  def apply(): Behavior[ChatCommand] =
    Behaviors.setup { ctx =>
      val chatRoom = ctx.spawn(ChatRoom(), "chatRoom")
      Behaviors.receiveMessage {
        case CreateUserSession(clientName, replyTo) =>
          println(s"session request get for client ${clientName}")
          chatRoom ! GetSession(clientName, replyTo)
          Behaviors.same
      }

  }

  def main(args: Array[String]) {
    val chatActor: Behavior[ChatCommand] = Chat()
    implicit val chatSystem: ActorSystem[ChatCommand] = ActorSystem(chatActor, "chat-app")
    val wsHandler: ActorSystem[Done] = WSServer(chatSystem)

    StdIn.readLine()
    chatSystem.terminate()
    wsHandler.terminate()
  }
}
