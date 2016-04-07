package ar.edu.unq.lids.arq2.model


import ar.edu.unq.lids.arq2.CartePriceActivateContext._

//import com.sun.prism.PixelFormat.DataType
import org.apache.commons.beanutils.BeanUtils
import scala.beans.{BeanProperty, BeanInfo}
import scala.reflect.ClassTag
import ar.edu.unq.lids.arq2.service.Resource

import scala.beans.BeanInfo

@BeanInfo
class Product extends Entity with Resource{
  var name: String = _
  var barcode: String = _
}

@BeanInfo
class Price extends Entity with Resource{
  var price: Double = _
  var name: String = _
}
