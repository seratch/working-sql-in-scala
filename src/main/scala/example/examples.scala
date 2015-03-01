package example

object JDBCSettings {
  val jdbcDriver = "org.h2.Driver"
  val jdbcUrl = "jdbc:h2:mem:hello;MODE=PostgreSQL"
  val jdbcUser = "user"
  val jdbcPassword = "password"
}

trait DBInitializer {
  import JDBCSettings._
  import scalikejdbc._
  Class.forName(jdbcDriver)
  ConnectionPool.singleton(jdbcUrl, jdbcUser, jdbcPassword)
  DB.autoCommit { implicit s =>
    sql"create table country (id int primary key not null, name varchar(100) not null)".execute.apply()
    sql"create table company (id int primary key not null, name varchar(100) not null, country_id int references country(id))".execute.apply()
    sql"create table person (id int primary key not null, name varchar(100) not null, birth_date date)".execute.apply()
    sql"create table employee (person_id int not null references person(id), company_id int not null references company(id))".execute.apply()
    sql"""
      create table personal_attributes (
        person_id int primary key not null references person(id),
        attr1 varchar(30),
        attr2 varchar(30),
        attr3 varchar(30),
        attr4 varchar(30),
        attr5 varchar(30),
        attr6 varchar(30),
        attr7 varchar(30),
        attr8 varchar(30),
        attr9 varchar(30),
        attr10 varchar(30)
      )
    """.execute.apply()

    Seq(
      sql"insert into country (id, name) values (1, 'Japan')",
      sql"insert into company (id, name, country_id) values (2, 'Toyota', 1)",
      sql"insert into person (id, name, birth_date) values (123, 'Taro Yamada', '1980-01-02')",
      sql"insert into employee (person_id, company_id) values (123, 2)",
      sql"insert into personal_attributes (person_id, attr1, attr2) values (123, 'GitHub user', 'Nagoya')"
    ).foreach(_.update.apply())
  }
}

// http://scalikejdbc.org/documentation/sql-interpolation.html
trait ScalikeJDBCExample {
  import scalikejdbc._

  object CountryTable extends SQLSyntaxSupport[Country] { override val tableName = "country" }
  object CompanyTable extends SQLSyntaxSupport[Company] { override val tableName = "company" }
  object PersonTable extends SQLSyntaxSupport[Person] { override val tableName = "person" }
  object EmployeeTable extends SQLSyntaxSupport[Employee] { override val tableName = "employee" }
  object PersonalAttributesTable extends SQLSyntaxSupport[PersonalAttributes] { override val tableName = "personal_attributes" }

  def runScalikeJDBCExample(personId: Int) = {
    val (cnt, cmp, p, e, pe) = (CountryTable.syntax("cnt"), CompanyTable.syntax("cmp"), PersonTable.syntax("p"), EmployeeTable.syntax("e"), PersonalAttributesTable.syntax("pe"))
    val query = sql"""
select
  ${p.result.*},
  ${cmp.result.*},
  ${cnt.result.*},
  ${pe.result.*}
from
  ${PersonTable as p}
  left join ${EmployeeTable as e}  on ${e.personId} = ${p.id}
  left join ${CompanyTable as cmp} on ${cmp.id} = ${e.companyId}
  left join ${CountryTable as cnt} on ${cnt.id} = ${cmp.countryId}
  left join ${PersonalAttributesTable as pe} on ${pe.personId} = ${p.id}
where
  ${p.id} = ${personId}
    """
    println
    println("----- ScalikeJDBC -----")
    println("statement:" + query.statement)
    println("parameters:" + query.parameters)
    println

    val results = DB.readOnly { implicit s =>
      query.toMap.list.apply()
    }
    println(results)
    println
  }
}

// http://slick.typesafe.com/doc/2.1.0/sql.html
trait SlickStaticQueryExample {
  import JDBCSettings._

  import scala.slick.driver.H2Driver.simple._
  import scala.slick.driver.JdbcDriver.backend.Database
  import Database.dynamicSession
  import scala.slick.jdbc.{ GetResult, StaticQuery => Q }
  import Q.interpolation

  def runSlickStaticQueryExample(personId: Int) = {
    // #${...} embeds string values directly
    // It's possible to do the same in ScalikeJDBC: scalikejdbc.SQLSyntax.createUnsafely(personalAttributes)
    val personalAttributes: String = (1 to 10).map(i => s"pe.attr${i} as personal_attr${i}").mkString(",")
    val query = sql"""
select
  p.id as person_id, 
  p.name as person_name, 
  p.birth_date as person_birth_date,
  cmp.id as company_id, 
  cmp.name as company_name, 
  cnt.id as country_id, 
  cnt.name as country_name,
  #${personalAttributes}
from
  person p
  left join employee e  on e.person_id = p.id
  left join company cmp on cmp.id = e.company_id
  left join country cnt on cnt.id = cmp.country_id
  left join personal_attributes pe on pe.person_id = p.id
where
  p.id = ${personId}
  """
    println
    println("----- Slick -----")
    println(query)
    println

    implicit val resultAsStringMap = GetResult[Map[String, String]](prs =>
      (1 to prs.numColumns).map(_ =>
        prs.rs.getMetaData.getColumnName(prs.currentPos + 1) -> prs.nextString
      ).toMap
    )
    val database = Database.forURL(jdbcUrl, user = jdbcUser, password = jdbcPassword, driver = jdbcDriver)
    val results = database.withDynSession { query.as[Map[String, String]].list }
    println(results)
    println
  }
}

// https://www.playframework.com/documentation/2.3.x/ScalaAnorm
trait AnormExample {
  import scalikejdbc.DB
  import anorm._

  def runAnormExample(personId: Int) = {
    // or string concatenation like this:
    // val personalAttributes: String = (1 to 10).map(i => s"pe.attr${i} as personal_attr${i}").mkString(",")
    // SQL("... cnt.name as country_name, " +  personalAttributes + " from ...")
    val query = SQL"""
select
  p.id as person_id, 
  p.name as person_name, 
  p.birth_date as person_birth_date,
  cmp.id as company_id, 
  cmp.name as company_name, 
  cnt.id as country_id, 
  cnt.name as country_name,
  pe.attr1 as personal_attr1,
  pe.attr2 as personal_attr2,
  pe.attr3 as personal_attr3,
  pe.attr4 as personal_attr4,
  pe.attr5 as personal_attr5,
  pe.attr6 as personal_attr6,
  pe.attr7 as personal_attr7,
  pe.attr8 as personal_attr8,
  pe.attr9 as personal_attr9,
  pe.attr10 as personal_attr10
from
  person p
  left join employee e  on e.person_id = p.id
  left join company cmp on cmp.id = e.company_id
  left join country cnt on cnt.id = cmp.country_id
  left join personal_attributes pe on pe.person_id = p.id
where
  p.id = ${personId}
  """
    println
    println("----- Anorm -----")
    println(query)
    println
    // ScalikeJDBC connection management instead of Play DBApi
    val results = DB.readOnlyWithConnection { implicit conn => query.apply() }
    println(results)
    println
  }
}
