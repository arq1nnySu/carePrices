package ar.edu.unq.lids.arq2.controllers

import ar.edu.unq.lids.arq2.CartePriceActivateContext
import ar.edu.unq.lids.arq2.server.AppController
import ar.edu.unq.lids.arq2.service.{ProductDTO, ProductService}
import com.twitter.finagle.http.Request
import CartePriceActivateContext._

class ProductController extends AppController{

  val service = new ProductService
//  val price = new PriceService
  get("/products") { request: Request =>
    response.ok.json(service.all)
  }

  post("/product") { product: ProductDTO =>
    response.ok.json(service.save(product))
  }
 // agregar un nuevo precio
//  post("/product/:id/price") { request: Request =>
//    val id = request.params.getOrElse("id", "")
//    var product = service.
//    response.ok.json(service.save(price))
//  }
// modificar un precio de un producto
//  post(" ") {
//
//  }
  get("/product/:id") { request: Request =>
    val id = request.params.getOrElse("id", "")
    response.ok.json(service.get(_.id, id))
  }
}

