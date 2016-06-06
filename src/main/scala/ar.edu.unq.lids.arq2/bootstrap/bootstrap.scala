package ar.edu.unq.lids.arq2.bootstrap

import ar.edu.unq.lids.arq2.model.{Price, Shop, Product}
import ar.edu.unq.lids.arq2.CarePriceActivateContext._

object bootstrap {
  
  def data {
    val p1 = new Product("Tostadas Riera", "4646")
    val p2 = new Product("Jugo Tang", "7868678")
    val p3 = new Product("Té Manzanilla", "24242")
    val p4 = new Product("Arroz Molinos Ala", "345656")
    val p5 = new Product("Pure de Tomate Molto", "353453")
    new Product("Aceite Cañuelas", "12446456")
    new Product("Alcohol", "5676786")
    new Product("Vino Cosecha Tardia", "46456")
    new Product("Atun Campañola", "787686")
    new Product("Dulce De Leche", "345354")
    new Product("Vinagre", "46456")
    new Product("Soda", "45644")
    new Product("Desodorante Rexona", "53543")
    new Product("Arroz Molinos Ala", "23234234")
    new Product("Azucar Ledesma", "54654657")
    new Product("Gaseosa Coca Cola", "5645")
    new Product("Detergente Ala", "123123213")
    new Product("Leche Serenisima", "97898")
    new Product("Lentejas Cañuelas", "123123")
    new Product("Jabon Liquido Ariel", "5657")
    new Product("Huevos 6 unidades", "3334")
    new Product("Sal Fina11 Dos Anclas", "343535")
    new Product("Mayonesa Natura", "46456456")
    new Product("Galletitas Don Satur", "456565")

    val shop1 = new Shop("", "", "Chino de casa", "shangay", "Bernal")
    val shop2 = new Shop("0", "", "Verduleria 2 personas y un boliviano", "Av. Siempre viva", "Cronica")

    val shop3 = new Shop("7", "5", "Coto", "Rivadavia n 124 ", "Banfield")
    val shop4 = new Shop("1", "", "Juancito Almacen ", "Av. Colon", "Ranchos")

    val shop5 = new Shop("", "", "Auchan", "Saveedra 78", "Quilmes")
    val shop6 = new Shop("", "8", "Tomate un te", "kita 23", "Almirante Brown")

    val shop7 = new Shop("2", "", "Caferour", "jeppener 3", "Avellaneda")
    val shop8 = new Shop("0", "", "Disco tu sabes", "Av. Cabildo", "Recoleta")

    val shop9 = new Shop("", "", "Chino de casa", "shangay", "Belgrano")
    val shop10 = new Shop("0", "", "Verduleria 2 tomates para ti", "Av. Siempre viva", "Platanos")

    val shop11 = new Shop("", "", "Chino de Chas", "shangay", "Chascomus")
    val shop12 = new Shop("0", "", "Verduleria los 2 loros", "Av. Siempre viva", "Glew")

    val shop13 = new Shop("", "", "Potentera 43", "Tomillo 45", "Villanueva")
    val shop14 = new Shop("0", "", "Henry - tu Almacen", "Av. Irigoyen 6", "Korn")

    new Price(shop1, p1.barcode, 20, "20/10/2015")
    new Price(shop2, p1.barcode, 25, "20/11/2015")
    new Price(shop2, p1.barcode, 10, "20/12/2015")
    new Price(shop1, p1.barcode, 20, "20/103/2015")
  }
}
