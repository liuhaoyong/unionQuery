### 一个通用的联合查询系统, 支持如下特性
1. 可根据定义的查询sql生成查询表单, sql以及查询条件支持线上动态配置和修改 
2. 同一个查询页面中支持关联查询多数据库, 不同数据库的数据,目前支持mysql, sqlserver
3. 支持多sql多数据库表sql间的递归关联查询, 如一个查询表单中配置了3个查询sql, 可用用第一个sql的查询结果中的某个字段作为第二个查询sql的where条件
4. 单sql查询的支持分页

### 查询功能配置步骤
1. 创建数据源


2. 创建查询业务

3. 创建查询参数

4. 新建sql

5. 查询

