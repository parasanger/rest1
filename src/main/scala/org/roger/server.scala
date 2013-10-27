package org.roger

import com.typesafe.config.ConfigFactory
import spray.routing._
import akka.actor._
import akka.io.IO
import spray.can.Http
import spray.json._
import spray.httpx._
import spray.http.MediaTypes._

case class MyRequest(originator: String, message: String, receivers: List[String])
case class MyResponse(tiers: Array[(String, Array[String])])

object MyJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val RequestFormat = jsonFormat3(MyRequest)
  implicit val ResponseFormat = jsonFormat1(MyResponse)
}

object Main extends App {

  implicit val system = ActorSystem("sendhub")

  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")
  val service= system.actorOf(Props(new SprayActor()), "spray-service")

  IO(Http) ! Http.Bind(service, host, port)
}

class SprayActor extends Actor with SprayService with ActorLogging {
  def actorRefFactory = context
  def receive = runRoute(sprayRoute)
}

trait SprayService extends HttpService {
  import MyJsonProtocol._

  val sprayRoute = {
    path("send") {
      post {
        entity(as[MyRequest]) {request =>
          val response: Array[ResultTier] = Solver.findTiers(request)
          respondWithMediaType(`application/json`) {
            // problems serialzing inner custome type.
            val ret = for (t <- response) yield { (t.tier, t.messages)}
            complete(ret)
          }
        }
      } ~
      get {
        complete("\n\nPING\n\n")
      }
    }
  }
}

// the problem at hand.  A variant on making change with the least
// number coins

sealed abstract class Tier {
  def tier: String
  def capacity: Int
}

case class RateTier(tier: String, capacity: Int) extends Tier
case class UsedTier(tier: String, capacity: Int, chunks: Int = 0) extends Tier
case class ResultTier(tier: String, messages: List[String], capacity: Int = 0) extends Tier

object Solver {

  val ip4 = RateTier("10.0.4.0/24", 25)
  val ip3 = RateTier("10.0.3.0/24", 10)
  val ip2 = RateTier("10.0.2.0/24", 5)
  val ip1 = RateTier("10.0.1.0/24", 1)

  val rates =  Array(ip4, ip3, ip2, ip1)

  // after the setup some greedy computation
  def findTiers(request: MyRequest) = {
    def findTierChunks(size: Int, t: RateTier) = size / t.capacity     // Div

    var numMsgs = request.receivers.size
    val usedTiers = for (rateTier <- rates) yield {
      val chunks = findTierChunks(numMsgs, rateTier)
      numMsgs = numMsgs - chunks * rateTier.capacity
      new UsedTier(tier = rateTier.tier, chunks = chunks, capacity = rateTier.capacity)
    }

    printf(s"\n ${usedTiers.mkString(" , ")}\n ")

    // now we have how we want in each tier, divvy up and return
    var sliceIdx : Int = 0
    val result = for (usedTier <-  usedTiers) yield {
      val msg = request.receivers.slice(sliceIdx, sliceIdx + usedTier.capacity * usedTier.chunks)
      val resultTier = new ResultTier(messages = msg, tier = usedTier.tier)
      sliceIdx = sliceIdx + usedTier.capacity * usedTier.chunks

      resultTier
    }
    result
  }
}





















