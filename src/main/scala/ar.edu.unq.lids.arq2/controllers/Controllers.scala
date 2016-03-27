package ar.edu.unq.lids.arq2.controllers

import ar.edu.unq.lids.arq2.model.FakeData
import ar.edu.unq.lids.arq2.server.AppController
import com.twitter.finagle.http.Request

class ProductController extends AppController{

  get("/products") { request: Request =>
    response.ok.json(FakeData.products)
  }

  get("/product/:name") { request: Request =>
    val name = request.params.getOrElse("name", "")
    response.ok.json(FakeData.products.find(_.name.toLowerCase.equals(name.toLowerCase)))
  }
}
