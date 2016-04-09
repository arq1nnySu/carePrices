package ar.edu.unq.lids.arq2.controllers

import javax.inject.Singleton

import ar.edu.unq.lids.arq2.model.Product
import ar.edu.unq.lids.arq2.service._

@Singleton
class ProductController extends ResourceController[Product, ProductDTO]{
  get("/product")(all)
  post("/product")(save)
  get("/product/:id") (byId)
}



