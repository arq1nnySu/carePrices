package ar.edu.unq.lids.arq2.service

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.controllers.GenericResponse
import ar.edu.unq.lids.arq2.exceptions.DuplicateResourceException
import ar.edu.unq.lids.arq2.model.{Price, Product, Shop}
import ar.edu.unq.lids.arq2.persistence.Repository
import scala.collection.mutable.Map

class ProductService extends ResourceService[Product, ProductDTO] {}

class PriceService extends ResourceService[Price, PriceDTO] {

  var shopRepository = new Repository[Shop]

  override def save(pricedto: PriceDTO): PriceDTO = transactional {
    val shop = shopRepository.get(_.id, pricedto.shop)
    val price = new Price(shop, pricedto.product, pricedto.price, pricedto.datetime)
    repository.save(price)
  }


  def average(request:PriceRequest): GenericResponse = transactional{
    val prices = filter(request.limit, request.offset, List[((Price) => String, Option[String])](
      (_.product, request.product_barcode),
      (_.shop.id, request.shop_id),
      (_.shop.name, request.shop_name),
      (_.shop.address, request.address),
      (_.shop.latitude, request.latitude),
      (_.shop.longitude, request.longitude),
        (_.shop.location, request.location)
    ), (_.price)).items
    val sum = prices.foldLeft(0D)(_ + _)
    GenericResponse(Map("average"->sum/prices.size))
  }
}

class ShopService extends ResourceService[Shop, ShopDTO] {

  def search(request: ShopRequest) = {
    paginatefilter(request.limit, request.offset, List[((Shop) => String, Option[String])](
      (_.location, request.location),
      (_.name, request.name),
      (_.address, request.address),
      (_.latitude, request.latitude),
      (_.longitude, request.longitude)
    ))
  }

  override def save(shopdto: ShopDTO): ShopDTO = transactional {
    val shop = select[Shop].where(shop => ((shop.address :== shopdto.address) :&& (shop.location :== shopdto.location)) :|| ((shop.latitude :== shopdto.latitude) :&& (shop.longitude :== shopdto.longitude)))
    if (shop.isEmpty) repository.save(shopdto) else throw DuplicateResourceException(shop.head)
  }
}