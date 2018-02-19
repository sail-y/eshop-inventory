# eshop-inventory

本项目是亿级流量电商项目之库存服务

## 创建数据库

在数据库中创建一个eshop的database

创建一个eshop/eshop的用户

```sql
create database if not exists eshop;
grant all privileges on eshop.* to 'eshop'@'%' identified by 'eshop';

use eshop;
create table user(name varchar(255), age int);
insert into user values('张三', 25);
```


## 整合Jedis Cluster

新添加依赖

```xml
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

添加配置：

```properties
spring.redis.cluster.nodes=192.168.2.202:7003,192.168.2.202.:7004,192.168.2.203:7006
spring.redis.timeout=5000
```
