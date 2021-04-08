package io.github.ghostbuster91.sttp.client3.example

import _root_.sttp.client3._
import _root_.sttp.model._
import _root_.io.circe.Decoder
import _root_.io.circe.Encoder
import _root_.io.circe.generic.AutoDerivation
import _root_.sttp.client3.circe.SttpCirceApi

trait CirceCodecs extends AutoDerivation with SttpCirceApi {
  implicit val status2Decoder: Decoder[Status2] = Decoder.decodeString.emap({
    case "new" =>
      Right(Status2.New)
    case "old" =>
      Right(Status2.Old)
    case other =>
      Left("Unexpected value for enum:" + other)
  })
  implicit val status2Encoder: Encoder[Status2] =
    Encoder.encodeString.contramap({
      case Status2.New => "new"
      case Status2.Old => "old"
    })
  implicit val statusDecoder: Decoder[Status] = Decoder.decodeString.emap({
    case "happy" =>
      Right(Status.Happy)
    case "neutral" =>
      Right(Status.Neutral)
    case other =>
      Left("Unexpected value for enum:" + other)
  })
  implicit val statusEncoder: Encoder[Status] =
    Encoder.encodeString.contramap({
      case Status.Happy   => "happy"
      case Status.Neutral => "neutral"
    })
}
sealed trait Status2
object Status2 {
  case object New extends Status2()
  case object Old extends Status2()
}
sealed trait Status
object Status {
  case object Happy extends Status()
  case object Neutral extends Status()
}
case class Person1(status: Status)
case class Person2(status: Status2)
case class Couple(p1: Person1, p2: Person2)

class DefaultApi(baseUrl: String) extends CirceCodecs {
  def getPerson(): Request[Couple, Any] =
    basicRequest.get(uri"$baseUrl/person").response(asJson[Couple].getRight)
}