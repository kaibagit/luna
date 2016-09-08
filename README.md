# luna

## 应用类型
1. 最简单的应用，基本是对于数据库的CRUD，占90%以上，很少用到事务
2. 略微复杂点的应用，大部分CRUD，25%左右的业务逻辑，需要事务支持
3. 企业级应用，75%含有业务逻辑，25%以下的CRUD，大量用到事务
4. 互联网应用，需要服务化

## 解决方案
1. Controller + Spring事务注解 + Ebean
2. Controller + 部分Biz层 + Spring事务注解 + Ebean
3. DDD
4. RPC

## 技术选型
### 后端
* 核心框架：Spring Framework
* MVC框架：Spring MVC
* 持久层框架：Ebean
### 前端
* JS框架：jQuery
* CSS框架：Bootstrap AdminLTE