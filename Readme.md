# 基于Spring Boot、Dubbo、Redis、Mybtis搭建分布式开发项目

## 1. 概述
本文从零开始，讲述基于Spring Boot、Dubbo搭建一个分布式开发项目，并集成Redis、Mybatis及其分页插件PageHelper的所有步骤。
文中所用的IDE为Intellij IDEA，JDK版本为1.8

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

```
## 通用配置文件，任何环境都要用到的配置在此处

# 接收mavan构建时的-P参数，用于引入不同环境的配置文件
spring.profiles.active=@profileActive@

# mybatis配置文件引入
mybatis.config-locations=classpath:mybatis/mybatis-config.xml
mybatis.mapper-locations=classpath:mybatis/mapper/*.xml
```



- src/main/resources/application-dev.properties

```
## dev开发环境下特有的配置记录在此

# logging配置
logging.level.root=info
logging.file=tyrival-log/user-log.log

# 数据源
spring.datasource.driverClassName = org.postgresql.Driver
spring.datasource.url = jdbc:postgresql://10.211.55.14:5432/postgres?useUnicode=true&characterEncoding=utf-8
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




## 3. 部署
可以手工或利用Jenkins等自动化工具进行部署。
在用Maven对各模块进行编译时，在命令行最后加上`-P 参数`，可在编译时引入不同的Spring Boot配置文件，从而对各类环境进行区分，参数包括开发环境`dev`和生产环境`prod`。
