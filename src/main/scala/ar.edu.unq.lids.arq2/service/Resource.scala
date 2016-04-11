package ar.edu.unq.lids.arq2.service

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.model.{Product, Shop, Price}
import org.apache.commons.beanutils.BeanUtils

import scala.beans.BeanInfo
import scala.reflect.ClassTag

trait Resource extends Entity{ self =>
  def toData[T<:Resource](dto:Class[DTO[T]]):DTO[T] = dto.newInstance().toDTO(this.asInstanceOf[T])
}

abstract class DTO[T<:Resource: ClassTag](implicit manifest: Manifest[T]){

  def toResource(): T ={
    val t = manifest.runtimeClass.newInstance().asInstanceOf[T]
    BeanUtils.copyProperties(t, this)
    t
  }

  def toDTO(t:T):DTO[T] ={
    BeanUtils.copyProperties(this, t)
    this
  }

}

@BeanInfo
class ProductDTO extends DTO[Product]{
  var name: String = _
  var barcode: String = _
  var id:String = _
}

@BeanInfo
class PriceDTO extends DTO[Price]{
  var shop: String = _
  var product: String = _ // GS1 code
  var price: Double = _
  var datetime: String = _
  var id: String = _
}

@BeanInfo
class ShopDTO extends DTO[Shop]{
  var latitude: Double = _
  var longitude: Double = _
  var name: String = _
  var address: String = _
  var location : String = _
  var id: String = _
}
