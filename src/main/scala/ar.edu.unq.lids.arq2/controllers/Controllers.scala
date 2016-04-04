package ar.edu.unq.lids.arq2.controllers

import ar.edu.unq.lids.arq2.server.AppController
import ar.edu.unq.lids.arq2.service.{ProductDTO, ProductService}
import com.twitter.finagle.http.Request

class ProductController extends AppController{

  val service = new ProductService

  get("/products") { request: Request =>
    response.ok.json(service.all)
  }

  post("/products") { product: ProductDTO =>
    response.ok.json(service.save(product))
  }

  get("/product/:name") { request: Request =>
    val name = request.params.getOrElse("name", "")
    response.ok("")
//    response.ok.json(service.get(_.name, name))
  }
}

