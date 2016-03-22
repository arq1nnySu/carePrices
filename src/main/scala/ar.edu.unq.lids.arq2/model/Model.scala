package ar.edu.unq.lids.arq2.model

case class Product(var name: String, var barcode: String, prices:List[Price])

case class Price(var price:Double)


object FakeData {
  val products = List(Product("Coca", "1231", List(Price(12))), Product("Leche", "232", List(Price(10), Price(11))))
}