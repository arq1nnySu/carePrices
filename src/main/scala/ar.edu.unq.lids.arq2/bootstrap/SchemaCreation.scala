package ar.edu.unq.lids.arq2.bootstrap

import ar.edu.unq.lids.arq2.model.{Price, Shop, Product}
import net.fwbrasil.activate.migration.Migration
import ar.edu.unq.lids.arq2.CartePriceActivateContext._

class SchemaCreation extends Migration{
  def timestamp = 201604111510l

  def up = {
    table[Product]
      .createTable(
        _.column[String]("name"),
        _.column[String]("barcode")
      )
      .ifNotExists

    table[Shop]
      .createTable(
        _.column[Double]("latitude"),
        _.column[Double]("longitude"),
        _.column[String]("name"),
        _.column[String]("address"),
        _.column[String]("location")
      )
      .ifNotExists

    table[Price]
      .createTable(
        _.column[String]("shop"),
        _.column[String]("product"),
        _.column[Double]("price"),
        _.column[String]("datetime")
      )
      .ifNotExists


  }

}
