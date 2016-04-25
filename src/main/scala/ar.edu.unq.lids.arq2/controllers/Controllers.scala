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
  post("/products")(save)
  get("/products/:id") (byId)
}

@Singleton
class PriceController extends ResourceController[Price, PriceDTO]{
  override val service = new PriceService
  val cacheService  =  new CacheService

  get("/found-prices"){ request: Request =>
    response.ok.json((cacheService.memorize("prices"){service.all}))
  }

  post("/found-prices"){ price: PriceDTO =>
    val id = service.savePrice(price).id
    response.created.location(s"/found-prices/$id")
  }
}

@Singleton
class ShopController extends ResourceController[Shop, ShopDTO]{
  override val service = new ShopService

  get("/shops"){ request: ShopDTO =>
    response.ok.json(service.search(request))
  }

  post("/shops")(save)
  //post("/shops"){request: ShopTest =>
  //  response.ok.json(request)}
}
