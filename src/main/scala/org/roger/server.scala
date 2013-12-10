package org.roger

import com.typesafe.config.ConfigFactory
import spray.routing._
import akka.actor._
import akka.io.IO
import spray.can.Http
import spray.json._
import spray.httpx._
import spray.http.MediaTypes._
import spray.http.{StatusCodes, StatusCode}
import spray.util.LoggingContext

case class MyRequest(originator: String, message: String, receivers: List[String])
case class MyResponse(tiers: Array[(String, Int, Array[String])])

object MyJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val RequestFormat = jsonFormat3(MyRequest)
  implicit val ResponseFormat = jsonFormat1(MyResponse)
}

object Main extends App {

  implicit val system = ActorSystem("hub")

  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")
  val service= system.actorOf(Props(new SprayActor()), "spray-service")

  IO(Http) ! Http.Bind(service, host, port)
}

/**
 * http requets handling
 */
class SprayActor extends Actor with SprayService with ActorLogging {
  def actorRefFactory = context
  def receive = runRoute(sprayRoute)
}

trait SprayService extends HttpService {
  import MyJsonProtocol._

  implicit def myExceptionHandler(implicit log: LoggingContext) =
    ExceptionHandler {
      case e: NoSuchElementException => ctx =>
        log.warning("Request format bad", ctx.request)
        ctx.complete(StatusCodes.UnprocessableEntity, "Unmarshalling problem")
      case f: RuntimeException => ctx =>
        log.error("Request Handling problem", f)
        ctx.complete(StatusCodes.InternalServerError, "Server Error")
      case g: Throwable => ctx =>
        log.error("Unexpected problem",g)
        ctx.complete(StatusCodes.InternalServerError, "Server Error")
    }

  val sprayRoute = {
    path("send") {
      post {
        entity(as[MyRequest]) {request =>
          val response = Solver.findTiers(request)
          respondWithMediaType(`application/json`) {
            // problems serialzing inner complex type.
            val ret = for (t <- response) yield { (t.tier, t.capacity, t.messages)}
            complete(ret)
          }
        }
      } ~
      get {
        complete("\n\nPING\n\n")
      } ~
      put  {
        respondWithMediaType(`application/json`) {
          respondWithStatus(responseStatus = StatusCodes.NotFound) {
            complete("""{ "status" : "PUT Not Supported" }""")
          }
        }
      } ~
        delete  {
          respondWithMediaType(`application/json`) {
            respondWithStatus(responseStatus = StatusCodes.NotFound) {
            complete("""{ "status" : "DELETE Not Supported" }""")
            }
          }

      }
    }
  }
}

sealed abstract class Tier {
  def tier: String
  def capacity: Int
}

case class RateTier(tier: String, capacity: Int) extends Tier
case class ResultTier(tier: String, messages: List[String], capacity: Int = 0) extends Tier

object Solver {
  val ip4 = RateTier("10.0.4.0/24", 25)
  val ip3 = RateTier("10.0.3.0/24", 10)
  val ip2 = RateTier("10.0.2.0/24", 5)
  val ip1 = RateTier("10.0.1.0/24", 1)

  val rates = Array(ip4, ip3, ip2, ip1)

  def findTiers(request: MyRequest) = {
    rates.map {
      case tier =>
        val l = request.receivers.grouped(tier.capacity).toList.flatten
        val r = ResultTier(tier = tier.tier, messages = l, capacity = l.size)
        request.receivers.drop(l.size)
        r
    }
  }
}
//
//  object Solver {
//
//    val ip4 = RateTier("10.0.4.0/24", 25)
//    val ip3 = RateTier("10.0.3.0/24", 10)
//    val ip2 = RateTier("10.0.2.0/24", 5)
//    val ip1 = RateTier("10.0.1.0/24", 1)
//
//    val rates =  Array(ip4, ip3, ip2, ip1)
//
//    // after the setup some greedy computation
//    def findTiers(request: MyRequest) = {
//      def findTierChunks(size: Int, t: RateTier) = size / t.capacity     // Div
//
//      var remainingMsgs = request.receivers.size
//      val usedTiers = for (rateTier <- rates) yield {
//        val chunks = findTierChunks(remainingMsgs, rateTier)
//
//        remainingMsgs = remainingMsgs - chunks * rateTier.capacity
//        UsedTier(tier = rateTier.tier, chunks = chunks, capacity = rateTier.capacity)
//      }
//
//      // now we have how we want in each tier, divvy up and return
//      var sliceIdx : Int = 0
//      val result = for (usedTier <-  usedTiers) yield {
//        val msg = request.receivers.slice(sliceIdx, sliceIdx + usedTier.capacity * usedTier.chunks)
//        sliceIdx = sliceIdx + usedTier.capacity * usedTier.chunks
//
//        ResultTier(messages = msg, tier = usedTier.tier, capacity=msg.length)
//      }
//      result
//    }
//  }



















