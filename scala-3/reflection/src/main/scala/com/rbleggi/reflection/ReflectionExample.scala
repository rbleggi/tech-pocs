package com.rbleggi.reflection

import scala.reflect.ClassTag
import java.lang.reflect.{Method, Field, Constructor}

case class Person(name: String, age: Int, email: String)
case class Product(id: Long, name: String, price: Double, inStock: Boolean)

object ReflectionUtils:
  def getClassName[A](value: A): String = value.getClass.getName

  def getSimpleClassName[A](value: A): String = value.getClass.getSimpleName

  def getMethods[A](value: A): List[String] =
    value.getClass.getMethods.map(_.getName).toList.sorted.distinct

  def getFields[A](value: A): List[String] =
    value.getClass.getDeclaredFields.map(_.getName).toList

  def getConstructors[A](value: A): List[Constructor[?]] =
    value.getClass.getConstructors.toList

  def isInstanceOf[A](value: Any, clazz: Class[A]): Boolean =
    clazz.isInstance(value)

  def getFieldValue[A](obj: A, fieldName: String): Option[Any] =
    try
      val field = obj.getClass.getDeclaredField(fieldName)
      field.setAccessible(true)
      Some(field.get(obj))
    catch
      case _: Exception => None

  def invokeMethod[A](obj: A, methodName: String, args: Any*): Option[Any] =
    try
      val method = obj.getClass.getMethod(methodName, args.map(_.getClass)*)
      Some(method.invoke(obj, args.map(_.asInstanceOf[Object])*))
    catch
      case _: Exception => None

  def createInstance[A](clazz: Class[A], args: Any*): Option[A] =
    try
      val constructor = clazz.getConstructors()(0)
      Some(constructor.newInstance(args.map(_.asInstanceOf[Object])*).asInstanceOf[A])
    catch
      case _: Exception => None

object TypeInspector:
  def inspectType[A: ClassTag]: Unit =
    val tag = summon[ClassTag[A]]
    println(s"Type: ${tag.runtimeClass.getName}")
    println(s"Simple name: ${tag.runtimeClass.getSimpleName}")
    println(s"Is interface: ${tag.runtimeClass.isInterface}")
    println(s"Is primitive: ${tag.runtimeClass.isPrimitive}")

    val methods = tag.runtimeClass.getDeclaredMethods
    if methods.nonEmpty then
      println(s"Methods (${methods.length}):")
      methods.take(5).foreach { m =>
        println(s"  - ${m.getName}: ${m.getReturnType.getSimpleName}")
      }

  def compareSuperTypes[A: ClassTag, B: ClassTag]: Unit =
    val tagA = summon[ClassTag[A]]
    val tagB = summon[ClassTag[B]]

    val classA = tagA.runtimeClass
    val classB = tagB.runtimeClass

    println(s"Comparing ${classA.getSimpleName} and ${classB.getSimpleName}")
    println(s"  A is assignable from B: ${classA.isAssignableFrom(classB)}")
    println(s"  B is assignable from A: ${classB.isAssignableFrom(classA)}")

object SerializationUtils:
  def toMap(obj: Any): Map[String, Any] =
    val fields = obj.getClass.getDeclaredFields
    fields.map { field =>
      field.setAccessible(true)
      field.getName -> field.get(obj)
    }.toMap

  def toKeyValuePairs(obj: Any): List[(String, String)] =
    toMap(obj).map { case (k, v) => (k, v.toString) }.toList

  def prettyPrint(obj: Any): String =
    val className = obj.getClass.getSimpleName
    val fields = toKeyValuePairs(obj).map { case (k, v) => s"$k=$v" }.mkString(", ")
    s"$className($fields)"

trait JsonSerializer:
  def toJson(obj: Any): String =
    obj match
      case s: String => s""""$s""""
      case n: Int => n.toString
      case n: Long => n.toString
      case n: Double => n.toString
      case b: Boolean => b.toString
      case null => "null"
      case _ =>
        val fields = SerializationUtils.toMap(obj)
        val jsonFields = fields.map { case (k, v) => s""""$k":${toJson(v)}""" }.mkString(",")
        s"{$jsonFields}"

object JsonSerializer extends JsonSerializer

@main def reflectionExample(): Unit =
  println("=== Reflection Example ===\n")

  val person = Person("Alice", 30, "alice@example.com")
  val product = Product(1001, "Laptop", 999.99, true)

  println("1. Class Information")
  println(s"Class name: ${ReflectionUtils.getClassName(person)}")
  println(s"Simple name: ${ReflectionUtils.getSimpleClassName(person)}")

  println("\n2. Fields Inspection")
  println(s"Person fields: ${ReflectionUtils.getFields(person).mkString(", ")}")
  println(s"Product fields: ${ReflectionUtils.getFields(product).mkString(", ")}")

  println("\n3. Field Values (via Reflection)")
  ReflectionUtils.getFieldValue(person, "name").foreach(v => println(s"person.name = $v"))
  ReflectionUtils.getFieldValue(person, "age").foreach(v => println(s"person.age = $v"))
  ReflectionUtils.getFieldValue(product, "price").foreach(v => println(s"product.price = $v"))

  println("\n4. Method Invocation")
  val str = "hello"
  ReflectionUtils.invokeMethod(str, "toUpperCase").foreach(result =>
    println(s"'$str'.toUpperCase() = $result")
  )
  ReflectionUtils.invokeMethod(str, "length").foreach(result =>
    println(s"'$str'.length() = $result")
  )

  println("\n5. Instance Creation")
  ReflectionUtils.createInstance(
    classOf[Person],
    "Bob",
    Integer.valueOf(25),
    "bob@example.com"
  ).foreach(p => println(s"Created: $p"))

  println("\n6. Type Inspection with ClassTag")
  TypeInspector.inspectType[String]
  println()
  TypeInspector.inspectType[List[Int]]

  println("\n7. Serialization via Reflection")
  println(s"Person as Map: ${SerializationUtils.toMap(person)}")
  println(s"Product as Map: ${SerializationUtils.toMap(product)}")

  println("\n8. Pretty Printing")
  println(SerializationUtils.prettyPrint(person))
  println(SerializationUtils.prettyPrint(product))

  println("\n9. JSON Serialization")
  println(JsonSerializer.toJson(person))
  println(JsonSerializer.toJson(product))

  println("\n10. Type Comparison")
  TypeInspector.compareSuperTypes[Any, String]

  println("\n=== Reflection Use Cases ===")
  println("1. Object serialization/deserialization")
  println("2. Dependency injection frameworks")
  println("3. ORM frameworks")
  println("4. Testing frameworks")
  println("5. Generic utilities")
  println("\n=== Caution ===")
  println("- Runtime overhead")
  println("- Type safety lost")
  println("- Prefer compile-time alternatives when possible")

