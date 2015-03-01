# Working with Plain SQL in Scala

This is a simple example to show how to work with SQL queries in Scala. ScalikeJDBC, Slick StaticQuery and Anorm.

- [ScalikeJDBC 2.2.4](http://scalikejdbc.org/documentation/sql-interpolation.html)
- [Slick 2.1.0](http://slick.typesafe.com/doc/2.1.0/sql.html)
- [Anorm 2.3.8](https://www.playframework.com/documentation/2.3.8/ScalaAnorm)

## How to run

Just `sbt run` will run [examples](https://github.com/seratch/working-sql-in-scala/blob/master/src/main/scala/example/examples.scala).

```
$ sbt run

----- ScalikeJDBC -----
statement:
select
  p.id as i_on_p, p.name as n_on_p, p.birth_date as bd_on_p,
  cmp.id as i_on_cmp, cmp.name as n_on_cmp, cmp.country_id as ci_on_cmp,
  cnt.id as i_on_cnt, cnt.name as n_on_cnt,
  pe.person_id as pi_on_pe, pe.attr1 as a1_on_pe, pe.attr2 as a2_on_pe, pe.attr3 as a3_on_pe, pe.attr4 as a4_on_pe, pe.attr5 as a5_on_pe, pe.attr6 as a6_on_pe, pe.attr7 as a7_on_pe, pe.attr8 as a8_on_pe, pe.attr9 as a9_on_pe, pe.attr10 as a10_on_pe
from
  person p
  left join employee e  on e.person_id = p.id
  left join company cmp on cmp.id = e.company_id
  left join country cnt on cnt.id = cmp.country_id
  left join personal_attributes pe on pe.person_id = p.id
where
  p.id = ?

parameters:List(123)

20:35:38.945 [run-main-0] DEBUG s.StatementExecutor$$anon$1 - SQL execution completed

  [SQL Execution]
   select p.id as i_on_p, p.name as n_on_p, p.birth_date as bd_on_p, cmp.id as i_on_cmp, cmp.name as n_on_cmp, cmp.country_id as ci_on_cmp, cnt.id as i_on_cnt, cnt.name as n_on_cnt, pe.person_id as pi_on_pe, pe.attr1 as a1_on_pe, pe.attr2 as a2_on_pe, pe.attr3 as a3_on_pe, pe.attr4 as a4_on_pe, pe.attr5 as a5_on_pe, pe.attr6 as a6_on_pe, pe.attr7 as a7_on_pe, pe.attr8 as a8_on_pe, pe.attr9 as a9_on_pe, pe.attr10 as a10_on_pe from person p left join employee e on e.person_id = p.id left join company cmp on cmp.id = e.company_id left join country cnt on cnt.id = cmp.country_id left join personal_attributes pe on pe.person_id = p.id where p.id = 123; (1 ms)

  [Stack Trace]
    ...
    example.ScalikeJDBCExample$$anonfun$2.apply(examples.scala:80)
    example.ScalikeJDBCExample$$anonfun$2.apply(examples.scala:79)
    scalikejdbc.DBConnection$class.readOnly(DBConnection.scala:206)
    scalikejdbc.DB.readOnly(DB.scala:75)
    scalikejdbc.DB$$anonfun$readOnly$1.apply(DB.scala:188)
    scalikejdbc.DB$$anonfun$readOnly$1.apply(DB.scala:187)
    scalikejdbc.LoanPattern$class.using(LoanPattern.scala:33)
    scalikejdbc.DB$.using(DB.scala:153)
    scalikejdbc.DB$.readOnly(DB.scala:187)
    example.ScalikeJDBCExample$class.runScalikeJDBCExample(examples.scala:79)
    example.Main$.runScalikeJDBCExample(main.scala:3)
    example.Main$.delayedEndpoint$example$Main$1(main.scala:11)
    example.Main$delayedInit$body.apply(main.scala:4)
    scala.Function0$class.apply$mcV$sp(Function0.scala:40)
    scala.runtime.AbstractFunction0.apply$mcV$sp(AbstractFunction0.scala:12)
    ...

List(Map(I_ON_CNT -> 1, A2_ON_PE -> Nagoya, A1_ON_PE -> GitHub user, PI_ON_PE -> 123, I_ON_P -> 123, I_ON_CMP -> 2, N_ON_CNT -> Japan, N_ON_P -> Taro Yamada, BD_ON_P -> 1980-01-02, N_ON_CMP -> Toyota, CI_ON_CMP -> 1))


----- Slick -----
SQLInterpolationResult(WrappedArray(
select
  p.id as person_id,
  p.name as person_name,
  p.birth_date as person_birth_date,
  cmp.id as company_id,
  cmp.name as company_name,
  cnt.id as country_id,
  cnt.name as country_name,
  #,
from
  person p
  left join employee e  on e.person_id = p.id
  left join company cmp on cmp.id = e.company_id
  left join country cnt on cnt.id = cmp.country_id
  left join personal_attributes pe on pe.person_id = p.id
where
  p.id = ,
  ),(pe.attr1 as personal_attr1,pe.attr2 as personal_attr2,pe.attr3 as personal_attr3,pe.attr4 as personal_attr4,pe.attr5 as personal_attr5,pe.attr6 as personal_attr6,pe.attr7 as personal_attr7,pe.attr8 as personal_attr8,pe.attr9 as personal_attr9,pe.attr10 as personal_attr10,123),SetTupleParameter<2>)

20:35:39.167 [run-main-0] DEBUG s.slick.jdbc.JdbcBackend.statement - Preparing statement:
select
  p.id as person_id,
  p.name as person_name,
  p.birth_date as person_birth_date,
  cmp.id as company_id,
  cmp.name as company_name,
  cnt.id as country_id,
  cnt.name as country_name,
  pe.attr1 as personal_attr1,pe.attr2 as personal_attr2,pe.attr3 as personal_attr3,pe.attr4 as personal_attr4,pe.attr5 as personal_attr5,pe.attr6 as personal_attr6,pe.attr7 as personal_attr7,pe.attr8 as personal_attr8,pe.attr9 as personal_attr9,pe.attr10 as personal_attr10
from
  person p
  left join employee e  on e.person_id = p.id
  left join company cmp on cmp.id = e.company_id
  left join country cnt on cnt.id = cmp.country_id
  left join personal_attributes pe on pe.person_id = p.id
where
  p.id = ?

20:35:39.176 [run-main-0] DEBUG s.slick.jdbc.JdbcBackend.benchmark - Execution of prepared statement took 1ms
20:35:39.196 [run-main-0] DEBUG s.slick.jdbc.StatementInvoker.result - /-----------+-------------+-------------------+------------+--------------+------------+--------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+-----------------\
20:35:39.196 [run-main-0] DEBUG s.slick.jdbc.StatementInvoker.result - | PERSON_ID | PERSON_NAME | PERSON_BIRTH_DATE | COMPANY_ID | COMPANY_NAME | COUNTRY_ID | COUNTRY_NAME | PERSONAL_ATTR1 | PERSONAL_ATTR2 | PERSONAL_ATTR3 | PERSONAL_ATTR4 | PERSONAL_ATTR5 | PERSONAL_ATTR6 | PERSONAL_ATTR7 | PERSONAL_ATTR8 | PERSONAL_ATTR9 | PERSONAL_ATTR10 |
20:35:39.196 [run-main-0] DEBUG s.slick.jdbc.StatementInvoker.result - +-----------+-------------+-------------------+------------+--------------+------------+--------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+-----------------+
20:35:39.197 [run-main-0] DEBUG s.slick.jdbc.StatementInvoker.result - | 123       | Taro Yamada | 1980-01-02        | 2          | Toyota       | 1          | Japan        | GitHub user    | Nagoya         | NULL           | NULL           | NULL           | NULL           | NULL           | NULL           | NULL           | NULL            |
20:35:39.197 [run-main-0] DEBUG s.slick.jdbc.StatementInvoker.result - \-----------+-------------+-------------------+------------+--------------+------------+--------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+----------------+-----------------/
List(Map(PERSONAL_ATTR7 -> null, PERSON_BIRTH_DATE -> 1980-01-02, PERSONAL_ATTR2 -> Nagoya, COMPANY_NAME -> Toyota, COUNTRY_ID -> 1, PERSONAL_ATTR3 -> null, PERSONAL_ATTR10 -> null, PERSONAL_ATTR6 -> null, PERSONAL_ATTR1 -> GitHub user, PERSONAL_ATTR5 -> null, PERSONAL_ATTR8 -> null, COUNTRY_NAME -> Japan, PERSONAL_ATTR9 -> null, PERSONAL_ATTR4 -> null, PERSON_ID -> 123, PERSON_NAME -> Taro Yamada, COMPANY_ID -> 2))


----- Anorm -----
SimpleSql(SqlQuery(
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
  p.id = %s
  ,List(_0),None),Map(_0 -> ParameterValue(123)),<function1>)

Stream(Row('ColumnName(.PERSON_ID,Some(PERSON_ID))': 123 as java.lang.Integer, 'ColumnName(.PERSON_NAME,Some(PERSON_NAME))': Taro Yamada as java.lang.String, 'ColumnName(.PERSON_BIRTH_DATE,Some(PERSON_BIRTH_DATE))': 1980-01-02 as java.sql.Date, 'ColumnName(.COMPANY_ID,Some(COMPANY_ID))': 2 as java.lang.Integer, 'ColumnName(.COMPANY_NAME,Some(COMPANY_NAME))': Toyota as java.lang.String, 'ColumnName(.COUNTRY_ID,Some(COUNTRY_ID))': 1 as java.lang.Integer, 'ColumnName(.COUNTRY_NAME,Some(COUNTRY_NAME))': Japan as java.lang.String, 'ColumnName(.PERSONAL_ATTR1,Some(PERSONAL_ATTR1))': GitHub user as java.lang.String, 'ColumnName(.PERSONAL_ATTR2,Some(PERSONAL_ATTR2))': Nagoya as java.lang.String, 'ColumnName(.PERSONAL_ATTR3,Some(PERSONAL_ATTR3))': null as java.lang.String, 'ColumnName(.PERSONAL_ATTR4,Some(PERSONAL_ATTR4))': null as java.lang.String, 'ColumnName(.PERSONAL_ATTR5,Some(PERSONAL_ATTR5))': null as java.lang.String, 'ColumnName(.PERSONAL_ATTR6,Some(PERSONAL_ATTR6))': null as java.lang.String, 'ColumnName(.PERSONAL_ATTR7,Some(PERSONAL_ATTR7))': null as java.lang.String, 'ColumnName(.PERSONAL_ATTR8,Some(PERSONAL_ATTR8))': null as java.lang.String, 'ColumnName(.PERSONAL_ATTR9,Some(PERSONAL_ATTR9))': null as java.lang.String, 'ColumnName(.PERSONAL_ATTR10,Some(PERSONAL_ATTR10))': null as java.lang.String), ?)

[success] Total time: 10 s, completed Mar 1, 2015 8:35:39 PM
```
