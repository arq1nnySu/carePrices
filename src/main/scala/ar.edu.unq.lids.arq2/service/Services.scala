package ar.edu.unq.lids.arq2.service

import java.io.Serializable

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.model.{Price, Product, Shop}
import ar.edu.unq.lids.arq2.persistence.Repository

class ProductService extends ResourceService[Product, ProductDTO] {}

class PriceService extends ResourceService[Price, PriceDTO] {

  var shopRepository = new Repository[Shop]

  override def save(pricedto: PriceDTO): PriceDTO = transactional {
    val shop = shopRepository.get(_.id, pricedto.shop)
    val price = new Price(shop, pricedto.product, pricedto.price, pricedto.datetime)
    repository.save(price)
  }
}

class ShopService extends ResourceService[Shop, ShopDTO] {

  def search(request: ShopRequest) = {
    filter(request.limit, request.offset, List[((Shop) => String, Option[String])](
      (_.location, request.location),
      (_.name, request.name),
      (_.address, request.address),
      (_.latitude, request.latitude),
      (_.longitude, request.longitude)
    ))
  }

  override def save(shopdto: ShopDTO): ShopDTO = transactional {
    val shop = select[Shop].where(shop => (shop.latitude :== shopdto.latitude) :|| (shop.longitude :== shopdto.longitude) :|| (shop.name :== shopdto.name) :|| (shop.address :== shopdto.address) :|| (shop.location :== shopdto.location))
    if (shop.isEmpty) repository.save(shopdto) else shop.head
  }
}