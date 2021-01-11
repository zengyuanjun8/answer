# answer
个人学习spring使用

# record

### spring cloud nacos
- spring-cloud-starter-alibaba-nacos-config Version要与Spring Boot Version对应，项目中采用2.2.2版本(https://nacos.io/zh-cn/docs/quick-start-spring-cloud.html)
- 服务提供方和消费方，启动类上添加@EnableDiscoveryClient启用服务发现，配置文件application.properties中添加注册中心地址spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
- 消费方实例化Bean对象RestTemplate并添加@LoadBalanced注解

### spring boot docker
- 添加Maven插件docker-maven-plugin:1.0.0，编写Dockerfile
- 在IDEA Settings->Docker中添加TCP Socket URL(docker服务端不在本地，最好开启TLS)

### spring boot https
- 配置文件application.properties中添加server.ssl.key-store=classpath:*.jks，server.ssl.key-store-type=JKS，server.ssl.key-store-password=*，server.ssl.key-password=*，默认使用https
- 新建ServletWebServerFactory类增加http访问接口，可设置重定向到https端口，见com/zyj/answer/config/ServletConfig.java
