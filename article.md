## What's ScalikeJDBC

 I'm a maintainer of ScalikeJDBC which is a DB access library in Scala. This library naturally wraps JDBC APIs and provides you easy-to-use and very flexible APIs.

http://scalikejdbc.org/

## SQL Interpolation

 ScalikeJDBC uses [String Interpolation (SIP-11)](http://docs.scala-lang.org/sips/completed/string-interpolation.html) to generate SQL statements. We call this feature [SQL interpolation](http://scalikejdbc.org/documentation/sql-interpolation.html). The concept of SQL interpolation was [originally invented by Slick](http://slick.typesafe.com/doc/1.0.0/sql.html#string-interpolation) and recently Play's [Anorm](https://www.playframework.com/documentation/2.3.x/ScalaAnorm)  has [the same feature](https://www.playframework.com/documentation/2.3.x/ScalaAnorm#SQL-queries-using-String-Interpolation). Some people ask me what the difference between ScalikeJDBC and other libraries.

 I think the most powerful feature of ScalikeJDBC's SQL interpolation is [`scalikejdbc.SQLSyntax`](https://github.com/scalikejdbc/scalikejdbc/blob/master/scalikejdbc-core/src/main/scala/scalikejdbc/interpolation/SQLSyntax.scala) and [`scalikejdbc.SQLSyntaxSupport`](https://github.com/scalikejdbc/scalikejdbc/blob/master/scalikejdbc-interpolation/src/main/scala/scalikejdbc/SQLSyntaxSupportFeature.scala). Other libraries don't have the same features. `SQLSyntax` is not a bind variable but a value which will be directly embedded as a part of SQL statements. `SQLSyntaxSupport` trait generates SQLSyntax objects to represent column names, table aliases or any parts of SQL. You can learn about them here:

http://scalikejdbc.org/documentation/sql-interpolation.html

## Plain SQL Code Example

Let's think about SQL queries that have multiple joins. I've created a small example to show what the good point of ScalikeJDBC's interpolation is.

https://github.com/seratch/working-sql-in-scala

Examples are here:

https://github.com/seratch/working-sql-in-scala/blob/master/src/main/scala/example/examples.scala

The following SQL interpolation code in `ScalikeJDBCExample` trait,

    // SQLSyntaxSupport provides SQLSyntax for defined tables
    val (cnt, cmp, p, e, pe) = (CountryTable.syntax("cnt"), CompanyTable.syntax("cmp"), PersonTable.syntax("p"), EmployeeTable.syntax("e"), PersonalAttributesTable.syntax("pe"))
    
    sql"""
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

generates the following SQL statement:

    select
      p.id as i_on_p, 
      p.name as n_on_p, 
      p.birth_date as bd_on_p,
      cmp.id as i_on_cmp, 
      cmp.name as n_on_cmp, 
      cmp.country_id as ci_on_cmp,
      cnt.id as i_on_cnt, 
      cnt.name as n_on_cnt,
      pe.person_id as pi_on_pe, 
      pe.attr1 as a1_on_pe, 
      pe.attr2 as a2_on_pe, 
      pe.attr3 as a3_on_pe, 
      pe.attr4 as a4_on_pe, 
      pe.attr5 as a5_on_pe, 
      pe.attr6 as a6_on_pe, 
      pe.attr7 as a7_on_pe, 
      pe.attr8 as a8_on_pe, 
      pe.attr9 as a9_on_pe, 
      pe.attr10 as a10_on_pe
    from
      person p
      left join employee e  on e.person_id = p.id
      left join company cmp on cmp.id = e.company_id
      left join country cnt on cnt.id = cmp.country_id
      left join personal_attributes pe on pe.person_id = p.id
    where
      p.id = ?

 [Slick StaticQuery](http://slick.typesafe.com/doc/2.1.0/sql.html) has `#${...}` feature to embed literal values in SQL statements.

    val personalAttributes: String = 
      (1 to 10)
        .map(i => s"pe.attr${i} as personal_attr${i}")
        .mkString(",")
    
    sql"""
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
      p.id = ?
    """

Since Anorm doesn't support literal values, library users need to write full SQL statements or use string concatenation (in String interplation) like this:

    SQL(
    // this is not SQL interpolation!
    s"""
    select
      p.id as person_id, 
      p.name as person_name, 
      p.birth_date as person_birth_date,
      cmp.id as company_id, 
      cmp.name as company_name, 
      cnt.id as country_id, 
      cnt.name as country_name,
      ${personalAttributes}
    from 
      person p
      left join employee e  on e.person_id = p.id
      left join company cmp on cmp.id = e.company_id
      left join country cnt on cnt.id = cmp.country_id
      left join personal_attributes pe on pe.person_id = p.id
    where
      p.id = ?
    """)  

### Conclusion

 Of course, Slick's main feature is not querying with plain SQL statements but [querying in the same way as when working with Scala collections](http://slick.typesafe.com/doc/2.1.0/queries.html). I know fully well that it's unfair to compare with ScalikeJDBC only by this aspect.

 Anyway, ScalikeJDBC will make your code which has SQL statements DRY (Don't Repeat Yourself) especially when writing lots of different SQL statements. If you're interested in ScalikeJDBC, take a look at QueryDSL too.

http://scalikejdbc.org/documentation/query-dsl.html

 If you prefer more handy ORM, I highly recommend you checking Skinny ORM. This library is a Rails ActiveRecord-ish ORM library which is built upon ScalikeJDBC.
 
http://skinny-framework.org/documentation/orm.html


