package ar.edu.unq.lids.arq2.model


import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import com.sun.prism.PixelFormat.DataType
import org.apache.commons.beanutils.BeanUtils
import scala.beans.{BeanProperty, BeanInfo}
import scala.reflect.ClassTag

trait Resource extends Entity{ self =>
  def toData[T<:Resource](dto:Class[DTO[T]]) = dto.newInstance().toDTO(this.asInstanceOf[T])
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
  var name: String =_
  var barcode: String=_
  var id:String =_
}

@BeanInfo
case class Product(var name: String, var barcode: String) extends Entity with Resource{
  def this(){
    this("", "")
  }
}


//class Price(var price:Double, var name:String) extends Entity with Resource
