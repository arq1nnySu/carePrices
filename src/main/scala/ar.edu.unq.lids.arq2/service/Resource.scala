package ar.edu.unq.lids.arq2.service

import java.lang.Double

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.model.{Product, Shop, Price}
import com.fasterxml.jackson.annotation.{JsonProperty, JsonIgnore}
import com.twitter.finatra.request.QueryParam
import com.newrelic.agent.deps.com.google.gson.Gson
import org.apache.commons.beanutils.BeanUtils

import scala.beans.BeanInfo
import scala.reflect.ClassTag

trait Resource extends Entity{ self =>
  def toData[T<:Resource](dto:Class[DTO[T]]):DTO[T] = dto.newInstance().toDTO(this.asInstanceOf[T])

  override def toString() = id
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
class ShopDTO extends DTO[Shop]{
  var latitude: Double = _
  var longitude: Double = _
  var name: String = _
  var address: String = _
  var location : String = _
  var id: String = _
}

//TODO Mergear el ShopRequest Con el ShopDTO
// El problema son los options
case class ShopSearchRequest(
  @QueryParam latitude: Option[Double],
  @QueryParam longitude: Option[Double],
  @QueryParam name: Option[String],
  @QueryParam address: Option[String],
  @QueryParam location : Option[String]
)


