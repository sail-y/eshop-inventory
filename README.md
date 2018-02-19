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


## 实时数据：缓存与数据库双写一致性保障方案

```mysql
use eshop;
create table product_inventory(product_id int, inventory_cnt int);
INSERT INTO product_inventory values(1, 100);
```


**数据库与缓存更新与读取操作进行异步串行化。** 

更新数据的时候，根据数据的唯一标识（例如hash值取模），
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


模拟操作：


*（1）一个更新商品库存的请求过来，然后此时会先删除redis中的缓存，然后模拟卡顿5秒钟
*（2）在这个卡顿的5秒钟内，我们发送一个商品缓存的读请求，因为此时redis中没有缓存，就会来请求将数据库中最新的数据刷新到缓存中
*（3）此时读请求会路由到同一个内存队列中，阻塞住，不会执行
*（4）等5秒钟过后，写请求完成了数据库的更新之后，读请求才会执行
*（5）读请求执行的时候，会将最新的库存从数据库中查询出来，然后更新到缓存中



> 数据库中数据不存在的还需要优化