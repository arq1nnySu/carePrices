package ar.edu.unq.lids.arq2.controllers

import javax.inject.Singleton

import ar.edu.unq.lids.arq2.model.{Product, Shop, Price}
import ar.edu.unq.lids.arq2.service._

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
  get("/shops")(all)
  post("/shops")(save)
}
