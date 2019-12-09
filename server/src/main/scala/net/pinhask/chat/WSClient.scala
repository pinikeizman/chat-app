package net.pinhask.chat

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, PostStop}
import net.pinhask.chat.ChatRoom.{PostMessage, SessionCommand}

object WSClient {

  sealed trait Commands

  case class MessageFromWSClient(msg: String) extends Commands

  case class ChatSessionHandler(handler: ActorRef[SessionCommand]) extends Commands

  def apply(): Behavior[Commands] = apply(None)

  def apply(handler: Option[ActorRef[SessionCommand]]): Behavior[Commands] = Behaviors.setup { context =>
    Behaviors.receiveMessage[Commands] {
      case ChatSessionHandler(handler) =>
        context.log.info(s"Session in Chat client")
        apply(Some(handler))
      case MessageFromWSClient(msg) =>
        context.log.info(s"Passing msg from socket")
        handler.map(_ ! PostMessage(msg))
        Behaviors.same
    }.receiveSignal {
      case (ctx, PostStop) =>
        ctx.log.info(s"closing ${ctx.self}")
        Behaviors.same
    }
  }
}