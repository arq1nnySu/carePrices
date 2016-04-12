package ar.edu.unq.lids.arq2.service

import ar.edu.unq.lids.arq2.model.{Product, Shop, Price}
import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.persistence.Repository
import net.fwbrasil.activate.statement.StatementSelectValue

class ResourceService[T<:Resource, D<:DTO[T]](implicit manifestT: Manifest[T], manifestD: Manifest[D]){
  type Dto = DTO[T]
  var repository = new Repository[T]

  implicit def dtoToResource(dto:D) = dto.toResource()
  implicit def resourceToDTO(t:T):D = t.toData(manifestD.runtimeClass.asInstanceOf[Class[Dto]]).asInstanceOf[D]

  def save(data:D):Dto = transactional{
    repository.save(data)
  }

  def all = transactional{
    repository.alll.map(resourceToDTO(_))
  }

  def get(ff:(T)=>String, value:StatementSelectValue):Dto = transactional{
    repository.get(ff, value)
  }
}

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