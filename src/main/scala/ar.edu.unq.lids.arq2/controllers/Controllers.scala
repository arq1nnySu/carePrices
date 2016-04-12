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
  get("/product")(all)
  post("/product")(save)
  get("/product/:id") (byId)
}

@Singleton
class PriceController extends ResourceController[Price, PriceDTO]{
  override val service = new PriceService

  get("/found-prices")(all)

  post("/found-prices"){ price: PriceDTO =>
    response.ok.json(service.savePrice(price))
  }
}

@Singleton
class ShopController extends ResourceController[Shop, ShopDTO]{
  override val service = new ShopService

  get("/shops"){ request: ShopSearchRequest =>
    response.ok.json(service.search(request))
  }

  post("/shops")(save)
}
