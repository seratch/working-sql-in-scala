package example

import org.joda.time._
case class Person(id: Int, name: String, birthDate: Option[LocalDate])
case class Country(id: Int, name: String)
case class Company(id: Int, name: String, countryId: Option[Int])
case class Employee(personId: Int, companyId: Int)
case class PersonalAttributes(personId: Int, attr1: String, attr2: String, attr3: String, attr4: String, attr5: String, attr6: String, attr7: String, attr8: String, attr9: String, attr10: String)

