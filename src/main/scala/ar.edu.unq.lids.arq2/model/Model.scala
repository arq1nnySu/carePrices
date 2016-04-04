package ar.edu.unq.lids.arq2.model


import ar.edu.unq.lids.arq2.CartePriceActivateContext._
import ar.edu.unq.lids.arq2.service.Resource

import scala.beans.BeanInfo

@BeanInfo
case class Product(var name: String, var barcode: String) extends Entity with Resource{
  def this(){
    this("", "")
  }
}


//class Price(var price:Double, var name:String) extends Entity with Resource
