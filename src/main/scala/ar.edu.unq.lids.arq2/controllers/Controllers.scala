package ar.edu.unq.lids.arq2.controllers

import ar.edu.unq.lids.arq2.model.{ProductDTO, Resource}
import ar.edu.unq.lids.arq2.server.AppController
import ar.edu.unq.lids.arq2.service.ProductService
import com.twitter.finagle.http.Request

import scala.reflect._

class ProductController extends AppController{

  val service = new ProductService

  get("/products") { request: Request =>
    response.ok.json(service.all)
  }

  post("/products") { product: ProductDTO =>
    service.save(product)
    response.ok.json(service.all)
  }

  get("/product/:name") { request: Request =>
    val name = request.params.getOrElse("name", "")
    response.ok("")
//    response.ok.json(service.get(_.name, name))
  }
}

