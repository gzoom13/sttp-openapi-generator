package io.github.ghostbuster91.sttp.client3.example

import _root_.sttp.client3._
import _root_.sttp.model._
import _root_.io.circe.generic.AutoDerivation
import _root_.sttp.client3.circe.SttpCirceApi

trait CirceCodecs extends AutoDerivation with SttpCirceApi

class StoreApi(baseUrl: String) extends CirceCodecs {
  def getInventory(): Request[Map[String, Int], Any] =
    basicRequest
      .get(uri"$baseUrl/store/inventory")
      .response(
        fromMetadata(
          asJson[Map[String, Int]].getRight,
          ConditionalResponseAs(
            _.code == StatusCode.unsafeApply(200),
            asJson[Map[String, Int]].getRight
          )
        )
      )
}
