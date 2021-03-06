# 基于Spring Boot 2、Dubbo、Redis、Mybtis搭建分布式开发项目

## 1. 概述
本文讲述从零开始搭建一个分布式开发项目的过程。包含以下内容：

- Spring Boot 2.0.3
- Dubbo
- Redis
- Mybatis及分页插件PageHelper
- Interceptor
- 自定义注解和AOP（包括日志记录和权限管理）
- Solr的部署和开发
- Elasticsearch的部署和开发
- Tesseract ocr的安装和开发

项目可以手工或利用Jenkins等自动化工具进行部署。在用Maven对各模块进行编译时，在命令行最后加上`-P 参数`，可在编译时引入不同的Spring Boot配置文件，从而对各类环境进行区分，参数包括开发环境`dev`和生产环境`prod`，不加参数默认`dev`。

文中所用的IDE为Intellij IDEA，JDK版本为1.8。



## 2. 步骤
### 2.1 创建工程

菜单-File-New-Project，选择Spring Initializr

```
Project SDK: JDK1.8
Initializr Service Url: 默认 https://start.spring.io
```

点Next，进入Project Metadata界面，填写信息

```
Group: com.tyrival
Arfifact: springboot-dubbo-sample
Type: Maven Project

其余项目保持自动生成的值
```

点Next，进入Dependencies，由于工程本身不开发任何代码，只用于管理maven信息，从而提供给各模块进行继承，所以不选择任何依赖包，直接点Next，然后填写信息

```
Project name: springboot-dubbo-sample  // 与Project Metadata保持一致

其余项目保持自动生成的值
```

点击Finish，工程创建成功，如果默认生成了src、.mvn、mvnw、mvnw.cmd等文件，将其全部删除。



### 2.2 创建模块

菜单-File-New-Module，选择Spring Initializr

```
Project SDK: JDK1.8
Initializr Service Url: 默认 https://start.spring.io
```

点Next，进入Project Metadata界面，填写信息

```
Group: com.tyrival
Arfifact: common
Type: Maven Project

其余项目保持自动生成的值
```

点Next，进入Dependencies，不选择任何依赖包，直接点Next，然后填写信息

```
Module name: common  // 与Project Metadata保持一致

其余项目保持自动生成的值
```

点击Finish，模块创建完成，并用相同的方式创建controller、redis、user模块。



### 2.3 pom.xml

#### 2.3.1 项目的根pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.tyrival</groupId>
    <artifactId>springboot-dubbo-sample</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <!-- 必须修改为pom -->
    <packaging>pom</packaging>

    <name>springboot-dubbo-sample</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.3.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <!-- spring boot2.0的log4j依赖不全，排除，否则会报错 -->
            <exclusions>
                <exclusion>
                    <groupId>org.apache.logging.log4j</groupId>
                    <artifactId>log4j-to-slf4j</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <!-- spring boot2.0的log4j依赖不全，排除，否则会报错 -->
            <exclusions>
                <exclusion>
                    <groupId>org.apache.logging.log4j</groupId>
                    <artifactId>log4j-to-slf4j</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Spring Boot Dubbo 依赖 -->
        <dependency>
            <groupId>io.dubbo.springboot</groupId>
            <artifactId>spring-boot-starter-dubbo</artifactId>
            <exclusions>
                <!-- 排除javassist-3.15.0-GA并升级，否则用lambda，会报invalid constant type: 18 -->
                <exclusion>
                    <groupId>org.javassist</groupId>
                    <artifactId>javassist</artifactId>
                </exclusion>
            </exclusions>
            <version>1.0.0</version>
        </dependency>

        <!-- Javassist -->
        <dependency>
            <groupId>org.javassist</groupId>
            <artifactId>javassist</artifactId>
            <version>3.18.2-GA</version>
        </dependency>

        <!-- common -->
        <dependency>
            <groupId>com.tyrival</groupId>
            <artifactId>common</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        <!-- AOP -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

        <!-- guava -->
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>23.3-jre</version>
        </dependency>

        <!-- log4j -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.25</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>log4j-over-slf4j</artifactId>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
        </dependency>

    </dependencies>

    <!-- 引入各模块 -->
    <modules>
        <module>user</module>
        <module>redis</module>
        <module>controller</module>
        <module>common</module>
    </modules>

    <!-- 管理Maven编译参数 -->
    <!-- 在编译时，通过增加参数" -P 环境参数"，会根据参数加载不同配置 -->
    <!-- 例如："-P dev"表示调用开发环境配置 -->
    <profiles>
        <profile>
            <id>dev</id>
            <properties>
                <profileActive>dev</profileActive>
            </properties>
            <!-- 不加任何参数时，默认调用dev环境配置 -->
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
        <profile>
            <id>prod</id>
            <properties>
                <profileActive>prod</profileActive>
            </properties>
        </profile>
    </profiles>

    <!-- Maven编译配置 -->
    <build>
        <!-- 配置资源文件 -->
        <resources>
            <resource>
                <!-- 配置资源目录 -->
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
                <!-- 排除所有资源文件 -->
                <excludes>
                    <exclude>application.properties</exclude>
                    <exclude>application-dev.properties</exclude>
                    <exclude>application-prod.properties</exclude>
                </excludes>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
                <!-- 标识构建时所需要的配置文件 -->
                <includes>
                    <include>application.properties</include>
                    <!-- ${profileActive}这个值会在maven构建时传入 -->
                    <include>application-${profileActive}.properties</include>
                </includes>
            </resource>
        </resources>

        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>org.codehaus.cargo</groupId>
                <artifactId>cargo-maven2-plugin</artifactId>
                <version>1.6.5</version>
                <configuration>
                    <container>
                        <!-- 指明使用的tomcat服务器版本 -->
                        <containerId>tomcat8x</containerId>
                        <type>remote</type>
                    </container>
                    <configuration>
                        <type>runtime</type>
                        <cargo.remote.username>admin</cargo.remote.username>
                        <cargo.remote.password>password</cargo.remote.password>
                    </configuration>
                </configuration>
                <executions>
                    <execution>
                        <phase>deploy</phase>
                        <goals>
                            <goal>redeploy</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <!-- 添加插件maven-resources-plugin，maven构建时替换参数 -->
            <plugin>
                <artifactId>maven-resources-plugin</artifactId>
                <version>3.0.2</version>
                <configuration>
                    <delimiters>
                        <delimiter>@</delimiter>
                    </delimiters>
                    <useDefaultDelimiters>false</useDefaultDelimiters>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```



#### 2.3.2 common模块的pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.tyrival</groupId>
    <artifactId>common</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <!-- common模块作为所有模块的依赖，编译为jar -->
    <packaging>jar</packaging>

    <name>common</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.1</version>
        </dependency>
    </dependencies>

</project>
```



#### 2.3.3 controller模块的pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.tyrival</groupId>
    <artifactId>controller</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <!-- 修改为war -->
    <packaging>war</packaging>

    <name>controller</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>com.tyrival</groupId>
        <artifactId>springboot-dubbo-sample</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

</project>
```



#### 2.3.4 user模块的pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.tyrival</groupId>
    <artifactId>user</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <!-- 修改为war -->
    <packaging>war</packaging>

    <name>user</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>com.tyrival</groupId>
        <artifactId>springboot-dubbo-sample</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

    <dependencies>

        <!-- MyBatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.1</version>
        </dependency>

        <!-- PageHelper -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.2.3</version>
        </dependency>

        <!-- Mysql -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- PostgreSQL -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

    </dependencies>

</project>
```



#### 2.3.5 redis模块的pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.tyrival</groupId>
    <artifactId>redis</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <!-- 修改为war -->
    <packaging>war</packaging>

    <name>redis</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>com.tyrival</groupId>
        <artifactId>springboot-dubbo-sample</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>


    <dependencies>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- spring2.0 集成 redis 所需 common-pool2 -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
            <version>2.5.0</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.40</version>
        </dependency>

    </dependencies>

</project>
```



### 2.4 子模块

#### 2.4.1 common

common作为其他所有模块的依赖，主要功能是声明各模块通用的Entity、Exception、Enumeration和Service接口。其中的SpringBootApplication和配置文件无需修改



#### 2.4.2 user

user是一个以用户管理为示例开发的模块，主要负责向外提供UserService服务的实现，并与数据库进行用户信息的交换，下面列举部分主要源码。

- com.tyrival.user.UserApplication

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class UserApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(UserApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
```



- com.tyrival.user.service.UserServiceImpl

```java
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tyrival.entity.base.PageResult;
import com.tyrival.entity.param.QueryParam;
import com.tyrival.entity.user.User;
import com.tyrival.common.user.UserService;
import com.tyrival.user.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

// dubbo的服务注解，将此服务注册到dubbo注册中心，不可遗漏
@Service
// spring的服务注解，用于依赖注入
@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User create(User user) {
        user.setId(UUID.randomUUID().toString());
        int i = userDAO.create(user);
        return i == 1 ? user : null;
    }

    @Override
    public List<User> list(QueryParam queryParam) {
        return userDAO.find(queryParam);
    }

    @Override
    public PageResult listByPage(QueryParam queryParam) {
        PageInfo pageInfo = PageHelper.startPage(1, 10)
                .doSelectPageInfo(() -> userDAO.find(queryParam));
        long totalCount = pageInfo.getTotal();
        queryParam.getPage().setTotalCount(totalCount);
        PageResult result = new PageResult(pageInfo.getList(), queryParam.getPage());
        return result;
    }
}

```



- src/main/resources/application.properties

```properties
## 通用配置文件，任何环境都要用到的配置在此处

# 接收mavan构建时的-P参数，用于引入不同环境的配置文件
spring.profiles.active=@profileActive@

# mybatis配置文件引入
mybatis.config-locations=classpath:mybatis/mybatis-config.xml
mybatis.mapper-locations=classpath:mybatis/mapper/*.xml
```



- src/main/resources/application-dev.properties

```properties
## dev开发环境下特有的配置记录在此

# logging配置
logging.level.root=info
logging.file=tyrival-log/user-log.log

# 数据源
spring.datasource.driverClassName = org.postgresql.Driver
spring.datasource.url = jdbc:postgresql://192.168.0.179:5432/postgres?useUnicode=true&characterEncoding=utf-8
spring.datasource.username = postgres
spring.datasource.password = 123

# Dubbo 服务注册中心
spring.dubbo.application.name=user-provider
spring.dubbo.registry.address=zookeeper://192.168.0.179:2181
spring.dubbo.protocol.name=dubbo
spring.dubbo.protocol.port=20880
# 扫描包内的所有dubbo的@Service注解，将其发布为RPC服务
spring.dubbo.scan=com.tyrival.user.service
```



#### 2.4.3 controller

controller模块主要负责调用user模块发布的RPC服务，然后通过http协议对外提供服务。类似user模块，具体参考源码。



#### 2.4.4 redis

redis模块主要负责与redis服务器进行数据交互。类似user模块，具体参考源码。



## 3. 拦截器Interceptor

拦截器建立在controller模块中，此处以Token拦截器为例，讲解拦截器的配置方式，在调用接口时，拦截器比后面所说的AOP先发生作用。

### 3.1 依赖包

拦截器依赖于spring-boot-starter-web，由于项目的根pom.xml已经引入，而controller的pom.xml继承自根pom.xml，所以无需重复引入

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <!-- spring boot2.0的log4j依赖不全，排除，否则会报错 -->
    <exclusions>
        <exclusion>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-to-slf4j</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```



### 3.2 TokenInterceptor

需要注意的是，此处拦截器所在的包为`com.tyrival.controller.interceptor`，而不是`com.tyrival.interceptor`，后者在模块启动时会扫描不到。

```java
package com.tyrival.controller.interceptor;

import com.tyrival.entity.user.User;
import com.tyrival.enums.base.RequestAttrEnum;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        // 接受跨域访问
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, Authorization, Origin, Content-Type, Accept, token, apikey");

        String token = httpServletRequest.getParameter("token");
        User user = new User();

        // TODO 解析TOKEN，验证有效性，将得到的用户信息赋予user，并注入请求

        httpServletRequest.setAttribute(RequestAttrEnum.USER.getCode(), user);

        String newToken = "";

        // TODO 生成新TOKEN

        httpServletResponse.setHeader("token", newToken);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}
```



### 3.3 配置文件InterceptorConfig

拦截器配置文件所处的包与拦截器相似，必须在controller之下建文件夹，否则也会扫描不到。

```java
package com.tyrival.controller.config;

import com.tyrival.controller.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	// 将拦截器加入序列，并增加URI匹配
        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");
    }
}
```



## 4. 自定义注解和AOP

此处所有的注解都建立在controller模块中，与拦截器相同的是，此处的包都需要建立在`com.tyrival.controller`之下，否则SpringBoot也会扫描不到。

### 4.1 依赖包

aop依赖于spring-boot-starter-aop，由于项目的根pom.xml已经引入，而controller的pom.xml继承自根pom.xml，所以无需重复引入

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```



### 4.2 日志

#### 4.2.1 Log注解

```java
package com.tyrival.controller.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String value();
}
```



#### 4.2.2 AOP

```java
package com.tyrival.controller.aspect;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.tyrival.controller.annotation.Log;
import com.tyrival.entity.user.User;
import com.tyrival.enums.base.RequestAttrEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class LogAspect {

    // 设置切面为user包下所有类的所有方法
    @Pointcut("execution(public * com.tyrival.controller.user.*.*(..))")
    public void log() {
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取请求中由Token拦截器注入的用户信息
        User user = (User) request.getAttribute(RequestAttrEnum.USER.getCode());
        if (user == null || StringUtils.isBlank(user.getId())) {
            // TODO 记录为游客
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 类名
        String className = signature.getDeclaringTypeName();

        // 方法名
        Method method = signature.getMethod();
        String methodName = method.getName();

        // 参数
        Object[] arguments = joinPoint.getArgs();

        // 参数名称
        String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();

        Object result = joinPoint.proceed();

        // 如存在Log注解，需要记录日志
        if (method.isAnnotationPresent(Log.class)) {
            Log annotation = method.getAnnotation(Log.class);
            // 获取Log注解内的参数
            String type = annotation.value();

            // TODO 记录日志
            System.out.println("记录日志......");
        }
        return result;
    }
}
```



### 4.3 权限

#### 4.3.1 Permission注解

```java
package com.tyrival.controller.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    
}
```



#### 4.3.2 AOP

```java
package com.tyrival.controller.aspect;

import com.tyrival.controller.annotation.Permission;
import com.tyrival.entity.user.User;
import com.tyrival.enums.base.RequestAttrEnum;
import com.tyrival.exception.CommonException;
import com.tyrival.exception.ExceptionEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class PermissionAspect {

    @Pointcut("execution(public * com.tyrival.controller.user.*.*(..))")
    public void permission(){}

    @Around("permission()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取请求中由Token拦截器注入的用户信息
        User user = (User) request.getAttribute(RequestAttrEnum.USER.getCode());

        // 类名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();

        // 方法名
        Method method = signature.getMethod();
        String methodName = method.getName();

        // 包含Permission注解时，进行权限验证
        if (method.isAnnotationPresent(Permission.class)
                || !hasPermission(user, className, methodName)) {
            throw new CommonException(ExceptionEnum.NO_PERMISSION);
        }
        return joinPoint.proceed();
    }

    private boolean hasPermission(User user, String className, String methodName) {

        // TODO 根据用户信息、类名、方法名，查询用户是否有该权限

        return true;
    }
}
```



### 4.4  示例

```java
@RestController
public class UserControllerImpl implements UserController {
    
    // 记录为insert类日志，@Permission表示需要验证权限
    @Override
    @Log("insert")
    @Permission
    public Result create(HttpServletRequest request, HttpServletResponse response, User user) {
        user = userService.create(user);
        return new Result(user);
    }

    // 记录为query类日志，无@Permission注解表示无需验证权限
    @Override
    @Log("query")
    public Result list(HttpServletRequest request, HttpServletResponse response, QueryParam queryParam) {
        List<User> list = userService.list(queryParam);
        return new Result(list);
    }
    
    // 无需记录日志，无需验证权限
    @Override
    public Result listByPage(HttpServletRequest httpReq, HttpServletResponse httpRsp, QueryParam queryParam) {
        PageResult result = userService.listByPage(queryParam);
        return new Result(result);
    }
}
```



## 5. Solr

### 5.1 准备
- 下载工程solr-7.4.0.zip（不带src的），解压缩。下载地址：http://lucene.apache.org/solr/
- 创建目录`~/Documents/Workspace/Docker/Solr`，用来存储solr的所有cores
- 将工程`solr-7.4.0/server/solr`下所有文件，复制到刚创建的`~/Documents/Workspace/Docker/Solr`中

```bash
# 下载镜像solr:7.4.0，此版本需要和之前下载的工程版本保持一致
$ docker pull solr

# 已经下载过的镜像，可以通过以下代码查看版本
# Config.ENV = [...SOLR_VERSION=7.4.0...]
$ docker image inspect solr 
```



### 5.2 容器部署

```bash
# --name 命名容器名称
# -v 将本地的文件夹挂载到容器内的文件夹，使solr的所有cores储存到本地目录
# -p 将本地端口映射到容器端口
$ docker run --privileged=true --name solr  -v ~/Documents/Workspace/Docker/Solr:/opt/solr/server/solr -d -p 8983:8983 -t solr
```



### 5.3 创建core

```bash
# 在name=solr的容器中，创建core=tyrival
$ docker exec -it --user=solr solr bin/solr create_core -c tyrival

# 此时在`~/Documents/Workspace/Docker/Solr`中可以看到多了tyrival文件夹，即新建的core
# 在浏览器中访问http://localhost:8983/solr，会显示Solr Admin界面
# 在左侧菜单的Core Selector中选择tyrival，可以看到core=tyrival的信息
```



### 5.4 配置

- 下载postgres的驱动，此处用postgresql-42.2.2.jar，复制到`~/Documents/Workspace/Docker/Solr/lib`中
- 将工程`solr-7.4.0/example/example-DIH/solr/db/conf/db-data-config.xml`，复制到`~/Documents/Workspace/Docker/Solr/conf`中，并编辑此文件
```xml
<dataConfig>
    <!-- 数据库驱动，地址，用户名，密码 -->
    <dataSource type="JdbcDataSource"
                driver="org.postgresql.Driver"
                url="jdbc:postgresql://192.168.0.179:5432/postgres"
                user="postgres"
                password="123"/>

    <document>
        <!-- 索引country表 -->
        <entity name="country" query="select * from COUNTRY" />
    </document>
</dataConfig>
```


- 在数据库建表

```sql
create table country
(
  id        integer     not null  constraint country_pkey primary key,
  name      varchar(50) not null,
  name_en   varchar(100),
  continent varchar(50)
);

comment on table country
is '国家表';

create unique index country_id_uindex
  on country (id);

insert into country (id, name, name_en, continent) values (1, '中华人民共和国', 'Republic of China', '亚洲')
insert into country (id, name, name_en, continent) values (2, '大不列颠及北爱尔兰联合王国', 'United Kingdom of Great Britain and Northern Ireland', '欧洲')
insert into country (id, name, name_en, continent) values (3, '德意志联邦共和国', 'Federal Republic of Germany', '欧洲')
insert into country (id, name, name_en, continent) values (4, '朝鲜民主主义人民共和国', 'Democratic People''s Republic of Ko-rea', '亚洲')
insert into country (id, name, name_en, continent) values (5, '法兰西共和国', 'French Republic', '欧洲')
```



- 编辑`~/Documents/Workspace/Docker/Solr/conf/solrconfig.xml`，设置DataImportHandler
```xml
<!-- 如果出现问题，就尝试把/dataimport写在/select之前 -->
<requestHandler name="/dataimport" class="solr.DataImportHandler">
<lst name="defaults">
  <str name="config">db-data-config.xml</str>
</lst>
</requestHandler>

<requestHandler name="/select" class="solr.SearchHandler">
    <lst name="defaults">
      <str name="echoParams">explicit</str>
      <int name="rows">10</int>
```


- 重启Solr
```bash
$ docker container stop [solr container id]
$ docker container start [solr container id]
```


- 浏览器打开`http://localhost:8983/solr`，进入Solr Admin，左侧菜单Core Selector选择tyrival，然后选择Schema功能
- 点击右侧页面左上方的Add Field按钮，添加country表的name列

```
name: name
## 此处需要注意，如果选择text_general等类型，会导致后面查询结果时，name的值为数组，而不是字符串
## 会造成查询结果解析时报BindingException异常
field type: string
default: ''
stored: 选中
indexed: 选中
```

同样的方法添加name_en和continent列，然后可以在`~/Documents/Workspace/Docker/Solr/conf/managed_schema`中看到如下内容：

```xml
<field name="continent" type="string" indexed="true" stored="true"/>
<field name="id" type="string" multiValued="false" indexed="true" required="true" stored="true"/>
<field name="name" type="string" indexed="true" stored="true"/>
<field name="name_en" type="string" indexed="true" stored="true"/>
```



- 此处如果主键名不是id，需要进行以下修改

```xml
<!-- 将id修改为主键名 -->
<uniqueKey>id</uniqueKey>

<!-- 改为required="false" -->
<field name="id" type="string" multiValued="false" indexed="true" required="true" stored="true"/>
```



- 重启Solr
- 刷新Solr Admin页面，左侧选择core=tyrival，选DataImport菜单，进入数据导入页面
- 在数据导入页选全部导入Command=full-import，选择Entity=country，点击Execute，刷新页面，直到右侧出现绿色提示后，表示导入成功

```
Indexing completed. Added/Updated: 5 documents. Deleted 0 documents.
Requests: 1 , Fetched: 5 , Skipped: 0 , Processed: 5 
Started: less than a minute ago
```



- 点击左侧菜单Query，进入查询页面，输入q=name_en:republic，点击Execute Query进行查询，可以查到相应的记录。

```json
{
  "responseHeader":{
    "status":0,
    "QTime":49,
    "params":{
      "q":"name:*共和国*",
      "_":"1530684926303"}},
  "response":{"numFound":4,"start":0,"docs":[
      {
        "continent":"亚洲",
        "name":"中华人民共和国",
        "id":"1",
        "name_en":"Republic of China",
        "_version_":1605039455213715456},
      {
        "continent":"欧洲",
        "name":"德意志联邦共和国",
        "id":"3",
        "name_en":"Federal Republic of Germany",
        "_version_":1605039455262998528},
      {
        "continent":"亚洲",
        "name":"朝鲜民主主义人民共和国",
        "id":"4",
        "name_en":"Democratic People's Republic of Ko-rea",
        "_version_":1605039455264047104},
      {
        "continent":"欧洲",
        "name":"法兰西共和国",
        "id":"5",
        "name_en":"French Republic",
        "_version_":1605039455265095680}]
  }}
```



### 5.5 开发

当solr部署完成后，可以进行二次开发，实现远程调用Solr应用，实现业务功能和solr引擎解耦，开发过程和user模块相似。

#### 5.5.1 新建模块

新建solr模块，并在根pom.xml中增加solr模块

```xml
<modules>
	...
	<module>solr</module>
</modules>
```



#### 5.5.2 pom.xml

参考user模块的pom.xml修改，继承根pom.xml，并引入spring-boot-starter-data-solr

```xml
<dependencies>
    <!-- solr -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-solr</artifactId>
    </dependency>
</dependencies>
```



#### 5.5.3 application.properties

配置文件中增加solr应用的访问地址，最后加上core名称tyrival，工程中实际配置在开发环境配置文件`application-dev.properties`中

```properties
## solr 服务器
spring.data.solr.host=http://127.0.0.1:8983/solr/tyrival
```



#### 5.5.4 Country.java

在common模块中创建实体时，必须加注解

- 类加@SolrDocument注解，solrCoreName的值为core名称，即tyrival
- 属性加@Field注解，如果表字段名与属性名不同，在@Field内标注表名
- 主键属性加@Id注解

```java
package com.tyrival.entity.country;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.io.Serializable;

@SolrDocument(solrCoreName = "tyrival")
public class Country implements Serializable {

    /**
     * id
     */
    @Id
    @Field("id")
    private String id;

    /**
     * 名称
     */
    @Field("name")
    @Indexed
    private String name;

    /**
     * 英文名
     */
    @Field("name_en")
    @Indexed
    private String nameEn;

    /**
     * 所在州
     */
    @Field("continent")
    @Indexed
    private String continent;
    
    /* getter & setter */
}
```



#### 5.5.5 CountryServiceImpl.java

- 记得类要加@com.alibaba.dubbo.config.annotation.Service和@org.springframework.stereotype.Service注解
- SolrClient会被框架自动示例化注入

```java
@Service
@org.springframework.stereotype.Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private SolrClient client;

    @Override
    public List<Country> listByPage(QueryParam queryParam) {
        SolrQuery query = new SolrQuery();
        int pageIndex = queryParam.getPage().getPageIndex();
        int pageSize = queryParam.getPage().getPageSize();
        // 这里设置的是从第几条数据开始，而不是从第几页开始
        query.setStart((pageIndex - 1) * pageSize);
        query.setRows(pageSize);
        StringBuffer buffer = new StringBuffer();
        Object name = queryParam.getConditions().get("name");
        if (name != null && StringUtils.isNotBlank(name.toString())) {
        	// 此处需要在关键字两边加*号
            buffer.append("name:").append("*").append(name.toString()).append("*");
        } else {
            buffer.append("*:*");
        }
        query.set("q", buffer.toString());
        QueryResponse response = null;
        try {
            response = client.query(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 如果之前在solr配置schema时，选择的type与Country类的属性类型不匹配的话
        // 这里会报BindingException异常，因为查询出的属性值会是一个数组，而不是一个基本类型
        List<Country> list = response.getBeans(Country.class);
        return list;
    }
}
```



## 6. Elasticsearch

### 6.1 准备

- 创建目录`~/Documents/Workspace/Docker/Elastic`，其中创建config、data、plugins三个文件夹，分别用来储存配置文件、数据、插件
- 在config文件夹中创建配置文件elasticsearch.yml，只需要配置一个参数

```yml
network.host: 0.0.0.0
```



- elasticsearch.yml基本配置参数

```yml
cluster.name: elasticsearch
配置es的集群名称，默认是elasticsearch，es会自动发现在同一网段下的es，如果在同一网段下有多个集群，就可以用这个属性来区分不同的集群。
node.name: "Franz Kafka"
节点名，默认随机指定一个name列表中名字，该列表在es的jar包中config文件夹里name.txt文件中，其中有很多作者添加的有趣名字。
node.master: true
指定该节点是否有资格被选举成为node，默认是true，es是默认集群中的第一台机器为master，如果这台机挂了就会重新选举master。
node.data: true
指定该节点是否存储索引数据，默认为true。
index.number_of_shards: 5
设置默认索引分片个数，默认为5片。
index.number_of_replicas: 1
设置默认索引副本个数，默认为1个副本。
path.conf: /path/to/conf
设置配置文件的存储路径，默认是es根目录下的config文件夹。
path.data: /path/to/data
设置索引数据的存储路径，默认是es根目录下的data文件夹，可以设置多个存储路径，用逗号隔开，例：
path.data: /path/to/data1,/path/to/data2
path.work: /path/to/work
设置临时文件的存储路径，默认是es根目录下的work文件夹。
path.logs: /path/to/logs
设置日志文件的存储路径，默认是es根目录下的logs文件夹
path.plugins: /path/to/plugins
设置插件的存放路径，默认是es根目录下的plugins文件夹
bootstrap.mlockall: true
设置为true来锁住内存。因为当jvm开始swapping时es的效率会降低，所以要保证它不swap，可以把ES_MIN_MEM和 ES_MAX_MEM两个环境变量设置成同一个值，并且保证机器有足够的内存分配给es。同时也要允许elasticsearch的进程可以锁住内存，linux下可以通过`ulimit -l unlimited`命令。
network.bind_host: 192.168.0.1
设置绑定的ip地址，可以是ipv4或ipv6的，默认为0.0.0.0。 
network.publish_host: 192.168.0.1
设置其它节点和该节点交互的ip地址，如果不设置它会自动判断，值必须是个真实的ip地址。
network.host: 192.168.0.1
这个参数是用来同时设置bind_host和publish_host上面两个参数。
transport.tcp.port: 9300
设置节点间交互的tcp端口，默认是9300。
transport.tcp.compress: true
设置是否压缩tcp传输时的数据，默认为false，不压缩。
http.port: 9200
设置对外服务的http端口，默认为9200。
http.max_content_length: 100mb
设置内容的最大容量，默认100mb
http.enabled: false
是否使用http协议对外提供服务，默认为true，开启。
gateway.type: local
gateway的类型，默认为local即为本地文件系统，可以设置为本地文件系统，分布式文件系统，Hadoop的HDFS，和amazon的s3服务器。
gateway.recover_after_nodes: 1
设置集群中N个节点启动时进行数据恢复，默认为1。
gateway.recover_after_time: 5m
设置初始化数据恢复进程的超时时间，默认是5分钟。
gateway.expected_nodes: 2
设置这个集群中节点的数量，默认为2，一旦这N个节点启动，就会立即进行数据恢复。
cluster.routing.allocation.node_initial_primaries_recoveries: 4
初始化数据恢复时，并发恢复线程的个数，默认为4。
cluster.routing.allocation.node_concurrent_recoveries: 2
添加删除节点或负载均衡时并发恢复线程的个数，默认为4。
indices.recovery.max_size_per_sec: 0
设置数据恢复时限制的带宽，如入100mb，默认为0，即无限制。
indices.recovery.concurrent_streams: 5
设置这个参数来限制从其它分片恢复数据时最大同时打开并发流的个数，默认为5。
discovery.zen.minimum_master_nodes: 1
设置这个参数来保证集群中的节点可以知道其它N个有master资格的节点。默认为1，对于大的集群来说，可以设置大一点的值（2-4）
discovery.zen.ping.timeout: 3s
设置集群中自动发现其它节点时ping连接超时时间，默认为3秒，对于比较差的网络环境可以高点的值来防止自动发现时出错。
discovery.zen.ping.multicast.enabled: false
设置是否打开多播发现节点，默认是true。
discovery.zen.ping.unicast.hosts: ["host1", "host2:port", "host3[portX-portY]"]
设置集群中master节点的初始列表，可以通过这些节点来自动发现新加入集群的节点。
下面是一些查询时的慢日志参数设置
index.search.slowlog.level: TRACE
index.search.slowlog.threshold.query.warn: 10s
index.search.slowlog.threshold.query.info: 5s
index.search.slowlog.threshold.query.debug: 2s
index.search.slowlog.threshold.query.trace: 500ms
index.search.slowlog.threshold.fetch.warn: 1s
index.search.slowlog.threshold.fetch.info: 800ms
index.search.slowlog.threshold.fetch.debug:500ms
index.search.slowlog.threshold.fetch.trace: 200ms
```



### 6.2 容器部署

#### 6.2.1 创建容器

```bash
# -e "cluster.name=elasticsearch" 集群名称，相同名称的节点会进入同一集群
# -e "node.name=elas" 节点名称，相同集群中节点名称保持唯一
# -v 将配置文件、数据、插件挂载到容器中，使其保存在Docker外
$ docker run -d -p 9200:9200 -p 9300:9300 --name elas -e "cluster.name=elasticsearch" -e "node.name=elas" -v ~/Documents/Workspace/Docker/Elastic/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v ~/Documents/Workspace/Docker/Elastic/data:/usr/share/elasticsearch/data -v ~/Documents/Workspace/Docker/Elastic/plugins:/usr/share/elasticsearch/plugins docker.elastic.co/elasticsearch/elasticsearch:6.3.0
```



#### 6.2.2 安装插件

```bash
### 进入容器
$ docker exec -it elas /bin/bash

### 安装分词插件
# ./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.3.0/elasticsearch-analysis-ik-6.3.0.zip

### 下面两个插件，如果不将本地plugins文件夹挂载到容器中，是会自动生成的，现在需手工安装
# ./bin/elasticsearch-plugin install ingest-geoip
# ./bin/elasticsearch-plugin install ingest-user-agent

### 查看插件，三个插件安装完成
# ls plugins/
analysis-ik  ingest-geoip  ingest-user-agent
```



#### 6.2.3 常用命令

Elasticsearch是通过http请求进行操作的，常用指令如下

```bash
###### Elasticsearch6.x 对content-type验证采用了严格模式，不加--header会报错 
###### Content-Type header [application/x-www-form-urlencoded] is not supported

# 创建index=accounts，其中包括一个type=person，
# person包含三个字段，user，title，desc，都启用了ik分词插件
$ curl --header "content-type: application/JSON" -X PUT 'localhost:9200/accounts' -d '
{
  "mappings": {
    "person": {
      "properties": {
        "user": {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        },
        "title": {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        },
        "desc": {
          "type": "text",
          "analyzer": "ik_max_word",
          "search_analyzer": "ik_max_word"
        }
      }
    }
  }
}'

# 查看所有index
$ curl -X GET 'http://localhost:9200/_cat/indices?v'

# 删除index=accounts
$ curl -X DELETE 'localhost:9200/accounts'


# 在index=person创建id为1的一条记录，包括三个属性
# 此时如果index=accounts不存在，会自动创建
# PUT操作必须指定id，与post不同
$ curl --header "content-type: application/JSON" -X PUT 'localhost:9200/accounts/person/1?pretty' -d '
{
  "user": "张三",
  "title": "工程师",
  "desc": "数据库管理"
}' 

# POST时可以不指定id，有系统自动生成
$ curl -X POST 'localhost:9200/accounts/person' -d '
{
  "user": "李四",
  "title": "工程师",
  "desc": "系统管理"
}'

# 查看type=person中id为1的记录
$ curl 'localhost:9200/accounts/person/1?pretty'

# 删除type=person中id=1的记录
$ curl -X DELETE 'localhost:9200/accounts/person/1'

# 查询所有记录
$ curl 'localhost:9200/accounts/person/_search?pretty=true'

# 查询满足desc包含‘管理’的记录
$ curl --header "content-type: application/JSON" 'localhost:9200/accounts/person/_search?pretty'  -d '
{
  "query" : { "match" : { "desc" : "管理" }},
  "from": 1,
  "size": 1
}'

# 多个关键词用空格分开，表示OR
$ curl --header "content-type: application/JSON" 'localhost:9200/accounts/person/_search?pretty'  -d '
{
  "query" : { "match" : { "desc" : "数据库 系统" }}
}'

# 以下写法表示AND
$ curl --header "content-type: application/JSON" 'localhost:9200/accounts/person/_search?pretty'  -d '
{
  "query": {
    "bool": {
      "must": [
        { "match": { "desc": "数据库" } },
        { "match": { "desc": "系统" } }
      ]
    }
  }
}'
```



### 6.3 开发

#### 6.3.1 模块

与solr类似，创建模块，创建相应的接口、配置文件等。



#### 6.3.2 pom.xml

参考solr模块的pom.xml，区别在于依赖如下

```xml
<dependencies>

        <dependency>
            <groupId>org.elasticsearch</groupId>
            <artifactId>elasticsearch</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-elasticsearch</artifactId>
        </dependency>

        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>

        <dependency>
            <groupId>net.java.dev.jna</groupId>
            <artifactId>jna</artifactId>
        </dependency>

        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
        </dependency>

    </dependencies>
```



#### 6.3.3 application.properties

配置文件与solr类似，区别在于把solr的配置项换成elastic的

```properties
## Elastic
# 节点名字，默认elasticsearch
spring.data.elasticsearch.cluster-name=elasticsearch
# 节点地址，多个节点用逗号隔开
spring.data.elasticsearch.cluster-nodes=127.0.0.1:9300
# spring.data.elasticsearch.local=false
spring.data.elasticsearch.repositories.enable=true
```



#### 6.3.4 ArticleRepository.java

ArticleRepository相当于通常意义上的DAO层，用来与数据库直接进行交互

```java
import com.tyrival.entity.article.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {
}
```



#### 6.3.5 ArticleServiceImpl.java

Service层直接调用articleRepository进行数据的操作，并将服务注册到Zookeeper

```java
import com.alibaba.dubbo.config.annotation.Service;
import com.tyrival.common.article.ArticleService;
import com.tyrival.elastic.repo.ArticleRepository;
import com.tyrival.entity.article.Article;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@org.springframework.stereotype.Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void create(Article article) {
        articleRepository.save(article);
    }

    @Override
    public void delete(Article article) {
        articleRepository.delete(article);
    }

    @Override
    public List<Article> list(String keyword) {
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(keyword);
        Iterable<Article> searchResult = articleRepository.search(builder);
        List<Article> list = new ArrayList<>();
        for (Iterator iter = searchResult.iterator(); iter.hasNext();) {
            list.add((Article) iter.next());
        }
        return list;
    }
}
```



## 7. Tesseract

### 7.1 准备

- 安装Tesseract，安装方法：https://github.com/tesseract-ocr/tesseract/wiki
- 下载官方的训练库，里面包含许多语言，这里只需要简体中文chi_sim.traineddata，下载地址：https://github.com/tesseract-ocr/tessdata



### 7.2 pom.xml

最重要的是下面这个Tess4j，

```xml
<dependencies>
    <dependency>
        <groupId>net.sourceforge.tess4j</groupId>
        <artifactId>tess4j</artifactId>
        <version>4.0.2</version>
    </dependency>
<dependencies>
```



### 7.3 Tessdata

将第一步中下载的chi_sim.traineddata放在/src/main/resources/tessdata中，作为当前工程的训练库，基本可以识别大部分印刷体中文。如果有个性化的需求，例如艺术字、手写稿等，可以在网上搜索训练方法，非常简单，基本就是通过命令行加载并识别稿件，并对成果进行手工校准，校准完成之后就能形成个性化的训练库。



### 7.4 application.properties

```properties
# 设置训练库的路径，即在resources中的文件夹名
tess.data=tessdata
```



### 7.4 OcrServiceImpl.java

```java
import com.alibaba.dubbo.config.annotation.Service;
import com.tyrival.common.ocr.OcrService;
import com.tyrival.ocr.utils.ExcelUtil;
import com.tyrival.ocr.utils.FileUtil;
import com.tyrival.ocr.utils.OcrUtil;
import com.tyrival.ocr.utils.WordUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ClassUtils;

@Service
@org.springframework.stereotype.Service
public class OcrServiceImpl implements OcrService {

	// application.properties中设置的tessData注入
    @Value("${tess.data}")
    private String tessData;

    @Override
    public String doOCR(String filePath) throws Exception {
        // word文件用poi读取
        if (FileUtil.isWord(filePath)) {
            return WordUtil.read(filePath);
        }
        // excel文件用poi读取
        if (FileUtil.isExcel(filePath)) {
            return ExcelUtil.read(filePath);
        }
        // 其他文件ocr，例如图片、pdf等
        String result = OcrUtil.doOCR(filePath, this.getTessData());
        return result;
    }

    private String getTessData() {
        return ClassUtils.getDefaultClassLoader().getResource("").getPath() + tessData;
    }
}
```



### 7.5 OrcUtil.java

```java
package com.tyrival.ocr.utils;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.FileNotFoundException;

public class OcrUtil {

    private final static String CHINESE = "chi_sim";
    private final static String ENGLISH = "eng";

    private static ITesseract instance;

    public static String doOCR(String filePath, String tessData) throws TesseractException, FileNotFoundException {
        return doOCR(filePath, tessData, CHINESE);
    }

    /* 过程非常简单 */
    public static String doOCR(String filePath, String tessData, String language) throws TesseractException, FileNotFoundException {

        ITesseract instance = getTesseract();
        File file = new File(filePath);
        if (!file.isFile()) {
            throw new FileNotFoundException("未找到 " + filePath + " 文件");
        }
        
        // tessdata文件夹默认位置是工程根目录下/tessdata，即与/src目录同级
        // 此处我们放在resources下，所以要进行手工设置datapath
        instance.setDatapath(tessData);
        instance.setLanguage(language);
        String result = instance.doOCR(file);
        return result;
    }

    private static ITesseract getTesseract() {
        if (instance == null) {
            instance = new Tesseract();
        }
        return instance;
    }
}
```



### 7.6 问题

在MacOS下运行工程，如果通过controller模块中的`/ocr/do`接口运行，在识别过程中会崩溃，错误日志反应似乎是和dubbo有冲突，Windows和Linux还没测试，不知道是不是MacOS特有的。

解决办法：直接在ocr模块中建立controller，直接访问本模块的service，不通过dubbo进行远程调度，就不会出现这个问题。