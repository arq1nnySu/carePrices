package ar.edu.unq.lids.arq2.controllers

import javax.inject.Singleton

import ar.edu.unq.lids.arq2.model.{Price, Product, Shop}
import ar.edu.unq.lids.arq2.service._
import ar.edu.unq.lids.arq2.{BuildInfo, ConfigurationResponse}
import com.twitter.finagle.http.{Request, Status}
import com.twitter.finatra.http.Controller

@Singleton
class ServerController extends Controller {
  get("/health") { request: Request =>
    response.ok.status = Status.Ok
  }

  get("/info") { request: Request =>
    response.ok.json(BuildInfo.toJson)
  }

  get("/config") { request: Request =>
    response.ok.json(ConfigurationResponse())
  }

  get("/") {request: Request =>
    response.ok.plain("It works!!").toFuture
  }
}

@Singleton
class ProductController extends ResourceController[Product, ProductDTO] {
  get("/api/v1/products")(all)
  post("/api/v1/products")(save("api/v1/products"))
  get("/api/v1/products/:id")(byId)
}

@Singleton
class PriceController extends ResourceController[Price, PriceDTO] {
  override val service = new PriceService

  get("/api/v1/found-prices")(all)
  post("/api/v1/found-prices")(save("api/v1/found-prices"))
  get("/api/v1/found-prices/:id")(byId)

  get("/api/v1/prices/average/:id") { request: PriceRequest =>
    response.ok.json(service.average(request).toJson)
  }
}

@Singleton
class ShopController extends ResourceController[Shop, ShopDTO] {
  override val service = new ShopService

  get("/api/v1/shops") { request: ShopRequest =>
    response.ok.json(cache(request) {
      service.search(request)
    })
  }
  post("/api/v1/shops")(save("api/v1/shops"))
  get("/api/v1/shops/:id")(byId)

}