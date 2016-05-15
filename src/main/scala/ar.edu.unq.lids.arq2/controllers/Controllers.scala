package ar.edu.unq.lids.arq2.controllers

import javax.inject.Singleton
import ar.edu.unq.lids.arq2.{ConfigurationResponse, BuildInfo}
import ar.edu.unq.lids.arq2.model.{Price, Product, Shop}
import ar.edu.unq.lids.arq2.service._
import com.twitter.finagle.http.{Request, Status}
import com.twitter.finatra.http.Controller

@Singleton
class ServerController extends Controller{
  get("/health") {request: Request =>
    response.ok.status = Status.Ok
  }

  get("/info") {request: Request =>
    response.ok.json(BuildInfo.toJson)
  }

  get("/config") {request: Request =>
    response.ok.json(ConfigurationResponse())
  }

  get("/") {request: Request =>
    response.ok.body("It works!!")
  }
}

@Singleton
class ProductController extends ResourceController[Product, ProductDTO]{
  get("/products")(all)
  post("/products")(save("products"))
  get("/products/:id") (byId)
}

@Singleton
class PriceController extends ResourceController[Price, PriceDTO]{
  override val service = new PriceService

  get("/found-prices")(all)

  post("/found-prices")(save("found-prices"))
  get("/found-prices/:id")(byId)

  get("/prices/average/:id"){ request: PriceRequest =>
    response.ok.json(service.average(request).toJson)
  }
}

@Singleton
class ShopController extends ResourceController[Shop, ShopDTO]{
  override val service = new ShopService

  get("/shops"){ request: ShopRequest =>
    response.ok.json(cache(request){service.search(request)})
  }

  get("/shops/:id")(byId)

  post("/shops")(save("shops"))

}


