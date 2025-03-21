package com.rbleggi.guitarfactory.model

trait Guitar {
  def model: String
  def specs: String
  def os: String
  override def toString: String = s"Model: $model, Specs: $specs, OS: $os"
}

case class ElectricGuitar(model: String, specs: String, os: String) extends Guitar
case class AcousticGuitar(model: String, specs: String, os: String) extends Guitar
case class BassGuitar(model: String, specs: String, os: String) extends Guitar
