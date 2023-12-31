"# food-social-contact" 
# 美食社交APP后台

## 1、应用介绍

美食社交APP后台API接口设计，涉及APP中用户、好友、订单为基础的相关业务，分为用户、优惠券、好友、Feed、订单五个微服务。完成用户登录、交友、发朋友圈以及购买优惠券这个业务流程。

![在这里插入图片描述](./img/img.png)

## 2、应用总结

截至目前我们已经开发的功能如下：

| 业务场景     | 数据类型        | 操作指令                           | 作用                                 |
| ------------ | --------------- | ---------------------------------- | ------------------------------------ |
| 单点登录     | String          | SET、GET                           | 存储 Token 与登录食客信息            |
| 抢购代金券   | Hash、Lua       | HGET、HSET、HINCRBY                | 防止超卖、限购(分布式锁)             |
| 好友功能     | Set             | SADD、SMEMBERS、SINTER             | 存储关注集合、粉丝集合、共同关注列表 |
| Feed功能     | Sorted Set      | ZADD、ZREVRANGE                    | 关注的好友的 Feed 流集合             |
| 签到功能     | Bitmap、String  | SETBIT、GETBIT、BITCOUNT、BITFIELD | 位图存储食客签到信息                 |
| 积分排行榜   | Sorted Set      | ZINCRBY、ZREVRANK、ZREVRANGE       | 存储食客总积分集合方便排序           |
| 附近的人     | Geo、Sorted Set | GEOADD、GEOREDIUS                  | 存储与查询食客地理位置信息           |
| 餐厅缓存     | Hash            | HSET、HGETALL、HINCRBY             | 存储餐厅热点数据                     |
| 最新餐厅评论 | List            | LPUSH、LRANGE                      | 存储最新餐厅评论                     |



### 2.1、单点登录

我们使用 Spring Security 和 OAuth2 实现了授权认证中心及单点登录的功能。

这个功能中 Redis 主要用于存储 Token 令牌信息，使用了 String 数据类型。

### 2.2、抢购优惠券

这个功能中我们实现了抢购秒杀完整的一套业务，解决了超卖、限制一人一单的问题。

这个功能中 Redis 主要用于实现分布式锁、Lua脚本，使用了 Hash 数据类型，利用了原生的方式和 Redisson 的方式。

### 2.3、好友功能

这个功能中我们实现了关注、取关、获取共同关注列表功能。

这个功能中 Redis 主要用于存储关注列表和粉丝列表中的相关用户信息，使用了 Set 数据类型。

### 2.4、Feed功能

这个功能中我们实现了添加 Feed、删除 Feed、关注取关时变更 Feed、查询 Feed 功能。

这个功能中 Redis 主要用于存储每个用户关注好友添加的 Feed 流集合，使用了 Sorted Set 数据类型。

### 2.5、签到功能

这个功能中我们实现了签到、补签、获取连续签到次数、获取签到总次数、获取签到详情功能。

这个功能中 Redis 主要用于存储签到信息，使用了 Bitmap 数据类型。

### 2.6、积分功能

这个功能中我们实现了添加积分、获取积分排行榜功能。

这个功能中 Redis 主要用于存储积分信息，使用了 Sorted Set 数据类型。

### 1.7、附近的人

这个功能中我们实现了上传用户坐标、获取附近的人功能。

这个功能中 Redis 主要用于存储地理位置信息，使用了 GEO 数据类型。

### 2.8、餐厅缓存

这个功能中我们实现了餐厅热点数据缓存、查询餐厅缓存功能。

这个功能中 Redis 主要用于存储餐厅信息，使用了 Hash 数据类型。

### 2.9、最新餐厅评论

这个功能中我们实现了添加餐厅评论、获取餐厅评论功能。

这个功能中 Redis 主要用于存储餐厅评论信息，使用了 List 数据类型。


