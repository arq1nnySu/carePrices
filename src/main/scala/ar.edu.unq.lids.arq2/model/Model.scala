package ar.edu.unq.lids.arq2.model

import ar.edu.unq.lids.arq2.CartePriceActivateContext._

//import com.sun.prism.PixelFormat.DataType
import org.apache.commons.beanutils.BeanUtils
import scala.beans.{BeanProperty, BeanInfo}
import scala.reflect.ClassTag
import ar.edu.unq.lids.arq2.service.Resource

import scala.beans.BeanInfo

@BeanInfo
class Shop extends Entity with Resource{
  var latitude: Double = _
  var longitude: Double = _
  var name: String = _
  var address: String = _
  var location : String = _

  def this(latitude: Double, longitude: Double, name: String, address: String, location: String){
    this()
    this.latitude = latitude
    this.longitude = longitude
    this.name = name
    this.address = address
    this.location = location
  }
}

@BeanInfo
class Product extends Entity with Resource{
  var name: String = _
  var barcode: String = _

  def this(name: String, barcode: String) {
    this()
    this.name = name
    this.barcode = barcode
  }

}

@BeanInfo
class Price extends Entity with Resource{
  var shop: Shop = _
  var product: Product = _
  var price: Double = _
  var datetime: String = _

  def this(shop: Shop, product: Product, price: Double, datetime: String) {
  this()
    this.shop = shop
    this.product = product
    this.price = price
    this.datetime = datetime
  }
}