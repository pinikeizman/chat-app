package net.pinhask.chat

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.http.scaladsl.model.ws.TextMessage

object WSClientSession {

  import ChatRoom._

  def apply(down: ActorRef[TextMessage], clientPostMsg: ActorRef[WSClient]): Behavior[SessionEvent]= apply(down, None)
  def apply(down: ActorRef[TextMessage], roomSession: Option[ActorRef[SessionCommand]]): Behavior[SessionEvent]= {
    Behaviors.setup { context =>
      Behaviors.receiveMessage {
        case SessionGranted(handle: ActorRef[SessionCommand]) =>
          context.log.info(s"Session granted for ")
          WSClientSession(down, Some(handle))
        case SessionDenied(reason) =>
          context.log.error(s"Session denied for $down because $reason")
          Behaviors.stopped
        case MessagePosted(screenName, msg) =>
          context.log.info("message has been posted by '{}': {}", screenName, msg)
          down ! TextMessage(s"From: $screenName, context: $msg")
          Behaviors.same
      }
    }
  }
}