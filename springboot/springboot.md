1.什么是SpringBoot？

```
用来简化Spring应用的初始搭建以及开发过程，使用特定的方式来进行配置
创建独立的Spring引用程序main方法运行
嵌入的tomcat无需部署war文件
简化maven配置
自动配置Spring添加对应的功能starter自动化配置
有丰富的生态系统，易于集成

SpringBoot来简化Spring应用开发，约定大于配置，去繁化简
```

2.SpringBoot有哪些优点？

```
SpringBoot是一个基于Spring框架的开源项目，旨在简化Spring应用的初始搭建以及开发过程。通过自动化的配置，它让开发者能够更快地启动和运行Spring应用。SpringBoot采用了“约定优于配置”（Convention Over Configuration）的原则，通过提供大量的默认配置来减少开发者需要进行的显式配置。以下是SpringBoot的一些主要特性和优点：

快速开发：
SpringBoot极大地简化了Spring应用的初始搭建和开发过程。开发者只需添加Spring Boot Starter依赖，即可快速开始项目的开发，
无需进行大量的XML配置。
独立运行：SpringBoot应用可以打包成可执行的jar或war文件，内嵌了Servlet容器（如Tomcat、Jetty或Undertow），因此可以独立
运行，无需部署到外部容器中。
自动配置：SpringBoot提供了自动配置的功能，能够基于添加的jar依赖自动配置Spring应用。例如，如果添加了Spring Web Starter
依赖，SpringBoot会自动配置Tomcat和Spring MVC。
无代码生成和XML配置：SpringBoot通过Java配置类来实现配置，尽可能地减少或消除XML配置。同时，它也避免了代码的生成，
使得项目结构更加清晰。
提供生产就绪型功能：SpringBoot提供了很多用于生产环境的功能，如性能指标、健康检查、外部化配置等。这些功能使得SpringBoot
应用能够很容易地部署到生产环境中。
丰富的生态系统：SpringBoot拥有庞大的生态系统，提供了大量的starter依赖，用于简化各种常见场景的配置。这些starter依赖涵盖
了数据库连接、缓存、消息队列、安全等多个方面。
易于集成：SpringBoot很容易与其他框架和库进行集成，如MyBatis、JPA、Redis等。开发者只需添加相应的starter依赖，并进行简单
的配置，即可在SpringBoot应用中使用这些框架和库。
开发者工具：SpringBoot提供了开发者工具（DevTools），该工具能够在应用运行时自动重启，并支持快速的应用属性更新和现场模板
重载等功能，极大地提高了开发效率。
社区支持：SpringBoot作为Spring生态中的重要一员，拥有庞大的社区支持。开发者可以在社区中获取到丰富的教程、文档和解决方案，
遇到问题时也能够得到及时的帮助。
综上所述，SpringBoot通过其快速开发、独立运行、自动配置等特性，极大地简化了Spring应用的开发和部署过程，成为了Java开发者
广泛使用的框架之一。
```

3.SpringBoot、Spring MVC和Spring有什么区别？

```
Spring
Spring最重要的特征是依赖注入。所有Spring Modules不是依赖注入就是IOC控制反转。
当我们恰当的使用DI或者是IOC的时候，可以开发松耦合应用。
Spring MVC
Spring MVC提供了一种分离式的方法来开发Web应用。通过运用像DispatcherServelet，MoudlAndView 和 ViewResolver 等一些简单
的概念，开发 Web 应用将会变的非常简单。
SpringBoot
Spring和Spring MVC的问题在于需要配置大量的参数。
SpringBoot通过一个自动配置和启动的项来解决这个问题。

Spring：拥有广泛的生态系统，包括各种第三方库和工具，这些可以集成到Spring应用程序中，以提供额外的功能。
SpringBoot：在继承Spring生态系统的同时，也提供了自己的starter依赖，用于简化常见场景的配置和集成。
```

4.SpringBoot的核心注解是什么？由那些注解组成？

```
SpringBoot的核心注解是@SpringBootApplication，它是一个非常方便的注解，用于Spring Boot应用的启动类上。这个注解实际上是
三个关键注解的组合，它们分别是：

@SpringBootConfiguration：
这个注解标记当前类为配置类，告诉Spring Boot这是一个Spring配置，允许通过Spring的基于Java的配置源来添加bean定义以及导入
其他配置类。这个注解内部通过@Configuration注解来实现。
@EnableAutoConfiguration：
这个注解告诉Spring Boot基于添加的jar依赖自动配置你的Spring应用。@EnableAutoConfiguration注解会尝试根据你添加的jar依赖
自动配置Spring应用。例如，如果你的classpath下存在HSQLDB，Spring Boot会自动配置内存数据库。你可以通过添加
@EnableAutoConfiguration注解的exclude属性来排除特定的自动配置。
@ComponentScan：
这个注解告诉Spring在包和子包下查找其他组件、配置和服务，让Spring创建bean、扫描到这些注解的类并注册为Spring应用上下文中
的bean。@SpringBootApplication默认扫描启动类所在的包及其子包下的所有组件。
因此，当你将@SpringBootApplication注解添加到你的主类上，它实际上是在告诉Spring Boot执行以下操作：

基于你的类路径设置、其他bean和各种属性设置来自动配置你的Spring应用。
扫描你的应用以找到其他相关组件、配置和服务，让Spring自动创建并注册这些bean。
将你的主类标记为配置类，允许你添加@Bean方法以及导入其他配置类。
这种组合简化了Spring应用的初始化和配置过程，使得开发者能够更专注于业务逻辑的开发，而不是花费大量时间在Spring的配置上。


@SpringBootConfiguration：
组合了 @Configuration 注解，实现配置文件的功能。
@EnableAutoConfiguration：
打开自动配置的功能，也可以关闭某个自动配置的选项，如关闭数据源自动配置功能： @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })。
@ComponentScan：
Spring组件扫描.
```

5.SpringBoot自动配置原理是什么？

```
SpringBoot的自动配置原理是Spring框架中一个重要的特性，它极大地简化了应用程序的开发和部署过程。以下是SpringBoot自动配置原理的详细解释：

一、核心概念
条件注解（Conditional Annotations）：
Spring Boot 提供了一系列条件注解，如 @ConditionalOnClass、@ConditionalOnMissingBean、@ConditionalOnProperty 等，用于根据特定条件决定是否加载某个配置类或Bean。
这些条件可以基于类路径中存在的依赖、配置属性的值、环境变量或其他Spring Bean的存在等。
自动配置类（Auto-configuration Classes）：
这些类通过 META-INF/spring.factories 文件被自动加载。当Spring Boot应用程序启动时，会扫描这个文件，并加载其中定义的自动配置类。
自动配置类内部定义了基于条件注解的Bean声明，只有当满足特定条件时，这些Bean才会被创建和注册到Spring容器中。
配置属性（Configuration Properties）：
通过外部化配置（如 application.properties 或 application.yml）来控制自动配置的行为。
使用 @ConfigurationProperties 注解可以将配置文件中的属性值自动绑定到配置类的属性上。
二、实现机制
启用自动配置：
在主类上添加 @SpringBootApplication 注解，该注解包含了 @EnableAutoConfiguration，用于开启自动配置功能。
@EnableAutoConfiguration 注解内部使用 @Import(AutoConfigurationImportSelector.class) 来加载配置类。
加载自动配置类：
Spring Boot 在启动时会扫描 META-INF/spring.factories 文件，找到所有自动配置类。
AutoConfigurationImportSelector 类负责从 spring.factories 文件中加载配置类，并通过条件注解的评估来决定是否加载这些配置类。
条件注解的评估：
根据条件注解（如 @ConditionalOnClass、@ConditionalOnMissingBean 等），AutoConfigurationImportSelector 会决定是否加载对应的配置类或Bean。
如果满足条件，则相应的Bean会被创建并注册到Spring容器中；如果不满足条件，则相应的Bean会被忽略。
三、自动配置的优先级和覆盖
在某些情况下，可能存在多个自动配置类都能满足条件的情况。为了解决这种冲突，Spring Boot为自动配置类定义了优先级。
具有更高优先级的配置类将覆盖具有较低优先级的配置类。
开发者可以通过自定义配置类来覆盖Spring Boot默认的自动配置行为。

综上所述，SpringBoot的自动配置原理通过条件注解、自动配置类和配置属性等核心概念，以及一系列的实现机制，极大地简化了
Spring应用程序的开发和部署过程。
```

简而言之：Springboot启动时，会通过@EnableAutoConfiguration注解、加载META-INF/spring.factories文件中的某些配置Bean，具体加载与否，取决于项目引入的start模块、项目中的配置、条件注解如 @ConditionalOnClass、@ConditionalOnMissingBean，AutoConfigurationImportSelector会决定是否加载对应的配置类活Bean。满足条件则相应的Bean会被创建并注册到Spring容器，反之，则忽略相应的Bean.

6.SpringBoot 需要独立的容器运行吗？

可以不需要，内置了 Tomcat/ Jetty 等容器。

7.SpringBoot启动时都做了什么?

```
SpringBoot的启动原理是一个相对复杂但设计得既简单又高效的流程，它极大地简化了基于Spring的应用开发和服务部署。
以下是SpringBoot启动原理的详细解析：

一、启动流程概述
SpringBoot应用的启动从一个包含main()方法的主类开始，这个主类通常使用@SpringBootApplication注解标记。
@SpringBootApplication是一个组合注解，包含了@SpringBootConfiguration、@EnableAutoConfiguration和@ComponentScan。
当调用SpringApplication.run()方法时，会启动Spring应用。

二、启动过程详解
1.加载主类和配置文件
启动过程从加载包含main()方法的主类开始。
读取项目中的配置文件（主要是application.yml和application.properties），这些配置文件会指定项目的启动端口号、数据库连接
等配置信息。
2.创建SpringApplication对象
调用SpringApplication.run()方法时，首先会创建一个SpringApplication对象。
SpringApplication对象负责管理Spring应用的启动和初始化，包括设置一些基础属性，如是否应该添加命令行属性，是否需要打印启动横幅等。
3.环境准备
应用上下文准备前，先准备环境（Environment），这包括配置文件、命令行参数、环境变量等的解析。
4.创建应用上下文
根据你选择的web环境类型（如Servlet或Reactive），Spring Boot设置合适的应用上下文（ApplicationContext）。
5.注册应用上下文初始化器和监听器
应用上下文初始化器（ApplicationContextInitializer）和应用事件监听器（ApplicationListener）会被加载和注册，它们可以在
上下文的生命周期的不同阶段执行操作。
6.加载Bean定义
通过扫描类路径中的组件和配置类（由@ComponentScan和@Configuration等注解指定）来加载Bean定义。
7.自动配置
@EnableAutoConfiguration注解激活了自动配置的机制，它告诉Spring Boot基于添加到项目中的jar依赖，尝试去猜测并配置你可能
需要的Bean。整个J2EE的整体解决方案和自动配置都在springboot-autoconfigure的jar包中；
自动配置类会在应用上下文中被条件化地执行，以确保配置的正确性和合理性。
8.Bean实例化
在所有的配置类和组件被加载之后，Spring容器会根据需要创建和配置Bean实例。
9.刷新应用上下文
完成所有配置后，应用上下文会被刷新，此时所有的Bean都已注册完毕，且已准备就绪。
10.触发命令行运行器和应用就绪事件
如果应用中包含CommandLineRunner或ApplicationRunner Bean，它们会在此时执行。
最后，一个ApplicationReadyEvent被发布，表明应用已完全启动并准备好接收请求。
三、总结
通过上述机制，SpringBoot实现了“约定大于配置”的软件设计范式，极大地简化了基于Spring的应用开发和服务部署。SpringBoot的
启动过程是一个复杂而又精妙的流程，它负责加载配置、初始化应用上下文、自动配置和启动内嵌服务器等任务，使得开发者能够快速、
轻松地部署和管理Spring应用。
```

简而言之：

1.加载主类和配置文件，比如application.yml和application.properties.

2.调用SpringApplication.run()，创建SpringApplication对象.

3.根据环境配置，创建应用上下文ApplicationContext.

4.整个J2EE的整体解决方案和自动配置都在springboot-autoconfigure的jar包中，从类路径下的META-INF/spring.factories中获取EnableAutoConfiguration指定的值，创建项目所需要的配置Bean。

5.进行Bean的实例化.

6.完成所有的配置后，刷新应用上下文，最后一个触发应用就绪事件，表明应用以及完全启动，并准备好接收请求

8.读取配置文件原理和加载顺序优先级

```
加载顺序优先级（以Spring Boot为例）
Spring Boot按照特定的顺序和优先级加载配置文件。以下是一个常见的加载顺序和优先级列表（从高到低）：

命令行参数：通过命令行参数指定的配置文件路径或配置项具有最高的优先级，可以覆盖其他所有位置的配置。
外部配置（file:./config/）：位于当前目录下的config子目录中的配置文件。
外部配置（file:./）：位于当前目录下的配置文件。
类路径下的config包（classpath:/config/）：位于项目的resources/config目录下的配置文件，当应用被打包成jar或war文件时，
这对应于jar或war文件内部的BOOT-INF/classes/config/目录。
类路径（classpath:/）：位于项目的resources目录下的配置文件，当应用被打包成jar或war文件时，这对应于jar或war文件内部
的BOOT-INF/classes/目录。
通过@PropertySource注解指定的配置文件：Spring框架允许通过@PropertySource注解来指定额外的配置文件路径，这些路径可以是
文件系统上的路径，也可以是URL。
默认配置：如果以上位置都没有找到配置文件，Spring Boot会使用其默认的配置。
注意：

如果同一个目录下存在多个相同名称的配置文件（如application.yml和application.properties），默认会先读取application.
properties。
如果同一个配置属性在多个配置文件中都进行了配置，那么优先级更高的位置上的配置文件中的配置将覆盖优先级较低位置上的同名配置。
在Spring Boot中，还可以通过设置spring.config.location属性来指定额外的配置文件路径，这些路径可以是文件系统上的路径，也
可以是URL。当指定了额外的配置文件位置时，Spring Boot会按照指定的顺序加载这些配置文件，并将其与默认位置上的配置文件合并。
综上所述，读取配置文件的原理涉及文件的定位、打开、读取、解析和内存加载等步骤，而加载顺序和优先级则取决于配置文件的位置
和Spring Boot的配置规则。
```

9.会不会自定义Starter?大概实现过程?

```
是的，可以自定义Starter。在Spring Boot中，Starter是一个非常重要的概念，它能够将模块所需的依赖整合起来，并对模块内的Bean根据环境（条件）进行自动配置。自定义Starter的过程主要包括以下几个步骤：

1. 创建项目结构
首先，需要创建一个Maven项目作为Starter的基础。这个项目通常会包含两个主要模块：

starter模块：这是用户最终会依赖的模块，它不会包含任何具体的业务代码，只会声明对其他模块的依赖。
autoconfigure模块：这个模块包含了Starter的自动配置逻辑，包括配置类、服务类等。
2. 配置POM文件
在starter模块和autoconfigure模块的POM文件中，需要配置相应的依赖。特别是autoconfigure模块的POM文件，需要引入Spring Boot的自动配置相关依赖，如spring-boot-starter和spring-boot-configuration-processor。

xml
<dependencies>  
    <!-- 引入Spring Boot自动配置依赖 -->  
    <dependency>  
        <groupId>org.springframework.boot</groupId>  
        <artifactId>spring-boot-starter</artifactId>  
    </dependency>  
    <!-- 引入配置处理器，用于生成配置元数据 -->  
    <dependency>  
        <groupId>org.springframework.boot</groupId>  
        <artifactId>spring-boot-configuration-processor</artifactId>  
        <optional>true</optional>  
    </dependency>  
    <!-- 其他依赖 -->  
</dependencies>
注意：在自定义的starter的pom中，将spring-boot-autoconfigure的maven依赖声明为<optional>true</optional>，表明自定义的starter的jar包不会传递spring-boot-autoconfigure依赖；否则会将spring-boot-autoconfigure版本固定，导致引用自定义starter的应用出现版本冲突问题。

3. 编写配置类
在autoconfigure模块中，需要编写一个或多个配置类（通常是@ConfigurationProperties注解的类），用于接收application.properties或application.yml中的配置信息。

java
@ConfigurationProperties(prefix = "my.custom")  
public class MyCustomProperties {  
    private String name;  
    // getter和setter省略  
}
4. 编写自动配置类
编写一个或多个自动配置类（通常是@Configuration注解的类），该类会利用条件注解（如@ConditionalOnClass、@ConditionalOnProperty等）来决定是否进行自动配置。同时，该类会利用@EnableConfigurationProperties注解来启用配置类，并将配置类的实例注入到Bean中。

java
@Configuration  
@EnableConfigurationProperties(MyCustomProperties.class)  
@ConditionalOnClass(MyCustomService.class)  
public class MyCustomAutoConfiguration {  
  
    @Autowired  
    private MyCustomProperties myCustomProperties;  
  
    @Bean  
    public MyCustomService myCustomService() {  
        return new MyCustomService(myCustomProperties.getName());  
    }  
}
5. 编写服务类
服务类是Starter提供的主要功能实现。它可以根据需要依赖配置类中的配置信息。

java
public class MyCustomService {  
    private String name;  
  
    public MyCustomService(String name) {  
        this.name = name;  
    }  
  
    // 其他业务方法  
}
6. 创建spring.factories文件
在autoconfigure模块的resources/META-INF目录下，创建一个名为spring.factories的文件，并在该文件中指定自动配置类的全路径。

properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\  
com.example.autoconfigure.MyCustomAutoConfiguration
7. 打包和分发
最后，将自定义的Starter打包成jar文件，并分发到Maven仓库或其他依赖管理系统中。其他Spring Boot项目就可以通过添加依赖的方式引入这个Starter，并享受它提供的自动配置功能了。

以上就是自定义Spring Boot Starter的大概实现过程。需要注意的是，具体的实现细节可能会根据项目的具体需求和Spring Boot的版本而有所不同。
```

10.SpringBoot的默认日志实现框架是什么…换成别的?

```
SpringBoot的默认日志实现框架是Logback。Logback是Log4j的继任者，提供了更好的性能和可靠性。Spring Boot项目在默认情况下会
包含spring-boot-starter-logging依赖，该依赖会自动引入Logback作为日志框架。

如果想将SpringBoot的日志框架换成其他框架，如Log4j2或Java Util Logging（JUL），可以通过以下步骤进行：

换成Log4j2
排除Logback依赖：在项目的pom.xml文件中，找到spring-boot-starter-web或类似的starter依赖，并排除其中的spring-boot-starter-logging（它默认包含Logback）。
xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-web</artifactId>  
    <exclusions>  
        <exclusion>  
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-logging</artifactId>  
        </exclusion>  
    </exclusions>  
</dependency>
添加Log4j2依赖：在pom.xml中添加spring-boot-starter-log4j2依赖。
xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-log4j2</artifactId>  
</dependency>
配置Log4j2：在src/main/resources目录下创建Log4j2的配置文件，如log4j2.xml或log4j2-spring.xml，并根据需要进行配置。

无论使用哪种日志框架，都需要在项目的配置文件中（如application.properties或application.yml）设置日志级别、输出格式等参数。
切换日志框架时，请确保删除了旧的配置文件（如logback.xml）并创建了新的配置文件（如log4j2.xml），以避免配置冲突。
考虑到兼容性和性能，建议在进行日志框架切换时仔细评估项目需求，并参考官方文档和社区经验。
```

格式如下FYI: logback-spring.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!-- 日志级别从低到高分为TRACE < DEBUG < INFO < WARN < ERROR < FATAL，如果设置为WARN，则低于WARN的信息都不会输出 -->
<!-- scan:当此属性设置为true时，配置文档如果发生改变，将会被重新加载，默认值为true -->
<!-- scanPeriod:设置监测配置文档是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。 当scan为true时，此属性生效。默认的时间间隔为1分钟。 -->
<!-- debug:当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。 -->
<configuration scan="true" scanPeriod="60 seconds"
	debug="false">
	<contextName>qdp</contextName>

	<!-- <springProperty scope="context" name="logPath" source="logging.path" 
		defaultValue="logs"/> -->
	<!-- name的值是变量的名称，value的值时变量定义的值。通过定义的值会被插入到logger上下文中。定义后，可以使“${}”来使用变量。 -->
	<property name="log.path" value="logs/" />

	<!--0. 日志格式和颜色渲染 -->
	<!-- 彩色日志依赖的渲染类 -->
	<conversionRule conversionWord="clr"
		converterClass="org.springframework.boot.logging.logback.ColorConverter" />
	<conversionRule conversionWord="wex"
		converterClass="org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter" />
	<conversionRule conversionWord="wEx"
		converterClass="org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter" />
	<!-- 彩色日志格式 -->
	<property name="CONSOLE_LOG_PATTERN"
			  value="${CONSOLE_LOG_PATTERN:-%clr([TRACEID:%X{trace_id}] %d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr(${PID:- }){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}}" />

	<!--1. 输出到控制台 -->
	<appender name="CONSOLE"
		class="ch.qos.logback.core.ConsoleAppender">
		<!-- 此日志appender是为开发使用，只配置最底级别，控制台输出的日志级别是大于或等于此级别的日志信息 -->
		<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
			<level>debug</level>
		</filter>
		<encoder>
			<Pattern>${CONSOLE_LOG_PATTERN}</Pattern>
			<!-- 设置字符集 -->
			<charset>UTF-8</charset>
		</encoder>
	</appender>



	<!--2. 输出到文档 -->
	<!-- 2.1 level为 DEBUG 日志，时间滚动输出 -->
	<appender name="DEBUG_FILE"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<!-- 正在记录的日志文档的路径及文档名 -->
		<file>${log.path}/web_debug.log</file>
		<!--日志文档输出格式 -->
		<encoder>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} -
				%msg%n</pattern>
			<charset>UTF-8</charset> <!-- 设置字符集 -->
		</encoder>
		<!-- 日志记录器的滚动策略，按日期，按大小记录 -->
		<rollingPolicy
			class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<!-- 日志归档 -->
			<fileNamePattern>${log.path}/web-debug-%d{yyyy-MM-dd}.%i.log
			</fileNamePattern>
			<timeBasedFileNamingAndTriggeringPolicy
				class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>100MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
			<!--日志文档保留天数 -->
			<maxHistory>15</maxHistory>
		</rollingPolicy>
		<!-- 此日志文档只记录debug级别的 -->
		<filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>debug</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
	</appender>

	<!-- 2.2 level为 INFO 日志，时间滚动输出 -->
	<appender name="INFO_FILE"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<!-- 正在记录的日志文档的路径及文档名 -->
		<file>${log.path}/web_info.log</file>
		<!--日志文档输出格式 -->
		<encoder>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} -
				%msg%n</pattern>
			<charset>UTF-8</charset>
		</encoder>
		<!-- 日志记录器的滚动策略，按日期，按大小记录 -->
		<rollingPolicy
			class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<!-- 每天日志归档路径以及格式 -->
			<fileNamePattern>${log.path}/web-info-%d{yyyy-MM-dd}.%i.log
			</fileNamePattern>
			<timeBasedFileNamingAndTriggeringPolicy
				class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>100MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
			<!--日志文档保留天数 -->
			<maxHistory>15</maxHistory>
		</rollingPolicy>
		<!-- 此日志文档只记录info级别的 -->
		<filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>info</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
	</appender>

	<!-- 2.3 level为 WARN 日志，时间滚动输出 -->
	<appender name="WARN_FILE"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<!-- 正在记录的日志文档的路径及文档名 -->
		<file>${log.path}/web_warn.log</file>
		<!--日志文档输出格式 -->
		<encoder>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} -
				%msg%n</pattern>
			<charset>UTF-8</charset> <!-- 此处设置字符集 -->
		</encoder>
		<!-- 日志记录器的滚动策略，按日期，按大小记录 -->
		<rollingPolicy
			class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<fileNamePattern>${log.path}/web-warn-%d{yyyy-MM-dd}.%i.log
			</fileNamePattern>
			<timeBasedFileNamingAndTriggeringPolicy
				class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>100MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
			<!--日志文档保留天数 -->
			<maxHistory>15</maxHistory>
		</rollingPolicy>
		<!-- 此日志文档只记录warn级别的 -->
		<filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>warn</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
	</appender>

	<!-- 2.4 level为 ERROR 日志，时间滚动输出 -->
	<appender name="ERROR_FILE"
		class="ch.qos.logback.core.rolling.RollingFileAppender">
		<!-- 正在记录的日志文档的路径及文档名 -->
		<file>${log.path}/web_error.log</file>
		<!--日志文档输出格式 -->
		<encoder>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} -
				%msg%n</pattern>
			<charset>UTF-8</charset> <!-- 此处设置字符集 -->
		</encoder>
		<!-- 日志记录器的滚动策略，按日期，按大小记录 -->
		<rollingPolicy
			class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
			<fileNamePattern>${log.path}/web-error-%d{yyyy-MM-dd}.%i.log
			</fileNamePattern>
			<timeBasedFileNamingAndTriggeringPolicy
				class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
				<maxFileSize>100MB</maxFileSize>
			</timeBasedFileNamingAndTriggeringPolicy>
			<!--日志文档保留天数 -->
			<maxHistory>15</maxHistory>
		</rollingPolicy>
		<!-- 此日志文档只记录ERROR级别的 -->
		<filter class="ch.qos.logback.classic.filter.LevelFilter">
			<level>ERROR</level>
			<onMatch>ACCEPT</onMatch>
			<onMismatch>DENY</onMismatch>
		</filter>
	</appender>


	<!-- <logger>用来设置某一个包或者具体的某一个类的日志打印级别、 以及指定<appender>。<logger>仅有一个name属性， 
		一个可选的level和一个可选的addtivity属性。 name:用来指定受此logger约束的某一个包或者具体的某一个类。 level:用来设置打印级别，大小写无关：TRACE, 
		DEBUG, INFO, WARN, ERROR, ALL 和 OFF， 还有一个特俗值INHERITED或者同义词NULL，代表强制执行上级的级别。 
		如果未设置此属性，那么当前logger将会继承上级的级别。 addtivity:是否向上级logger传递打印信息。默认是true。 <logger 
		name="org.springframework.web" level="info"/> <logger name="org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor" 
		level="INFO"/> -->

	<!-- 使用mybatis的时候，sql语句是debug下才会打印，而这里我们只配置了info，所以想要查看sql语句的话，有以下两种操作： 
		第一种把<root level="info">改成<root level="DEBUG">这样就会打印sql，不过这样日志那边会出现很多其他消息 
		第二种就是单独给dao下目录配置debug模式，代码如下，这样配置sql语句会打印，其他还是正常info级别： 【logging.level.org.mybatis=debug 
		logging.level.dao=debug】 -->

	<!-- root节点是必选节点，用来指定最基础的日志输出级别，只有一个level属性 level:用来设置打印级别，大小写无关：TRACE, 
		DEBUG, INFO, WARN, ERROR, ALL 和 OFF， 不能设置为INHERITED或者同义词NULL。默认是DEBUG 可以包含零个或多个元素，标识这个appender将会添加到这个logger。 -->

	<!-- 4. 最终的策略 -->
	<!-- 4.1 开发环境:打印控制台 -->
	<springProfile name="dev">
		<root level="info">
			<appender-ref ref="CONSOLE" />
		</root>
	</springProfile>

	<!-- 4.2 测试环境:输出到文档 -->
	<springProfile name="tst">
		<root level="info">
			<appender-ref ref="CONSOLE" />
			<appender-ref ref="DEBUG_FILE" />
			<appender-ref ref="INFO_FILE" />
			<appender-ref ref="WARN_FILE" />
			<appender-ref ref="ERROR_FILE" />
		</root>
	</springProfile>

	<!-- 4.4 生产环境:输出到文档 -->
	<springProfile name="prod">
		<root level="info">
			<appender-ref ref="CONSOLE" />
			<appender-ref ref="DEBUG_FILE" />
			<appender-ref ref="INFO_FILE" />
			<appender-ref ref="WARN_FILE" />
			<appender-ref ref="ERROR_FILE" />
		</root>
	</springProfile>

</configuration>
```