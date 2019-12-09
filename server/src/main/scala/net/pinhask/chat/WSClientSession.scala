package net.pinhask.chat

import akka.actor.typed.{ActorRef, Behavior, PostStop}
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.model.ws.TextMessage

object WSClientSession {

  import ChatRoom._

  def apply(down: ActorRef[TextMessage], clientPostMsg: ActorRef[WSClient.Commands]): Behavior[SessionEvent] = {
    Behaviors.setup { context =>
      Behaviors.receiveMessage[SessionEvent] {
        case SessionGranted(handle: ActorRef[SessionCommand]) =>
          context.log.info(s"Session granted for ")
          clientPostMsg ! WSClient.ChatSessionHandler(handle)
          Behaviors.same
        case SessionDenied(reason) =>
          context.log.error(s"Session denied for $down because $reason")
          Behaviors.stopped
        case MessagePosted(screenName, msg) =>
          context.log.info("message has been posted by '{}': {}", screenName, msg)
          down ! TextMessage(s"From: $screenName, context: $msg")
          Behaviors.same
      }.receiveSignal {
        case (ctx, PostStop) =>
          ctx.log.info(s"stopping ${ctx.self}")
          Behaviors.same
      }
    }
  }
}