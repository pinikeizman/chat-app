package net.pinhask.chat

import java.net.URLEncoder

import akka.actor.typed.{ActorRef, Behavior, PostStop, Terminated}
import akka.actor.typed.scaladsl.Behaviors
import java.nio.charset.StandardCharsets

object ChatRoom {

  sealed trait RoomCommand
  final case class GetSession(screenName: String, replyTo: ActorRef[SessionEvent]) extends RoomCommand
  private final case class PublishSessionMessage(screenName: String, message: String) extends RoomCommand

  sealed trait SessionEvent
  final case class SessionGranted(handle: ActorRef[PostMessage]) extends SessionEvent
  final case class SessionDenied(reason: String) extends SessionEvent
  final case class MessagePosted(screenName: String, message: String) extends SessionEvent

  trait SessionCommand
  final case class PostMessage(message: String) extends SessionCommand
  private final case class NotifyClient(message: MessagePosted) extends SessionCommand

  def apply(): Behavior[RoomCommand] =
    chatRoom(List.empty)


  private def chatRoom(sessions: List[ActorRef[SessionCommand]]): Behavior[RoomCommand] =
    Behaviors.receive[RoomCommand] { (context, message) =>
      message match {
        case GetSession(screenName, client) =>
          // create a child actor for further interaction with the client
          val ses = context.spawn(
            session(context.self, screenName, client),
            name = URLEncoder.encode(screenName, StandardCharsets.UTF_8.name))
          client ! SessionGranted(ses)
          context.watch(ses)
          chatRoom(ses :: sessions)
        case PublishSessionMessage(screenName, message) =>
          val notification = NotifyClient(MessagePosted(screenName, message))
          sessions.foreach(_ ! notification)
          Behaviors.same
      }
    }.receiveSignal{
      case (ctx, Terminated(ref)) =>
        val nextSessions = sessions.filter(_.path.name != ref.path.name)
        ctx.log.info(s"next length:${nextSessions.length} prev length: ${sessions.length}")
        chatRoom(nextSessions)
    }

  private def session(
                       room: ActorRef[PublishSessionMessage],
                       screenName: String,
                       client: ActorRef[SessionEvent]
                     ): Behavior[SessionCommand] =
    Behaviors.setup {
      ctx =>
        ctx.watch(client)

        Behaviors.receiveMessage[SessionCommand] {
          case PostMessage(message) =>
            ctx.log.info(s"posting msg to chat room.")
            // from client, publish to others via the room
            room ! PublishSessionMessage(screenName, message)
            Behaviors.same
          case NotifyClient(message) =>
            // published from the room
            client ! message
            Behaviors.same
        }.receiveSignal {
          case (context, Terminated(ref)) =>
            Behaviors.stopped
          case (context, PostStop) =>
            context.log.info(s"Terminating session ${context.self}")
            Behaviors.same
        }
    }
}