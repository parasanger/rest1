package org.roger


import akka.testkit.{ TestKit, ImplicitSender }
import akka.actor.{ Props, Actor, ActorSystem }
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers

import akka.util.Timeout
import scala.concurrent.Await
import scala.util.{ Try, Success, Failure }

/**
 * Created with IntelliJ IDEA.
 * User: roger
 * Date: 10/24/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
class ServerTest {


  class EchoActorTest extends TestKit(ActorSystem("testsystem"))
  with WordSpec
  with MustMatchers
  with ImplicitSender {

    "An EchoActor" must {
      "Reply with exactly one block" in {
        // exactly 25,10,5,1
      }
      "Reply with the zero case" in {
      }
      "Reply to 26, 11, 6, 2" in {
      }
      "repluy with gaps 24, 9" in {
      }
      "5000" in {
      }
      "5001" in {
      }
    }
  }

  class EchoActor extends Actor {
    def receive = {
      case msg =>
        sender ! msg
    }
  }
}
