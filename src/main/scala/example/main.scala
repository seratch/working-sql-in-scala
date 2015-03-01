package example

object Main
    extends App
    with DBInitializer
    with ScalikeJDBCExample
    with SlickStaticQueryExample
    with AnormExample {

  val personId = 123
  runScalikeJDBCExample(personId)
  runSlickStaticQueryExample(personId)
  runAnormExample(personId)
}

