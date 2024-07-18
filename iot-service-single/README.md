# iot-service-single 物联网服务单机版

## 版本号

- 父依赖 :
  - `SpringBoot Parent` : `org.springframework.boot:spring-boot-starter-parent`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-starter-parent?label=Maven%20Central)](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-parent)
- 继承父依赖不可修改版本号 :
  - `SpringBoot Web` : `org.springframework.boot:spring-boot-starter-web`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-starter-web?label=Maven%20Central)](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web)
  - `SpringBoot WebSocket` : `org.springframework.boot:spring-boot-starter-websocket`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-starter-websocket?label=Maven%20Central)](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-websocket)
  - `热部署` : `org.springframework.boot:spring-boot-devtools`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-devtools?label=Maven%20Central)](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools)
  - `SpringBoot打包插件` : `org.springframework.boot:spring-boot-maven-plugin`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-maven-plugin?label=Maven%20Central)](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-maven-plugin)
  - `自动装配` : `org.springframework.boot:spring-boot-autoconfigure-processor`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-autoconfigure-processor?label=Maven%20Central)](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-autoconfigure-processor)
  - `配置` : `org.springframework.boot:spring-boot-configuration-processor`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-configuration-processor?label=Maven%20Central)](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-configuration-processor)
- 继承父依赖可修改版本号 :
  - `实体层注解` : `org.projectlombok:lombok`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.projectlombok/lombok?label=Maven%20Central)](https://mvnrepository.com/artifact/org.projectlombok/lombok)
  - `日志` : `ch.qos.logback:logback-classic`  
    [![Maven Central](https://img.shields.io/maven-central/v/ch.qos.logback/logback-classic?label=Maven%20Central)](https://mvnrepository.com/artifact/ch.qos.logback/logback-classic)
  - `Maven编译插件` : `org.apache.maven.plugins:maven-compiler-plugin`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven.plugins/maven-compiler-plugin?label=Maven%20Central)](https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-compiler-plugin)
  - `MySQL` : `mysql:mysql-connector-java`  
    [![Maven Central](https://img.shields.io/maven-central/v/mysql/mysql-connector-java?label=Maven%20Central)](https://mvnrepository.com/artifact/com.mysql/mysql-connector-j)
  - `MongoDB` : `org.springframework.boot:spring-boot-starter-data-mongodb`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.springframework.boot/spring-boot-starter-data-mongodb?label=Maven%20Central)](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-mongodb)
- 父依赖不含 :
  - `FastJson` : `com.alibaba.fastjson2:fastjson2`  
    [![Maven Central](https://img.shields.io/maven-central/v/com.alibaba.fastjson2/fastjson2?label=Maven%20Central)](https://mvnrepository.com/artifact/com.alibaba.fastjson2/fastjson2)
  - `MQTT` : `org.eclipse.paho:org.eclipse.paho.client.mqttv3`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.eclipse.paho/org.eclipse.paho.client.mqttv3?label=Maven%20Central)](https://mvnrepository.com/artifact/org.eclipse.paho/org.eclipse.paho.client.mqttv3)
  - `加密算法` : `org.bouncycastle:bcpkix-jdk18on`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.bouncycastle/bcpkix-jdk18on?label=Maven%20Central)](https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk18on)
  - `MyBatis` : `org.mybatis.spring.boot:mybatis-spring-boot-starter`  
    [![Maven Central](https://img.shields.io/maven-central/v/org.mybatis.spring.boot/mybatis-spring-boot-starter?label=Maven%20Central)](https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter)
  - `PageHelper分页排序查询` : `com.github.pagehelper:pagehelper-spring-boot-starter`  
    [![Maven Central](https://img.shields.io/maven-central/v/com.github.pagehelper/pagehelper-spring-boot-starter?label=Maven%20Central)](https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper-spring-boot-starter)
  - `雪花ID生成器` : `cn.404z:id-spring-boot-autoconfigure`  
    [![Maven Central](https://img.shields.io/maven-central/v/cn.404z/id-spring-boot-autoconfigure?label=Maven%20Central)](https://mvnrepository.com/artifact/cn.404z/id-spring-boot-autoconfigure)
  - `Knife4j` : `com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter`  
    [![Maven Central](https://img.shields.io/maven-central/v/com.github.xiaoymin/knife4j-openapi3-jakarta-spring-boot-starter?label=Maven%20Central)](https://mvnrepository.com/artifact/com.github.xiaoymin/knife4j-openapi3-jakarta-spring-boot-starter)
