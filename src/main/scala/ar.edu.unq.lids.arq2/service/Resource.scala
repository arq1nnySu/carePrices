package ar.edu.unq.lids.arq2.service

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.model.Product
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
