package ar.edu.unq.lids.arq2.service

import java.lang.Double
import javax.inject.Inject

import ar.edu.unq.lids.arq2.CarePriceActivateContext._
import ar.edu.unq.lids.arq2.model.{Price, Product, Shop}
import ar.edu.unq.lids.arq2.utils.ScalaBeanUtils
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.Gson
import com.twitter.finagle.http.Request
import com.twitter.finatra.json.internal.caseclass.validation.validators.NotEmptyInternal
import com.twitter.finatra.request.{JsonIgnoreBody, RouteParam, QueryParam}

import scala.beans.BeanInfo
import scala.reflect.ClassTag

@BeanInfo
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

  def id:Option[String]

  override def toString() = new Gson().toJson(this)

}

@BeanInfo
class ProductDTO extends DTO[Product]{
  var name: String = _
  var barcode: String = _
  var id:Option[String] = _
}

@BeanInfo
class PriceDTO extends DTO[Price]{
  @JsonProperty("shop_id") var shop: String = _
  @JsonProperty("product_id") var product: String = _ // GS1 code
  var price: Double = _
  var datetime: String = _
  var id:Option[String] = _
}

/*TODO Pensar cuales atributos tiene que tener validacion
case class ShopTest(
  @NotEmptyInternal name: string,
  @NotEmptyInternal address: string,
  @NotEmptyInternal location: string
  )
*/

//TODO Mergear el ShopRequest Con el ShopDTO
// El problema son los options
case class ShopRequest(
  @QueryParam var limit: Option[Int]= None,
  @QueryParam var offset: Option[Int]= None,
  @QueryParam var latitude: Option[String]= None,
  @QueryParam var longitude: Option[String]=None,
  @QueryParam var name: Option[String]=None,
  @QueryParam var address: Option[String]=None,
  @QueryParam var location : Option[String]=None,
  @JsonIgnoreBody @Inject var request:Request
) extends RequestToString

class ShopDTO() extends DTO[Shop] {
  var latitude: String =_
  var longitude: String =_
  var name:String =_
  var address:String=_
  var location : String=_
  var id:Option[String] = _
}

case class ListRequest(
  @QueryParam var limit: Option[Int]= None,
  @QueryParam var offset: Option[Int]= None,
  @JsonIgnoreBody @Inject var request:Request
)extends RequestToString

case class PriceRequest (
  @RouteParam var product_barcode: Option[String]= None,
  @QueryParam var limit: Option[Int]= None,
  @QueryParam var offset: Option[Int]= None,
  @QueryParam var from: Option[String]= None,
  @QueryParam var to: Option[String]= None,
  @QueryParam var latitude: Option[String]= None,
  @QueryParam var longitude: Option[String]=None,
  @QueryParam var shop_id: Option[String]=None,
  @QueryParam var shop_name: Option[String]=None,
  @QueryParam var address: Option[String]=None,
  @QueryParam var location: Option[String]=None,
  @JsonIgnoreBody @Inject var request:Request
) extends RequestToString

trait RequestToString {

  override def toString():String = {
    val arguments = getClass.getDeclaredFields.map(field =>{
      field.setAccessible(true)
      field.get(this)
    }).filter(!_.isInstanceOf[Request]).mkString("(", ",", ")")
    val name = getClass.getSimpleName
    name + arguments
  }

}
