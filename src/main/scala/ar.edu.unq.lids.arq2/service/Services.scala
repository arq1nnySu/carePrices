package ar.edu.unq.lids.arq2.service

import java.io.Serializable

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.model.{Price, Product, Shop}
import ar.edu.unq.lids.arq2.persistence.Repository

class ProductService extends ResourceService[Product, ProductDTO]{}

class PriceService extends ResourceService[Price, PriceDTO] {

  var productRepository = new Repository[Product]
  var shopRepository = new Repository[Shop]

  def savePrice(pricedto: PriceDTO): Price = transactional {
    val product = productRepository.get(_.barcode,pricedto.product)
    val shop = shopRepository.get(_.id, pricedto.shop)
    val price = new Price(shop, product, pricedto.price, pricedto.datetime)
    repository.save(price)
  }
}

class ShopService extends ResourceService[Shop, ShopDTO]{

  def search(request: ShopSearchRequest) = {
    filter(List[((Shop)=>Serializable, Option[Serializable])](
      (s=> "1", Some("1")),
      (_.location, request.location),
      (_.name, request.name),
      (_.address, request.address),
      (_.latitude, request.latitude),
      (_.longitude, request.longitude)
    ))
  }
}