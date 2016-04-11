package ar.edu.unq.lids.arq2.bootstrap
import ar.edu.unq.lids.arq2.model.{Price, Shop, Product}
//import com.twitter.finatra.http.HttpServer
import ar.edu.unq.lids.arq2.CartePriceActivateContext._

object bootstrap  {

  def data {
    val p1 = new Product("Tostadas Riera", "4646")
    val p2 = new Product("Jugo Tang", "7868678")
    new Product("Té Manzanilla", "24242")
    new Product("Arroz Molinos Ala", "345656")
    new Product("Pure de Tomate Molto", "353453")
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

    val shop1 = new Shop(0D, 0, "Chino de casa", "shangay", "Bernal")
    val shop2= new Shop(0D, 0, "Verduleria 2 personas y un boliviano", "Av. Siempre viva", "Cronica")

    new Price(shop1, p1, 20, "")
    new Price(shop2, p1, 25, "")
    new Price(shop1, p2, 10, "")
  }
}