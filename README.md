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


## 缓存与数据库双写一致性保障方案

**数据库与缓存更新与读取操作进行异步串行化。**更新数据的时候，根据数据的唯一标识（例如hash值取模），
将操作路由之后，发送到一个jvm内部的队列中。读取数据的时候，如果发现数据不在缓存中，
那么将重新读取数据+更新缓存的操作，根据唯一标识路由之后，也发送同一个jvm内部的队列中。
一个队列对应一个工作线程，每个工作线程串行拿到对应的操作，然后一条一条的执行。

对应代码： 

* `Request.java`
* `RequestProcessorThreadPool.java`：线程池
* `RequestProcessorThread.java`：维护了一个内存队列的线程
* `RequestQueue.java`：内存队列，里面是各种Request
* `ProductInventoryDBUpdateRequest.java`：删除缓存，修改数据库
* `ProductInventoryCacheRefreshRequest.java`：刷新缓存
* `RequestAsyncProcessServiceImpl.java`：路由请求和异步执行请求
* `ProductInventoryController.java`：如果请求还在等待时间范围内，不断轮询发现可以取到值了，那么就直接返回; 如果请求等待的时间超过一定时长，那么这一次直接从数据库中读取当前的旧值