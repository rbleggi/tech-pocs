package com.rbleggi.guitarfactory.factory

import com.rbleggi.guitarfactory.model.{AcousticGuitar, BassGuitar, ElectricGuitar, Guitar}

object GuitarFactory {
  def createGuitar(guitarType: String, model: String, specs: String, os: String): Guitar = {
    guitarType.toLowerCase match {
      case "acoustic" => AcousticGuitar(model, specs, os)
      case "electric" => ElectricGuitar(model, specs, os)
      case "bass" => BassGuitar(model, specs, os)
      case _ => throw new IllegalArgumentException("Unknown guitar type")
    }
  }
}