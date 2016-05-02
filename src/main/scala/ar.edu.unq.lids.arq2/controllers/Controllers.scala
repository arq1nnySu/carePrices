package ar.edu.unq.lids.arq2.controllers

import java.io.Serializable
import javax.inject.Singleton
import ar.edu.unq.lids.arq2.model.{Product, Shop, Price}
import ar.edu.unq.lids.arq2.service._
import com.twitter.finagle.http.{Status, Request}
import com.twitter.finatra.http.Controller
import net.fwbrasil.activate.statement.StatementSelectValue
import ar.edu.unq.lids.arq2.CartePriceActivateContext._

@Singleton
class ServerController extends Controller{
  get("/health") {request: Request =>
    response.ok.status = Status.Ok
    response

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

  get("/found-prices"){ request: ListRequest =>
    response.ok.json((cacheService.memorize("prices"){service.all(request.limit, request.offset)}))
  }

  post("/found-prices")(save("found-prices"))
  post("/found-prices/:id")(byId)
}

@Singleton
class ShopController extends ResourceController[Shop, ShopDTO]{
  override val service = new ShopService

  get("/shops"){ request: ShopRequest =>
    response.ok.json(service.search(request))
  }

  get("/shops/:id")(byId)

  post("/shops")(save("shops"))

}


