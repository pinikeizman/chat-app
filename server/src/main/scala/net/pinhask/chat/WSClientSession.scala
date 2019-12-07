package net.pinhask.chat

import akka.actor.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object WSClientSession {

  import ChatRoom._

  def apply() = ???

  def wsClientSession(name: String, wsStreamPublisher: ActorRef): Behavior[SessionEvent] = {
    Behaviors.setup { context =>
      Behaviors.receiveMessage {
        case SessionGranted(handle) =>
          handle ! PostMessage("Hello World!")
          Behaviors.same
        case mp: MessagePosted =>
          context.log.info("message has been posted by '{}': {}", mp.screenName, mp.message)
          wsStreamPublisher ! mp
          Behaviors.stopped
      }
    }
  }
}