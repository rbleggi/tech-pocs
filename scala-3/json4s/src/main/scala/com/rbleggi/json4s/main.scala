package com.rbleggi.json4s

import org.json4s.native.JsonParser


object Json4sQuickDemo extends App {

  JsonParser.parse("""{"application": "MyApp", "version": 1.0}""")

}