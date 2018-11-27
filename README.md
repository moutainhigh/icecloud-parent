# icecloud-parent

#### 项目介绍
spring cloud 全家桶项目
#### 软件架构
````
│-com.icetech icecloud-parent	    --------------------	父及项目依赖
│	│-com.icetech icecloud-config 	    ----------------	配置中心
│	│-com.icetech icecloud-control	    ----------------	注册中心
│	│-com.icetech icecloud-authcenter	    ------------	鉴权认证中心
│	│-com.icetech icecloud-common 	    ----------------	common模块
│	│-com.icetech icecloud-gateway 	    ----------------	网关服务
│	│-com.icetech icecloud-monitor 	    ----------------	熔断监控
│	│-com.icetech icecloud-zipkin 	    ----------------	服务监控
│   │-com.icetech icecloud-job      --------------------    任务调度中心
│	│-com.icetech icecloud-provider     ----------------	服务提供方
│	│	│-com.icetech icecloud-paycenter    ------------	支付中心
│	│	│-com.icetech icecloud-datacenter   	--------	数据中心
│	│	│-com.icetech icecloud-cloudcenter 	    --------	云中心
│	│	│-com.icetech icecloud-tool     ----------------	对外对接api中心
│	│	│-com.icetech icecloud-web 	    ----------------	web
│	│	│-com.icetech icecloud-taskcenter --------------	任务管理
│	│-com.icetech icecloud-provider-api     ------------	对外服务暴露
│	│	│-com.icetech icecloud-paycenter-api    --------	支付中心
│	│	│-com.icetech icecloud-datacenter-api 	--------	数据中心
│	│	│-com.icetech icecloud-cloudcenter-api 	--------	cloud中心
│	│	│-com.icetech icecloud-tool-api ----------------	api
│	│	│-com.icetech icecloud-web-api 	----------------	web中心
│	│	│-com.icetech icecloud-taskcenter-api 	--------	任务

````
#### 软件架构
![](https://i.loli.net/2018/11/06/5be125c737b2c.png)
#### 技术要点
spring cloud Finchley 版本+spring boot 2.0.2

支付中心: 聚合支付平台,支付宝支付,微信支付,银联无感支付

数据中心: mq+mqtt mq消息+物联网消息推送

任务中心: xxl任务调度平台

存储: 阿里云oss

消息: 阿里云


