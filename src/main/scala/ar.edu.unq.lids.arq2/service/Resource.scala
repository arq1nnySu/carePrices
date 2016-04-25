package ar.edu.unq.lids.arq2.service

import java.lang.Double

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.model.{Price, Product, Shop}
import ar.edu.unq.lids.arq2.utils.ScalaBeanUtils
import com.fasterxml.jackson.annotation.JsonProperty
import com.newrelic.agent.deps.com.google.gson.Gson
import com.twitter.finatra.json.internal.caseclass.validation.validators.NotEmptyInternal
import com.twitter.finatra.request.QueryParam

import scala.beans.BeanInfo
import scala.reflect.ClassTag

trait Resource extends Entity{ self =>
  def toData[T<:Resource](dto:Class[DTO[T]]):DTO[T] = dto.newInstance().toDTO(this.asInstanceOf[T])

  override def toString() = id
}

abstract class DTO[T<:Resource: ClassTag](implicit manifest: Manifest[T]){

  def toResource(): T ={
    val t = manifest.runtimeClass.newInstance().asInstanceOf[T]
    ScalaBeanUtils.copy(this, t)
    t
  }

  def toDTO(t:T):DTO[T] ={
    ScalaBeanUtils.copy(t, this)
    this
  }

  override def toString() = new Gson().toJson(this)

}

@BeanInfo
class ProductDTO extends DTO[Product]{
  var name: String = _
  var barcode: String = _
  var id:String = _
}

@BeanInfo
class PriceDTO extends DTO[Price]{
  @JsonProperty("shop_id") var shop: String = _
  @JsonProperty("product_id") var product: String = _ // GS1 code
  var price: Double = _
  var datetime: String = _
  var id: String = _
}

@BeanInfo
class ShopDTOff extends DTO[Shop]{
  var latitude: Double = _
  var longitude: Double = _
  var name: String = _
  var address: String = _
  var location : String = _
  var id: String = _
}

case class ShopTest(
  @NotEmptyInternal name: String,
  @NotEmptyInternal address: String,
  @NotEmptyInternal location: String
  )

//TODO Mergear el ShopRequest Con el ShopDTO
// El problema son los options
case class ShopDTO(@QueryParam var latitude: Option[Double]= None,
@QueryParam var longitude: Option[Double]=None,
@QueryParam var name: Option[String]=None,
@QueryParam var address: Option[String]=None,
@QueryParam var location : Option[String]=None,
@QueryParam var id : Option[String] =None) extends DTO[Shop]{
}


