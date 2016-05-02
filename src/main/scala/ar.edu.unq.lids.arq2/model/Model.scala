package ar.edu.unq.lids.arq2.model

import java.lang.Double

import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.service.Resource

import scala.beans.BeanInfo

@BeanInfo
class Shop extends Entity with Resource {
  var latitude: String = _
  var longitude: String = _
  var name: String = _ // Local
  var address: String = _
  var location: String = _ // Barrio
  var chain: String = _ // Cadena de supermercado a la que pertenece

  def this(latitude: String, longitude: String, name: String, address: String, location: String){
    this()
    this.latitude = latitude
    this.longitude = longitude
    this.name = name
    this.address = address
    this.location = location
  }
}

@BeanInfo
class Product extends Entity with Resource {
  var name: String = _
  var barcode: String = _
  var score: Int = _ // Puntaje del producto

  def this(name: String, barcode: String) {
    this()
    this.name = name
    this.barcode = barcode
  }
}

@BeanInfo
class Price extends Entity with Resource {
  var shop: Shop = _
  var product: String = _
  var price: Double = _
  var datetime: String = _

  def this(shop: Shop, product: String, price: Double, datetime: String) {
    this()
    this.shop = shop
    this.product = product
    this.price = price
    this.datetime = datetime
  }
}
