1.什么是Spring？

轻量级、控制反转IOC、面向切面AOP、模块化、非入侵式。

Bean生命周期管理、事务管理、Web支持

核心容器Spring Core，框架的基本功能，包括Bean的创建、管理和依赖注入。

Spring上下文Spring Context）：提供了企业服务，如JNDI、电子邮件、国际化等，并扩展了核心容器的功能.

Spring AOP、Spring MVC、Spring ORM

Spring是一个开源的Java平台，由Rod Johnson在2002年最早提出并随后创建。它是一个为了解决企业应用开发的复杂性而设计的框架，致力于实现敏捷开发。Spring不仅限于服务器端的开发，从简单性、可测试性和松耦合性的角度来看，任何Java应用都可以从Spring中受益。以下是对Spring的详细解析：

### 一、Spring的定义与特点

- **定义**：Spring是一个轻量级的控制反转（IoC）和面向切面（AOP）的容器框架。
- **特点**：

1. **轻量级**：从JAR包的大小和系统资源使用来看，Spring都是轻量级的。其核心JAR包的大小较小，且运行期间只需少量的操作系统资源（CPU和内存）便能稳定运行。
2. **控制反转（IoC）**：Spring通过IoC技术促进了松耦合。一个对象依赖的其他对象会在容器初始化完成后主动传递给它，而不是由对象自己创建或查找依赖对象。
3. **面向切面（AOP）**：Spring提供了面向切面编程的丰富支持，允许通过分离应用的业务逻辑与系统级服务（如审计和事务管理）进行内聚性的开发。
4. **模块化**：Spring是模块化的，应用程序可以根据需求引入相应的模块（以JAR包依赖方式引入）来实现不同的功能。
5. **非侵入式**：Spring应用中的对象不依赖于Spring的特定类。

### 二、Spring的功能与用途

- **功能**：

1. **Bean管理**：Spring以bean的方式组织和管理Java应用中的各个组件及其关系。
2. **事务管理**：提供了一致的事务管理抽象层，简化了事务的编程。
3. **持久层集成**：集成了多种持久化框架，如Hibernate、MyBatis等。
4. **Web支持**：提供了灵活的MVC Web应用框架，简化了Web应用的开发。
5. **AOP支持**：通过AOP技术实现了横切关注点（如日志、安全、事务等）的模块化。

- **用途**：Spring广泛用于企业级应用开发，包括Web应用、分布式系统、微服务架构等。

### 三、Spring的组件与架构

Spring框架包含多个组件，其中核心组件包括：

- **核心容器（Spring Core）**：提供了框架的基本功能，包括Bean的创建、管理和依赖注入等。
- **Spring上下文（Spring Context）**：提供了企业服务，如JNDI、电子邮件、国际化等，并扩展了核心容器的功能。
- **Spring AOP**：提供了面向切面编程的支持，允许开发者将横切关注点模块化。
- **Spring MVC**：提供了构建Web应用的模型-视图-控制器（MVC）框架。
- **Spring ORM**：提供了对ORM框架的集成支持，如Hibernate、MyBatis等。

### 四、Spring的发展历史

Spring框架的形成与发展与J2EE的发展密切相关。在J2EE应用程序广泛实现的时期，虽然带来了诸如事务管理之类的核心中间层概念的标准化，但在实践中并没有获得绝对的成功。Spring的出现正是为了解决这些问题，它使得JAVA EE开发更加容易，并提供了更完善的开发环境。

综上所述，Spring是一个功能强大、灵活且易于使用的Java平台框架，它极大地简化了企业级应用的开发过程。

2.Spring的优缺点？

优点：

- Spring属于低侵入性设计。
- IOC将对象之间的依赖关系交给Spring，降低了组件之间的耦合，实现了各个层之间的解耦，使码农更专注于业务的开发。
- 提供了面向切面的编程。
- 对事务的支持良好，支持配置即可用。
- 对主流的插件提供了良好的支持。

缺点：依赖反射，影响性能。

3. 什么是IOC？

负责创建对象、管理对象（通过依赖注入）、整合对象、配置对象以及管理这些对象的生命周期。

4. 什么是依赖注入？

依赖注入是Spring实现IOC的一种重要手段，将对象间的依赖关系的控制权从开发人员的手里转移到容器。

5.IOC注入的几种方式？

1. Set方式注入（Setter Injection）

```
public class UserServiceImpl {  
    private UserDAO userDAO;  
  
    public void setUserDAO(UserDAO userDAO) {  
        this.userDAO = userDAO;  
    }  
  
    // 其他方法...  
}
```

2.构造器注入（Constructor Injection）

```
public class UserServiceImpl {  
    private UserDAO userDAO;  
  
    public UserServiceImpl(UserDAO userDAO) {  
        this.userDAO = userDAO;  
    }  
  
    // 其他方法...  
}
```

3.注解方式注入

Spring支持多种注解来实现依赖注入，如@Autowired、@Resource等。

- @Autowired：默认按类型装配依赖。如果需要按名称装配，可以结合@Qualifier注解使用。
- @Resource：默认按名称装配依赖，当找不到与名称匹配的bean时，才会按类型装配。

```
@Component  
public class People {  
    @Autowired  
    @Qualifier("cat")  
    private Cat cat;  
  
    @Resource  
    private Dog dog;  
  
    // 其他方法...  
```

6.Spring bean的生命周期?

推断构造方法、实例化、属性注入IOC、初始化前@PostConstruct、初始化阶段InitializingBean(afterPropertiesSet)、初始化后(AOP->代理对象)、使用阶段、销毁阶段。

map<beankey, bean> 如果是AOP的代理对象，代理对象会放入map中

Spring Bean的生命周期由Spring容器全权管理，从Bean的创建、属性注入、初始化到销毁，Spring提供了完整的控制机制。Spring Bean的生命周期可以大致分为以下几个阶段：

### 一、定义与配置阶段

1. **Bean元信息配置**：

- 开发者通过XML配置文件、Java配置类、注解等方式定义Bean的信息，包括Bean的类名、作用域、是否延迟初始化等。
- **Bean元信息解析**：
- Spring容器启动时，会解析这些配置信息，将Bean定义信息转换为Spring内部的BeanDefinition结构。
- **Bean注册**：
- 将解析后的BeanDefinition注册到Spring容器中，供后续使用。

### 二、实例化阶段

1. **Bean Class加载**：

- Spring容器根据BeanDefinition中的类名信息，加载Bean的Class对象。
- **Bean实例化**：
- 使用反射机制创建Bean的实例。如果Bean实现了BeanNameAware、BeanFactoryAware等接口，Spring会调用相应的set方法，将Bean的名称、BeanFactory等信息注入给Bean。

### 三、属性注入阶段

1. **依赖注入**：

- Spring容器根据BeanDefinition中的依赖信息，将Bean的依赖注入到Bean实例中。这包括自动注入和手动注入两种方式。

### 四、初始化阶段

1. **BeanPostProcessor的preProcessBeforeInitialization方法**：

- Spring容器调用所有注册的BeanPostProcessor的preProcessBeforeInitialization方法，对Bean进行前置处理。
- **初始化回调**：
- 如果Bean实现了InitializingBean接口，Spring会调用其afterPropertiesSet方法。
- 如果在配置中指定了init-method，Spring会调用该方法。
- 如果Bean的方法上使用了@PostConstruct注解，Spring会在依赖注入完成后调用该方法。
- **BeanPostProcessor的postProcessAfterInitialization方法**：
- Spring容器调用所有注册的BeanPostProcessor的postProcessAfterInitialization方法，对Bean进行后置处理。

### 五、使用阶段

1. **就绪使用**：

- 经过上述步骤后，Bean已经处于就绪状态，可以被应用程序使用。

### 六、销毁阶段

1. **容器关闭时销毁**：

- 当Spring容器关闭时，会触发Bean的销毁过程。
- 如果Bean实现了DisposableBean接口，Spring会调用其destroy方法。
- 如果在配置中指定了destroy-method，Spring会调用该方法。
- 如果Bean的方法上使用了@PreDestroy注解，Spring会在Bean销毁前调用该方法。

### 总结

Spring Bean的生命周期是一个从定义、实例化、属性注入、初始化、使用到销毁的完整过程。Spring容器通过一系列的回调机制和扩展点，为开发者提供了丰富的自定义空间，以便在Bean生命周期的不同阶段执行自定义逻辑。了解和掌握Spring Bean的生命周期，对于开发稳定且高效的Spring应用程序至关重要。

7.Spring中的bean有几种scope?

- singleton: 单例，每一个bean只创建一个对象实例。
- prototype，原型，每次对该bean请求调用都会生成各自的实例。
- request，请求，针对每次HTTP请求都会生成一个新的bean。表示在一次 HTTP 请求内有效。
- session，在一个http session中，一个bean定义对应一个bean实例。
- global session:在一个全局http session中，一个bean定义对应一个bean实例。

8.Spring AOP 之 通知、切面、切点、连接点、织入

通知(**Advice**)：

```
通知是AOP中在切面的某个特定连接点（Join Point）上执行的动作。
它定义了切面“要做什么”以及“何时做”。通知是增强代码的主要手段，用于在目标方法执行前后插入自定义的行为。
类型：
前置通知（Before Advice）：在目标方法执行之前执行。
后置通知（After Advice）：在目标方法执行之后执行，无论方法是否成功执行。
返回通知（After Returning Advice）：在目标方法正常返回后执行，即方法没有抛出异常。
异常通知（After Throwing Advice）：在目标方法抛出异常后执行。
环绕通知（Around Advice）：最强大的通知类型，可以在目标方法执行前后执行自定义行为，甚至可以决定是否执行目标方法。
```

切面（Aspect）

```
切面（Aspect）
定义：
切面是横切关注点的模块化，它封装了跨应用程序多个点的功能。切面可以看作是一个特殊的类，
这个类包含了通知和切点信息，用于定义在哪里（切点）以及如何（通知）执行代码。
切面是AOP的核心，它使得我们可以在不修改源代码的情况下，为系统增加额外的功能。

作用：
切面用于装载切点（Pointcut）和通知（Advice），从而实现在多个类中共同调用的逻辑或责任的封装。
通过切面，我们可以减少系统的重复代码，降低模块之间的耦合度，提高系统的可维护性和可扩展性。
```

连接点（Join point）

```
连接点：连接点是一个虚拟的概念，可以理解为所有满足切点扫描条件的所有的时机。

具体举个例子：比如开车经过一条高速公路，这条高速公路上有很多个出口（连接点），但是我们不会每个出口都会出去，
只会选择我们需要的那个出口（切点）开出去。

简单可以理解为，每个出口都是连接点，但是我们使用的那个出口才是切点。每个应用有多个位置适合织入通知，
这些位置都是连接点。但是只有我们选择的那个具体的位置才是切点。
```

切点（Pointcut）

```
切点是指通知（Advice）所要织入（Weaving）的具体位置。
上面说的连接点的基础上，来定义切入点，你的一个类里，有15个方法，那就有几十个连接点了对把，
但是你并不想在所有方法附近都使用通知（使用叫织入，以后再说），你只想让其中的几个，在调用这几个方法之前，
之后或者抛出异常时干点什么，那么就用切点来定义这几个方法，让切点来筛选连接点，选中那几个你想要的方法。
```

织入 (Weaving)

```
织入是将切面应用到目标对象并创建代理对象的过程。在目标对象的生命周期中，
有多个点可以织入切面，包括编译时、类加载时和运行时。织入过程决定了切面通知何时、何地以及如何被应用。
类型：
编译时织入：在编译时，将切面代码直接插入到目标字节码中。这种方式需要特殊的编译器支持，如AspectJ编译器。
类加载时织入：在类加载到JVM时，通过类加载器来织入切面。这种方式不需要修改源代码，但可能需要特殊的类加载器。
运行时织入：在程序运行时，通过动态代理技术来织入切面。Spring AOP就是采用这种方式，
它通过创建代理对象来拦截目标方法的调用，并在调用前后执行通知逻辑。
```

引入（Introduction）

```
在Spring AOP中，引入（Introduction）是一种特殊类型的通知，它允许开发者向现有的类添加新的接口和实现，
而无需修改原始类的代码。这一特性为扩展现有类功能提供了一种灵活且非侵入式的方式。以下是关于Spring AOP引入的详细解释：
定义与作用
定义：引入允许在不修改原始类代码的情况下，为目标类动态地添加新的方法和属性，即向现有的类引入新的接口及其实现。
作用：
扩展功能：通过引入新的接口和实现，为类增加额外的功能，无需通过继承或修改原类代码。
解耦：将额外的功能与现有类分离，有助于保持代码的清晰和模块化。
灵活性：提高了应用程序设计的灵活性和可扩展性。
```

9.AOP动态代理策略?

```
如果目标对象实现了接口，默认采用JDK 动态代理。可以强制转为CgLib实现AOP。
如果没有实现接口，采用CgLib进行动态代理。
```

10.什么是MVC框架？

```
MVC框架，全称为Model-View-Controller（模型-视图-控制器），是一种软件设计典范，用于以一种业务逻辑、数据、界面显示分离的
方法组织代码。MVC框架的核心思想是将应用程序的逻辑分离成三个主要部分：模型（Model）、视图（View）和控制器（Controller），
以提高代码的可读性、可维护性和可扩展性。

MVC框架的组成部分
模型（Model）：
表示应用程序的核心部分，通常负责处理应用程序的数据逻辑，如数据库访问和业务规则的实现。
它可以包含《数据访问层、业务逻辑层和数据验证层》。
数据访问层负责与数据库进行交互，实现对数据的读写操作。
业务逻辑层负责处理业务逻辑，如用户注册、登录、订单处理等。
数据验证层负责对用户输入的数据进行验证，确保数据的有效性和完整性。
视图（View）：
负责显示数据，是用户看到并与之交互的界面。
视图通常依据模型数据创建，用于将模型中的数据显示在用户界面上，并处理用户的交互事件，如按钮点击、输入框输入等。
随着技术的发展，视图层可以采用多种技术实现，包括HTML、CSS、JavaScript、Adobe Flash以及XHTML、XML/XSL、WML等标识语言。
控制器（Controller）：
负责处理用户请求并调用模型和视图去完成用户的需求。
控制器接收用户请求，根据请求类型调用相应的模型和视图，处理用户请求中的数据，并将处理结果返回给用户。
控制器本身不输出任何东西和做任何处理，它只是接收请求并决定调用哪个模型构件去处理请求，
然后再确定用哪个视图来显示返回的数据。
MVC框架的工作原理
当用户通过视图层发起请求时，控制器接收请求并根据请求类型调用相应的模型处理业务逻辑。模型处理完毕后，将结果返回给控制器，
控制器再调用视图层将结果显示给用户。整个过程中，模型、视图和控制器各自独立工作，通过接口进行通信，实现了业务逻辑、
数据和界面显示的分离。

MVC框架的优势
降低耦合度：模型、视图和控制器之间的分离降低了它们之间的耦合度，使得各个部分可以独立开发和维护。
提高可维护性：由于各个部分职责明确，因此当需要修改应用程序的某个部分时，只需要关注该部分的代码，而不会影响到其他部分。
提高复用性：模型可以被多个视图重用，减少了代码的重复性。
易于扩展：由于各个部分之间的低耦合性，使得应用程序更易于扩展新的功能。
MVC框架的应用场景
MVC框架在Web开发、移动互联开发以及与交互相关的开发等领域都有广泛的应用。随着移动互联网、大数据和云计算的不断发展，
MVC框架也在经历着不断的演化和发展，以满足日益复杂的开发需求。

综上所述，MVC框架是一种非常实用的软件设计模式，它通过分离应用程序的逻辑、数据和界面显示，提高了代码的可读性、
可维护性和可扩展性，广泛应用于各种软件开发领域。
```

11.什么是SpringMVC?

SpringMVC是Spring框架的一个模块。是一个基于MVC的框架。

12.SpringMVC工作流程?

```
SpringMVC的工作流程是Spring框架中用于构建Web应用程序的一个重要部分，它充当了控制层（Controller）的角色，
负责处理客户端的请求并返回相应的响应。以下是SpringMVC工作流程的详细步骤：

一、整体流程
1.用户请求旅程的第一站是DispatcherServlet。
2.收到请求后，DispatcherServlet调用HandlerMapping，获取对应的Handler。
3.如果有拦截器一并返回。
4.拿到Handler后，找到HandlerAdapter,通过它来访问Handler,并执行处理器。
5.执行Handler的逻辑。
6.Handler会返回一个ModelAndView对象给DispatcherServlet。
7.将获得到的ModelAndView对象返回给DispatcherServlet。
8.请求ViewResolver解析视图，根据逻辑视图名解析成真正的View。
9.返回View给DispatcherServlet。
10.DispatcherServlet对View进行渲染视图。
11.DispatcherServlet响应用户。
二、核心组件介绍
DispatcherServlet：前端控制器，负责请求的调度和处理。
HandlerMapping：处理器映射器，根据请求的URL查找对应的处理器（Controller）。
HandlerAdapter：处理器适配器，按照特定规则调用处理器。
Controller：处理器，负责处理具体的业务逻辑，并返回ModelAndView对象。
ViewResolver：视图解析器，根据逻辑视图名解析出真正的视图对象。
View：视图对象，负责渲染数据并生成最终的响应结果。
三、配置与开发
在Spring MVC项目中，通常需要进行以下配置和开发工作：

配置DispatcherServlet：在web.xml中配置DispatcherServlet，并指定其加载的配置文件。
配置HandlerMapping和HandlerAdapter：这些组件通常不需要显式配置，因为Spring MVC提供了默认的实现。
但是，在某些情况下，你可能需要自定义这些组件的行为。
开发Controller：编写处理请求的Controller类，并使用@RequestMapping等注解来映射请求。
开发视图：编写JSP、Thymeleaf、FreeMarker等模板文件，作为视图对象来渲染数据。
通过以上步骤，你可以构建出基于Spring MVC的Web应用程序，并处理客户端的请求和响应。
```

13.为什么需要 Handler、HandlerMapping和HandlerAdapter？

**那么 HandlerAdapter 的作用呢？其实是为了使 DispatcherServlet 不关注具体的 Handler。**

```
1.日常的开发中，我们需要针对 HttpServeletRequest 做各种处理，我们的处理逻辑，其实就是一个 Handler。
2.有了Handler 之后，我们还需要配置什么时候，DispatcherServlet 需要去使用这些 Handler 处理 HttpServeletRequest，
HandlerMapping 就是提供了url 和 Handler 的映射。这样，DispatcherServlet 可以通过 HandlerMapping 获取到对应的 Handler ，
并执行 Handler 中，我们定义的业务逻辑。
3.那么 HandlerAdapter 的作用呢？其实是为了使 DispatcherServlet 不关注具体的 Handler。考虑下面的场景，
@RequestMapping 修饰的方法和实现Controller 接口的类，其实都是 Handler，这两种 Handler 有很大不同，
那么如何使得 DispatcherServlet 可以直接调用 Handler 的逻辑，而不需要关注 Handler的形态呢？这个时候可以使用「适配器模式」，
为各种 Handler 提供不同的 HanderAdapter，DispatcherServlet 只需要调用 HandlerAdapter 的方法即可，
不需要关注 HandlerAdapter 是如何通过各种形态的 Handler 去执行业务逻辑的。
日常开发中，我们一般只需要配置 Handler 和 HandlerMapping，因为 springmvc 已经提供了常用的 HandlerAdapter，
一般情况下，我们不需要指定 HandlerAdapter（除非需要自定义 Handler 的执行）。
```

![img](https://picx.zhimg.com/80/v2-4e9417b8988e9140e45f891bc8cc8fa3_1440w.png?source=d16d100b)





HandlerAdapter适配多种handler

14.单例bean是线程安全的吗？

在默认情况下，如果bean是无状态的，那么它就是线程安全的。如果bean是有状态的，那么就需要采取额外的措施来确保线程安全

```
单例bean的线程安全性并不是一个绝对的结论，它取决于多个因素。

一、单例bean的默认情况
在Spring框架中，单例bean默认是线程安全的。这主要是因为Spring容器在处理单例bean时，会使用一个线程安全的实例工厂，
确保每次只有一个线程能够获取到这个单例bean的实例。然而，这种线程安全性是基于bean本身无状态的前提下的。
如果单例bean是无状态的，即它的方法不会修改对象的任何状态（成员变量），那么它就是线程安全的。

二、有状态单例bean的线程安全性
如果单例bean是有状态的，即它的成员变量可能在不同线程的执行过程中被修改，那么这种bean就不是线程安全的。
在这种情况下，如果多个线程同时访问并修改这个bean的状态，就可能导致数据不一致或其他并发问题。

三、如何保证有状态单例bean的线程安全性
1.避免在单例bean中使用状态变量：最简单的办法是在单例bean中避免使用任何状态变量，即确保bean是无状态的。
2.使用同步机制：如果必须在单例bean中使用状态变量，那么可以使用Java的同步机制（如synchronized关键字或Lock接口）
来确保线程安全。但是，这可能会降低程序的性能。
3.改变bean的作用域：将单例bean的作用域从singleton改为prototype，这样每次请求都会创建一个新的bean实例，
从而避免线程之间的状态共享问题。但是，这会增加系统的内存消耗和GC压力。
4.使用ThreadLocal：对于某些需要线程隔离的变量，可以使用ThreadLocal来封装，这样每个线程都会拥有自己的变量副本，
从而避免线程之间的干扰。
四、结论
综上所述，单例bean的线程安全性取决于bean本身的状态以及使用方式。在默认情况下，如果bean是无状态的，那么它就是线程安全的。
如果bean是有状态的，那么就需要采取额外的措施来确保线程安全。因此，在设计和使用单例bean时，需要仔细考虑其线程安全性问题。
```

15.自动装配有几种方式？分别是？

```
Spring 优先 byType  --> byName 
no - 默认设置，表示没有自动装配。
byName ： 根据名称装配。
byType ： 根据类型装配。
constructor ： 把与Bean的构造器入参具有相同类型的其他Bean自动装配到Bean构造器的对应入参中。
autodetect ：先尝试constructor装配，失败再尝试byType方式。
default：由上级标签的default-autowire属性确定。
```

16.说几个声明Bean 的注解？

```
@Component
@Service
@Repository
@Controller
@ResponseBody + @Controller =@RestController
其他
接收路径参数用哪个注解？ @PathVariable
用来标记缓存查询。@Cacheable 清空缓存是哪个注解 @CacheEvict
@Component注解  泛指组件，不好归类时，可以用它。
@Transaction 事务的注解
@Configuration注解 在Spring框架中扮演着至关重要的角色，它使得开发者能够以更加灵活和强大的方式来配置和管理Spring容器中的Bean
```

17.「BeanFactory 和 ApplicationContext」区别？

```
BeanFactory 和 ApplicationContext 是 Spring 框架中的两个核心接口，它们在功能和使用场景上存在明显的区别。以下是它们之间的主要区别：

1. 功能范围
BeanFactory：
是 Spring 框架中最底层的接口，是控制反转（IOC）的核心。
提供了最基本的容器功能，如Bean的定义、加载、实例化、依赖注入和生命周期管理等。
主要面向Spring本身，是IOC最基本的功能体现。
ApplicationContext：
是 BeanFactory 的子类，继承了 BeanFactory 的所有功能。
提供了更全面的框架功能，包括国际化支持、资源访问、事件传播、自动装配以及更高级的容器特性等。
主要面向使用Spring框架的开发者，使用的“场合”较多。
2. 加载机制
BeanFactory：
采用延时加载（Lazy Initialization）机制。
在容器启动时不会立即实例化Bean，而是在需要使用Bean时，才会对该Bean进行加载和实例化。
这种机制有利于节省启动时的资源，但可能在第一次访问Bean时增加响应时间。
ApplicationContext：
采用预加载（Eager Initialization）机制。
在容器启动时，会一次性创建并初始化所有的Bean（除非明确指定为延迟加载）。
这种机制使得容器启动后，Bean的访问速度更快，同时也能在启动时发现配置问题。
3. 异常检测
BeanFactory：
由于是延时加载，所以在容器启动时无法提前发现配置问题。
只有在第一次调用getBean()方法时，才可能因配置问题而抛出异常。
ApplicationContext：
由于是预加载，所以可以在容器启动时就发现配置问题。
这有助于开发者及时发现并修复问题，避免在运行时出现意外情况。
4. 国际化支持
BeanFactory：
不直接支持国际化。
ApplicationContext：
通过实现MessageSource接口，提供了对国际化消息资源的支持。
5. 资源访问
BeanFactory：
在资源访问方面功能较弱。
ApplicationContext：
提供了对资源的访问能力，如文件、URL等。
通过Resource接口和ResourceLoader接口实现，使得访问外部资源变得简单。
6. 创建方式
BeanFactory：
通常以编程的方式创建，如使用XmlBeanFactory类。
ApplicationContext：
通常以声明的方式创建，如使用ClassPathXmlApplicationContext、FileSystemXmlApplicationContext等类。
7. 高级功能
ApplicationContext 还支持自动装配、环境抽象（包括配置文件和程序化配置）、Web支持（通过WebApplicationContext接口）等高级功能。
综上所述，BeanFactory和ApplicationContext在功能范围、加载机制、异常检测、国际化支持、资源访问、创建方式以及高级功能等方面存在明显的区别。开发者在选择使用哪个接口时，应根据具体的应用场景和需求进行考虑。
```

18.Spring事务实现方式有？

```
Spring事务实现方式主要包括以下几种：

1. 声明式事务管理
基于@Transactional注解的声明式事务管理：这是Spring推荐的方式，通过在Service层方法上添加@Transactional注解来声明事务。这种方式使事务管理与业务代码分离，降低了代码的耦合度，使得业务代码更加简洁和易于维护。Spring容器在运行时会自动为添加了@Transactional注解的方法创建代理，并在代理中处理事务的开启、提交或回滚。
基于TransactionProxyFactoryBean的声明式事务管理：这是Spring早期提供的一种声明式事务管理方式，通过在Spring配置文件中使用TransactionProxyFactoryBean来为目标Bean创建事务代理。不过，随着Spring的发展，这种方式已经逐渐被基于@Transactional注解的方式所取代。
基于AspectJ AOP配置事务：这也是一种声明式事务管理方式，它利用AspectJ的强大AOP功能来配置事务。通过在AspectJ切面中定义事务通知（advice），并在业务方法执行前后进行事务的开启、提交或回滚等操作。这种方式需要引入AspectJ的依赖，并编写相应的AspectJ切面代码。
2. 编程式事务管理
使用TransactionTemplate：TransactionTemplate是Spring提供的一个用于编程式事务管理的工具类。它封装了事务的开启、提交和回滚等操作，使得开发者可以通过回调接口（TransactionCallback或TransactionCallbackWithoutResult）来编写需要事务管理的业务代码。这种方式相比于直接使用PlatformTransactionManager接口更加简便和直观。
使用PlatformTransactionManager：PlatformTransactionManager是Spring事务管理的核心接口，它提供了事务的开启、提交、回滚以及获取事务状态等方法。开发者可以通过实现该接口或使用其实现类（如DataSourceTransactionManager）来手动控制事务的执行过程。这种方式虽然提供了更高的灵活性，但也会增加代码的复杂度和维护难度。
3. 本地事务管理
Spring还提供了一种本地事务管理的机制，通过Spring的JdbcTemplate类和DataSourceWrapper类来实现。这种方式适用于对数据库操作的支持，开发者可以通过编程方式手动开启、提交或回滚本地事务。不过，在分布式系统或微服务架构中，这种方式可能无法满足跨多个服务或数据源的事务管理需求。
4. 基于JTA的事务管理
如果需要与多个数据库或其他事务系统进行交互，可以使用基于JTA（Java Transaction API）的事务管理。Spring提供了一个JtaTransactionManager类来实现基于JTA的事务管理。这种方式适用于需要跨多个系统进行事务管理的场景，如分布式事务管理。
综上所述，Spring提供了多种事务实现方式，开发者可以根据具体的业务需求和系统架构来选择适合的方式。在实际应用中，声明式事务管理因其简便性和易用性而广受欢迎，而编程式事务管理则适用于需要更高灵活性和控制能力的场景。
```

19.Spring事务传播行为有哪些？

```
Spring事务传播行为定义了在一个事务方法被另一个事务方法调用时，事务应该如何进行传播。Spring框架提供了七种事务传播行为，它们分别是：

PROPAGATION_REQUIRED（默认）：
如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。
示例：如果方法A调用方法B，且两者都使用PROPAGATION_REQUIRED，则它们会共享同一个事务。
PROPAGATION_REQUIRES_NEW：
创建一个新的事务，如果当前存在事务，则把当前事务挂起。
示例：如果方法A调用方法B，且方法B使用PROPAGATION_REQUIRES_NEW，则方法B会在一个新的事务中执行，与方法A的事务无关。
PROPAGATION_SUPPORTS：
如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务方式执行。
示例：如果方法A调用方法B，且方法B使用PROPAGATION_SUPPORTS，那么如果方法A在事务中，方法B也将在同一个事务中执行；如果方法A不在事务中，方法B将以非事务方式执行。
PROPAGATION_MANDATORY：
如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
示例：如果方法A调用方法B，且方法B使用PROPAGATION_MANDATORY，但方法A没有事务，则方法B会抛出异常。
PROPAGATION_NOT_SUPPORTED：
以非事务方式执行操作，如果当前存在事务，则把当前事务挂起。
示例：如果方法A调用方法B，且方法B使用PROPAGATION_NOT_SUPPORTED，则无论方法A是否在事务中，方法B都将以非事务方式执行。
PROPAGATION_NEVER：
以非事务方式执行，如果当前存在事务，则抛出异常。
示例：如果方法A调用方法B，且方法B使用PROPAGATION_NEVER，但方法A在事务中，则方法B会抛出异常。
PROPAGATION_NESTED：
如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该行为等同于PROPAGATION_REQUIRED。
示例：如果方法A调用方法B，且方法B使用PROPAGATION_NESTED，则方法B会在方法A的事务中作为一个嵌套事务执行。嵌套事务可以拥有自己的保存点（savepoint），这意味着它可以独立于外部事务进行提交或回滚。
每种传播行为都有其特定的使用场景和优缺点，开发者在实际应用中应根据具体需求选择合适的事务传播行为。例如，当需要确保多个操作在同一个事务中执行时，可以选择PROPAGATION_REQUIRED；当需要确保某个操作不受外部事务影响时，可以选择PROPAGATION_REQUIRES_NEW等。
 
```

20.Spring事务失效的场景有哪些？

```
Spring事务失效的场景多种多样，以下是一些常见的场景及其原因分析：

1. 访问权限问题
非public方法：如果事务方法不是public的，而是protected、private或默认（包访问权限），那么Spring将无法通过动态代理机制正确代理该方法，从而导致事务失效。因为Spring的事务管理是通过AOP（面向切面编程）实现的，而AOP是基于动态代理的，动态代理只能代理公共方法。
2. final方法
final修饰的方法：同样地，由于Spring事务管理依赖于AOP和动态代理，如果方法被final修饰，那么在代理类中就无法重写该方法，从而无法添加事务功能，导致事务失效。
3. 静态方法
静态方法：静态方法也无法通过动态代理进行事务管理，因为静态方法属于类，而不是类的实例，所以无法被Spring的AOP机制拦截。
4. 方法内部调用
同一个类中事务方法的内部调用：如果在同一个类中，一个事务方法内部调用了另一个事务方法，由于这种调用是直接通过this关键字进行的，而不是通过代理对象进行的，所以被调用的方法不会经过AOP的拦截，从而导致事务失效。
5. 异常处理不当
未捕获的运行时异常：默认情况下，Spring事务管理只会在捕获到受检查异常（checked exception）时回滚事务，如果事务方法中抛出了未捕获的运行时异常（unchecked exception），并且该异常没有通过@Transactional注解的rollbackFor属性指定为回滚条件，那么事务将不会回滚。
6. 数据库或配置问题
不支持事务的存储引擎：如果数据库表使用了不支持事务的存储引擎（如MyISAM），那么在该表上执行的操作将无法参与事务。
跨多个数据源操作：在分布式系统中，如果事务跨越了多个数据源，而系统没有配置分布式事务管理器，那么事务可能无法正确提交或回滚。
数据库连接配置不正确：如果数据库连接配置不正确，或者连接池没有正确配置事务支持，也可能导致事务失效。
7. 其他场景
DDL操作：某些数据库不支持DDL（数据定义语言）操作（如CREATE TABLE、ALTER TABLE等）的回滚，如果事务中包含了DDL操作，
并且这些操作无法回滚，那么整个事务可能会受到影响。
线程问题：如果事务方法在多线程环境下被调用，并且事务的边界没有正确管理（例如，每个线程都应该有自己独立的事务），
那么可能会导致事务失效或数据不一致。
针对上述场景，可以采取相应的解决措施来避免Spring事务失效。例如，确保事务方法的访问权限为public，
避免使用final和static修饰事务方法，通过代理对象进行方法调用，正确处理异常，配置正确的数据库和连接池等。
同时，也需要注意在设计和实现分布式系统时，合理配置和使用分布式事务管理器来确保事务的一致性和完整性。
```

21.AOP代理对象实现的方法？

JDK动态代理和CGLIB代理

```
JDK动态代理
实现步骤：

/**定义接口：首先定义一个或多个接口，这些接口将被目标类实现，并且也将被代理类实现。
实现InvocationHandler：创建一个实现了java.lang.reflect.InvocationHandler接口的类，这个类将包含实际的代理逻辑。
在invoke方法中，你可以调用目标对象的方法，并在调用前后添加自定义的逻辑（如日志记录、安全检查等）。
创建代理对象：使用java.lang.reflect.Proxy类的newProxyInstance方法创建代理对象。
这个方法需要三个参数：类加载器（用于加载代理类）、接口数组（代理类将实现的接口列表）和InvocationHandler实例
（包含代理逻辑）。**/
import java.lang.reflect.InvocationHandler;  
import java.lang.reflect.Method;  
import java.lang.reflect.Proxy;  
  
interface MyInterface {  
    void doSomething();  
}  
  
class MyTarget implements MyInterface {  
    @Override  
    public void doSomething() {  
        System.out.println("Doing something...");  
    }  
}  
  
class MyInvocationHandler implements InvocationHandler {  
    private final Object target;  
  
    public MyInvocationHandler(Object target) {  
        this.target = target;  
    }  
  
    @Override  
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {  
        System.out.println("Before method call");  
        Object result = method.invoke(target, args);  
        System.out.println("After method call");  
        return result;  
    }  
}  
  
public class Main {  
    public static void main(String[] args) {  
        MyTarget target = new MyTarget();  
        MyInterface proxy = (MyInterface) Proxy.newProxyInstance(  
                MyTarget.class.getClassLoader(),  
                new Class[]{MyInterface.class},  
                new MyInvocationHandler(target)  
        );  
        proxy.doSomething();  
    }  
}
```

CGLIB代理

```
/** 实现步骤：
添加CGLIB依赖：在你的项目中添加CGLIB库的依赖。
创建MethodInterceptor：实现net.sf.cglib.proxy.MethodInterceptor接口，这个接口类似于JDK的InvocationHandler，
但提供了更灵活的方法拦截机制。
创建代理对象：使用CGLIB提供的Enhancer类来创建代理对象。你需要设置Enhancer的setSuperclass（目标类的Class对象）
和setCallback（MethodInterceptor实例）。
**/

import net.sf.cglib.proxy.Enhancer;  
import net.sf.cglib.proxy.MethodInterceptor;  
import net.sf.cglib.proxy.MethodProxy;  
  
class MyTarget {  
    public void doSomething() {  
        System.out.println("Doing something...");  
    }  
}  
  
class MyMethodInterceptor implements MethodInterceptor {  
    @Override  
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {  
        System.out.println("Before method call");  
        Object result = proxy.invokeSuper(obj, args);  
        System.out.println("After method call");  
        return result;  
    }  
}  
  
public class Main {  
    public static void main(String[] args) {  
        Enhancer enhancer = new Enhancer();  
        enhancer.setSuperclass(MyTarget.class);  
        enhancer.setCallback(new MyMethodInterceptor());  
        MyTarget proxy = (MyTarget) enhancer.create();  
        proxy.doSomething();  
    }  
}
```

**注意**：在实际项目中，如果你正在使用Spring框架，那么Spring AOP会根据你的配置和类结构自动选择使用JDK动态代理还是CGLIB代理。通常，如果目标类实现了接口，Spring会优先使用JDK动态代理

22.spring在加载过程中bean有哪几种形态?

```
在Spring的加载过程中，Bean会经历不同的形态或阶段，这些阶段反映了Bean从定义到最终实例化的整个过程。以下是Spring加载过程中Bean可能经历的主要形态或阶段：

定义阶段（Bean Definition）：
在这个阶段，Spring容器通过读取配置文件（如XML文件）或注解，识别并收集Bean的定义信息。这些信息被封装成BeanDefinition对象，这些对象包含了Bean的元数据，如类名、作用域、生命周期回调等。
这些BeanDefinition对象随后被注册到BeanDefinitionRegistry中，供后续阶段使用。
注册阶段（Bean Registration）：
在这个阶段，BeanDefinition对象被注册到Spring容器中。这通常发生在Spring容器启动时，容器会扫描配置文件或注解，并将找到的Bean定义注册到内部的注册表中。
解析阶段（Bean Resolution）：
严格来说，解析阶段并不是Bean形态的直接体现，但它是Bean实例化之前的一个重要步骤。在这个阶段，Spring容器会根据BeanDefinition对象中的信息，解析Bean的依赖关系和其他属性。
实例化阶段（Bean Instantiation）：
在这个阶段，Spring容器会根据BeanDefinition对象中的类名信息，使用反射机制创建Bean的实例。这个实例是一个普通的Java对象，但尚未进行属性注入和其他初始化操作。
属性填充阶段（Property Population）：
也称为依赖注入阶段。在这个阶段，Spring容器会将Bean的依赖项（即其他Bean）注入到当前Bean的相应属性中。这通常通过Setter方法、构造函数注入或字段注入等方式实现。
初始化阶段（Bean Initialization）：
在这个阶段，Spring容器会调用Bean的初始化方法（如实现了InitializingBean接口的afterPropertiesSet方法，或自定义的初始化方法上标注了@PostConstruct注解）。这个阶段允许Bean在正式使用之前进行必要的初始化操作。
就绪阶段（Bean Ready）：
经过上述阶段后，Bean已经完成了从定义到实例化的全过程，并且已经通过了初始化阶段的检查。此时，Bean已经准备好被应用程序使用。
销毁阶段（Bean Destruction）：
虽然这个阶段不直接属于Bean的加载过程，但它是Bean生命周期的一个重要组成部分。当Spring容器关闭时，它会调用Bean的销毁方法（如实现了DisposableBean接口的destroy方法，或在自定义的销毁方法上标注了@PreDestroy注解），以释放Bean占用的资源。
需要注意的是，上述阶段中的某些阶段（如解析阶段）可能并不直接对应Bean的某个具体形态，而是描述了Bean在加载和实例化过程中经历的一个过程或步骤。然而，这些阶段共同构成了Spring加载Bean的完整过程，从Bean的定义开始，直到Bean准备就绪供应用程序使用。
```

23.Spring为什么要用三级缓存解决循环依赖？

```
Spring使用三级缓存解决循环依赖的问题，主要是出于以下几个方面的考虑：

一、循环依赖问题的本质
循环依赖指的是两个或多个Bean之间相互依赖，形成一个闭环。在Spring中，如果两个Bean A和B相互依赖，即A的某个属性是B，而B的某个属性又是A，那么在传统的单例Bean管理模式下，如果没有特殊处理，就会导致死循环，因为每个Bean都在等待对方初始化完成以便进行属性注入。

二、三级缓存的作用
Spring的三级缓存机制主要用于解决单例Bean之间的循环依赖问题，同时支持AOP（面向切面编程）代理的创建。三级缓存分别是：

一级缓存（Singleton Objects）：用于存放完全初始化好的Bean实例，这些Bean可以直接被应用程序使用。
二级缓存（Early Singleton Objects）：用于存放提前曝光的Bean实例，这些Bean实例可能还未完全初始化
（例如，AOP代理可能尚未生成），但它们已经可以用于解决循环依赖问题。
三级缓存（Singleton Factories）：用于存放ObjectFactory对象，这些ObjectFactory能够生成Bean实例或AOP代理实例。
当存在循环依赖时，Spring会通过这些ObjectFactory来提前获取Bean的代理引用，从而解决循环依赖问题。
三、三级缓存解决循环依赖的原理
实例化：Spring首先通过反射机制实例化Bean。
属性填充前的准备：在填充Bean的属性之前，Spring会将该Bean的ObjectFactory放入三级缓存中。这个ObjectFactory能够生成Bean的
实例或代理实例。
属性填充：当Spring为Bean填充属性时，如果发现该属性依赖于另一个尚未初始化的Bean（即存在循环依赖），它会尝试从缓存中获取
该Bean的引用。首先，它会尝试从一级缓存中获取；如果一级缓存中没有，它会尝试从二级缓存中获取；如果二级缓存中也没有，
并且允许从三级缓存中获取（即允许早期引用），那么它会从三级缓存中获取ObjectFactory，
并通过调用ObjectFactory的getObject方法来获取Bean的引用（可能是原始Bean实例或AOP代理实例）。
解决循环依赖：通过这种方式，即使存在循环依赖，Spring也能够通过三级缓存中的ObjectFactory来提前获取Bean的引用，
从而避免死循环。当Bean的所有属性都被填充后，Spring会将其从三级缓存中移除，并放入二级缓存中（如果还没有AOP代理的话），
最终在完成初始化后将其放入一级缓存中。
四、总结
Spring使用三级缓存解决循环依赖问题，主要是通过在Bean实例化后、属性填充前将ObjectFactory放入三级缓存中，
并在需要时通过ObjectFactory来提前获取Bean的引用（可能是原始Bean实例或AOP代理实例）。这种方式既能够解决循环依赖问题，
又能够支持AOP代理的创建。


一、二级缓存的局限性
虽然二级缓存可以在一定程度上解决Bean的循环依赖问题，但它存在一些局限性：

早期暴露问题：二级缓存通常用于存储提前暴露的、尚未填充完属性的Bean实例。然而，在Bean的创建过程中，可能还需要一些额外的
信息来确保Bean的正确初始化，比如Bean的属性值、依赖关系等。这些信息在Bean完全初始化之前是无法确定的。
如果仅仅使用二级缓存，那么在解决循环依赖时，可能会遇到Bean属性还未完全注入就被其他Bean引用的情况，导致不可预期的行为。
依赖注入不完整：在Bean的创建过程中，可能需要执行一些额外的操作，如依赖注入、AOP代理等。如果仅仅使用二级缓存，
那么在Bean完全初始化之前，这些操作可能还没有完成，导致依赖注入不完整。
```

24.@Async为什么会导致循环依赖解决不了?

```
@Async注解在Spring框架中用于声明一个方法为异步方法，该方法会在一个单独的线程中执行，以实现非阻塞的并发处理。
然而，当在存在循环依赖的Bean上使用@Async注解时，可能会遇到无法解决的循环依赖问题。
这主要是因为@Async注解的处理方式与Spring的依赖注入和循环依赖解决机制之间存在冲突。
@Async导致循环依赖问题的原因
代理对象的创建：
当在Bean的方法上添加@Async注解时，Spring会为该Bean创建一个代理对象，以便在调用被@Async注解的方法时，能够将其异步执行。
这个代理对象的创建过程可能依赖于其他尚未初始化的Bean，特别是当存在循环依赖时。
循环依赖的解决机制：
Spring通过三级缓存机制（singletonObjects、earlySingletonObjects、singletonFactories）来解决单例Bean之间的循环依赖问题。
在没有@Async等特殊情况下，如果Bean A依赖于Bean B，而Bean B又依赖于Bean A，Spring会在Bean A实例化后、属性填充前，将其暴露到三级缓存中。这样，当Bean B在创建过程中需要Bean A时，可以从三级缓存中获取到Bean A的早期引用（虽然此时Bean A的属性可能还未填充）。
@Async与循环依赖的冲突：
当Bean A中的方法被@Async注解时，Spring需要为Bean A创建一个代理对象来处理异步方法调用。
如果Bean A和Bean B之间存在循环依赖，那么在Bean A的代理对象创建过程中，可能需要注入Bean B。但是，由于Bean B也依赖于Bean A（可能是Bean A的原始实例或代理实例，具体取决于依赖注入的时机和方式），这就导致了冲突。
如果Spring尝试在Bean A的代理对象创建过程中注入Bean B的原始实例，而Bean B又需要Bean A的代理实例（因为Bean A的方法被@Async注解了），这就形成了无法解决的循环依赖。
解决方案
重新设计代码：
尽量避免在存在循环依赖的Bean上使用@Async注解。
可以通过重构代码，将异步方法移动到不参与循环依赖的Bean中，或者使用其他设计模式来避免循环依赖。
使用@Lazy注解：
在依赖注入时使用@Lazy注解，以延迟加载依赖对象，从而规避循环依赖。但需要注意的是，这可能会影响到应用的启动性能和首次请求的性能。
使用ApplicationContext获取Bean：
在需要调用异步方法的地方，通过ApplicationContext来动态获取Bean的实例，而不是在初始化时注入。这种方式虽然可以解决循环依赖问题，但可能会增加代码的复杂性和维护难度。
手动管理异步执行：
考虑不使用@Async注解，而是手动创建线程或使用线程池来管理异步任务的执行。这样可以更灵活地控制异步任务的执行时机和依赖关系。
总之，@Async注解在Spring中用于实现异步方法调用，但在处理循环依赖时可能会遇到问题。为了避免这些问题，开发者需要仔细设计代码结构，并采取适当的措施来避免循环依赖或管理异步任务的执行。
```

@Async注解对循环依赖解决的影响

```
@Async注解与循环依赖的交互问题
动态代理与循环依赖：
@Async注解是通过动态代理实现的，由AsyncAnnotationBeanPostProcessor类负责处理。这意味着，当一个方法被@Async注解标记时，
Spring会为该bean创建一个代理对象，以便能够拦截对该方法的调用并将其异步执行。
在处理循环依赖时，Spring依赖于其三级缓存机制。这通常是通过AnnotationAwareAspectJAutoProxyCreator
（或类似的BeanPostProcessor）在早期暴露对象时生成代理对象来解决的。然而，当@Async注解与循环依赖同时存在时，可能会出现问题。
问题现象：
当@Async注解与循环依赖相遇时，可能会出现BeanCurrentlyInCreationException异常。这是因为AnnotationAwareAspectJAutoProxyCreator
先处理循环依赖，生成早期暴露对象（可能是代理对象）。随后，AsyncAnnotationBeanPostProcessor再次处理@Async注解，
给早期暴露对象再次生成代理。这导致最终生成的对象与早期暴露的对象不一致，从而触发异常。
```

25.Spring如何避免在并发情况下获取不完整的 Bean?

```
Spring框架在并发情况下避免获取不完整的Bean主要依赖于其内部的管理机制，特别是与Bean的生命周期和缓存策略相关的机制。
以下是一些关键措施，用于确保在并发环境下Bean的完整性和可用性：

1. 双重检查锁机制
双重检查锁（Double-Check Locking）是一种在Java中用于延迟初始化实例字段的线程安全方法。在Spring中，
这种机制被用于确保在并发情况下Bean的创建和初始化过程不被打断，从而避免获取到不完整的Bean。

具体步骤如下：

第一次检查：在获取Bean之前，首先检查该Bean是否已经被创建并存储在缓存中（如一级缓存）。
加锁：如果Bean尚未被创建，则通过加锁来确保在同一时刻只有一个线程能够进入创建过程。
第二次检查：在加锁后，再次检查Bean是否已经被其他线程创建（避免重复创建）。
创建Bean：如果Bean确实未被创建，则执行Bean的创建和初始化过程。
存储Bean：将创建好的Bean存储到缓存中，以便后续快速访问。
2. 缓存管理
Spring通过多级缓存（如一级缓存、二级缓存、三级缓存）来管理Bean的创建和初始化过程。
这些缓存机制有助于减少重复创建Bean的开销，并在并发情况下确保Bean的完整性和可用性。

一级缓存：通常存储已经完全初始化并可供使用的Bean实例。
二级缓存：可能用于存储一些中间状态的Bean，如正在创建但尚未完成初始化的Bean。
三级缓存：用于存储一些早期引用或代理对象，这些对象可能在Bean的完整创建和初始化过程中被使用。
3. 同步策略
在Bean的创建和初始化过程中，Spring采用了多种同步策略来确保线程安全。这些策略可能包括使用synchronized关键字、
ReentrantLock等并发工具，以及利用Java内存模型（JMM）的特性来确保数据的一致性和可见性。

4. Bean的生命周期管理
Spring框架严格管理Bean的生命周期，包括Bean的实例化、属性赋值、初始化（如调用初始化方法、执行AOP增强等）、
使用以及销毁等阶段。这些生命周期管理策略有助于确保Bean在每个阶段都保持完整和可用，从而避免在并发情况下出现不完整Bean的问题。

5. 异步处理
虽然异步处理本身并不直接解决并发情况下获取不完整Bean的问题，但Spring提供了@Async注解等机制来支持异步方法的执行。
通过将这些耗时的操作放在异步线程中执行，可以减少对主线程的影响，并避免在并发情况下因长时间占用资源而导致的问题。

综上所述，Spring通过双重检查锁机制、缓存管理、同步策略、Bean的生命周期管理以及异步处理等多种机制来确保在并发情况下
能够获取到完整的Bean。这些机制共同作用，为Spring应用提供了高效、可靠的并发处理能力。
```

26.如何在Spring所有 Bean创建完后做扩展?

1.使用ApplicationContextInitializer 

```
ApplicationContextInitializer接口允许你在ApplicationContext被刷新之前进行定制，这通常是在Spring Boot应用中配置外部源
（如命令行参数、配置文件）之前进行的。但如果你只是需要在所有Bean加载完成后执行操作，这可能不是最直接的方法。
```

2.实现ApplicationListener<ContextRefreshedEvent>

这是最常用的方法之一。Spring的ApplicationEvent和ApplicationListener机制允许你在应用程序的上下文中发布和监听事件。ContextRefreshedEvent事件是在ApplicationContext被初始化或刷新时触发的，这通常意味着所有的Bean都已经被加载并可用。 

```
 @Component  
public class MyApplicationListener implements ApplicationListener<ContextRefreshedEvent> {  
  
    @Override  
    public void onApplicationEvent(ContextRefreshedEvent event) {  
        // 在这里，所有的Bean都已经加载完毕  
        // 执行你的扩展逻辑  
    }  
}
```

3.使用@PostConstruct注解

虽然@PostConstruct注解通常用于在依赖注入完成后初始化Bean，但如果你有一个特定的Bean需要在所有其他Bean之后初始化，你可以在该Bean上使用@DependsOn注解来指定它依赖于其他Bean，然后在该Bean的@PostConstruct方法中执行你的逻辑。但这种方法并不完美，因为它依赖于特定的Bean顺序，而不是全局的Bean加载完成。

```
@Component  
@DependsOn({"bean1", "bean2"}) // 确保这些Bean先被初始化  
public class MyInitializer {  
  
    @PostConstruct  
    public void init() {  
        // 当指定的Bean（如bean1, bean2）初始化完成后执行  
        // 注意：这并不保证所有Bean都已加载完成  
    }  
}
```

4. 自定义BeanFactoryPostProcessor

BeanFactoryPostProcessor接口允许你在Bean工厂中的Bean被实例化之前，对Bean工厂的配置元数据进行修改。这不是在所有Bean加载完成后执行操作的最佳方法，但它是修改Bean定义或在Bean实例化之前执行逻辑的一种方式。

### 结论

对于大多数需求，实现ApplicationListener<ContextRefreshedEvent>是最简单且直接的方法，因为它在所有的Bean都加载并初始化完成后触发。如果你需要更复杂的控制或初始化逻辑，可能需要结合使用多种方法。

27.**Spring容器启动时 为什么先加载BeanFactoryPostProcess？**

```
1. BeanFactoryPostProcessor的作用
BeanFactoryPostProcessor是Spring框架中的一个高级接口，允许在容器实例化任何bean之前，对Spring IoC容器的BeanFactory进行
后处理。这提供了对BeanDefinition的修改或注册新的BeanDefinition的机会。简而言之，它允许用户在容器完成BeanDefinition的
注册之后，但在bean的实例化之前，对容器进行一些自定义的扩展或修改。
2. 加载BeanFactoryPostProcessor的时机
在Spring容器的启动过程中，BeanDefinition的注册是第一步。这些BeanDefinition描述了容器中将要管理的bean的元数据。
一旦所有的BeanDefinition都被注册到容器中，Spring就会开始考虑是否有必要对这些BeanDefinition进行后处理。这时，
BeanFactoryPostProcessor就派上了用场。
3. 为什么先加载BeanFactoryPostProcessor
扩展性：BeanFactoryPostProcessor提供了一种机制，允许开发者在容器加载bean之前对容器进行自定义的扩展。
这种机制增强了Spring容器的灵活性和可扩展性。
依赖关系：由于BeanFactoryPostProcessor可能在容器中注册新的BeanDefinition或修改现有的BeanDefinition，
因此它必须在容器开始实例化bean之前被加载和执行。这样可以确保在bean实例化时，所有的BeanDefinition都是最新的、经过后处理的。
容器初始化流程：Spring容器的初始化流程是精心设计的，以确保在正确的时机执行正确的操作。
先加载BeanFactoryPostProcessor是这一流程中的一个重要环节，它确保了容器在实例化bean之前能够完成必要的准备工作。
4. 结论
综上所述，Spring容器启动时先加载BeanFactoryPostProcessor是因为它在容器生命周期中扮演着重要的角色，允许在bean实例化之前
对容器进行自定义的扩展或修改。这种机制增强了Spring容器的灵活性和可扩展性，并确保了容器在实例化bean时能够使用到最新的、
经过后处理的BeanDefinition。
```

28.事务四大特性

```
事务的四大特性，通常简称为ACID，是数据库管理系统（DBMS）中事务处理的基础原则，确保了数据的一致性和可靠性。这四大特性分别是：

1. 原子性（Atomicity）
定义：事务被视为一个不可分割的工作单元，事务中的所有操作要么全部成功，要么全部失败。如果事务中的任何操作失败，则整个事务
将被回滚到事务开始前的状态，就像没有执行过该事务一样。
作用：保证了数据的完整性和一致性，防止了部分完成的事务对数据库状态造成破坏。
2. 一致性（Consistency）
定义：事务必须使数据库从一个一致性状态转换到另一个一致性状态。在事务开始之前和事务结束之后，数据库的完整性约束没有被破坏。
作用：确保数据库的规则和约束在事务执行过程中始终得到满足，例如，在转账过程中，无论转账操作如何执行，两个账户的总金额应保持不变。
3. 隔离性（Isolation）
定义：并发执行的事务之间是相互隔离的，一个事务的执行不能被其他事务干扰。数据库系统必须提供一种隔离机制，使得事务在并发
执行时，它们的操作和数据就像是串行执行的一样。
作用：防止了多个事务并发执行时可能产生的问题，如脏读、不可重复读和幻读等。
4. 持久性（Durability）
定义：一旦事务被提交，它对数据库的修改就是永久性的，即使数据库系统发生故障也不会丢失。
作用：确保了事务的修改结果能够持久地保存在数据库中，不会因为系统故障等原因而丢失。
综上所述，事务的四大特性是数据库事务处理的基础，它们共同保证了数据库数据的一致性和可靠性。在实际应用中，数据库系统通过
实现这些特性来确保数据的安全和准确。
```

29.Spring 的事务隔离?

```
Spring 的事务隔离是数据库事务处理中的一个重要概念，它确保了在并发环境下事务的执行能够按照预期的顺序和结果进行，从而避免
了数据的不一致性。Spring 提供了对事务的强大支持，包括事务的隔离级别配置，这些隔离级别是通过数据库来控制的。

一、事务隔离级别的定义
Spring 定义了五个事务隔离级别，这些级别与数据库系统中的标准隔离级别相对应，具体如下：

读未提交（Read Uncommitted）：
允许一个事务读取另一个未提交事务的数据。
这可能导致脏读、不可重复读和幻读。
适用于对数据一致性要求不高且需要最大并发性的场景。
读已提交（Read Committed）：
只允许读取已提交的事务数据。
防止了脏读，但可能导致不可重复读和幻读。
大多数情况下是合理的选择，适用于大部分读写操作。
可重复读（Repeatable Read）：
确保在同一个事务中多次读取同样的数据时，这些数据是相同的。
防止了脏读和不可重复读，但可能导致幻读。
适用于需要防止不可重复读的场景，如银行账户转账等操作。
串行化（Serializable）：
提供最高的隔离级别，通过序列化事务执行。
完全防止脏读、不可重复读和幻读。
但可能会导致性能下降和锁定争用，适用于要求最高数据一致性的场景，如金融交易系统。
默认（DEFAULT）：
这是Spring特有的一个级别，意味着事务的隔离级别将由底层数据库系统决定。
每个数据库都有自己的默认隔离级别，例如MySQL的默认隔离级别是REPEATABLE_READ，而Oracle的默认级别是READ_COMMITTED。
二、事务隔离的实现
在Spring中，事务隔离级别是通过AOP（面向切面编程）来实现的。具体来说，当一个方法被标记为@Transactional注解时，Spring会在
方法执行前生成一个代理对象，代理对象会拦截方法的调用，执行事务管理的相关逻辑。在事务开始时，Spring会根据设置的隔离级别
将数据库的事务隔离级别设置为相应的级别。

三、事务隔离的并发问题
事务隔离级别与并发问题密切相关，主要包括脏读、不可重复读和幻读：

脏读（Dirty Read）：一个事务读取了另一个事务尚未提交的更改数据。
不可重复读（Non-Repeatable Read）：一个事务在读取同一数据时，发现该数据被另一个已提交的事务修改了。
幻读（Phantom Read）：一个事务在读取同一查询时，发现该查询的结果集中包含了由另一个已提交的事务插入的新行。
四、配置示例
在Spring中，可以通过@Transactional注解来设置事务的隔离级别，例如：

java
import org.springframework.transaction.annotation.Transactional;  
import org.springframework.transaction.annotation.Isolation;  
  
@Transactional(isolation = Isolation.READ_COMMITTED)  
public void someServiceMethod() {  
    // 事务内的代码  
}
五、总结
Spring 的事务隔离是确保数据库事务在并发环境下正确执行的关键技术之一。通过合理设置事务的隔离级别，可以平衡数据一致性和系统
性能的需求。在实际应用中，应根据业务场景和需求来选择合适的隔离级别。
```

30.Spring多线程事务 能否保证事务的一致性？

```
Spring多线程事务在默认情况下无法保证全局事务的一致性。这是因为Spring的本地事务管理是基于线程的，每个线程都有自己的独立
事务。Spring的事务管理通常将事务信息存储在ThreadLocal中，这意味着每个线程只能拥有一个事务。这确保了在单个线程内的数据库
操作处于同一个事务中，保证了原子性。但在多线程环境下，事务的隔离性、原子性和一致性可能面临挑战。

为了在多线程环境中实现事务一致性，Spring提供了以下几种解决方案：

编程式事务管理：在代码中显式控制事务的边界和操作，确保在适当的时机提交或回滚事务。
声明式事务管理：通过注解或XML配置来声明事务，并将其应用于方法或类级别。声明式事务管理使得事务边界管理更加方便，并提供了
一种优雅的方式来确保事务的一致性。
分布式事务管理：如果您的应用程序需要跨多个资源（例如多个数据库）的全局事务一致性，那么您可能需要使用分布式事务管理
（如2PC/3PC、TCC等）来管理全局事务。这将确保所有参与的资源都处于相同的全局事务中，以保证一致性。
```

参考代码:

```
package com.lenovo.otmp.download.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author tom
 */
public class ExecutorConfig {

    private final static int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private final static int QUEUE_SIZE = 500;
    private volatile static ExecutorService executorService;

    public static ExecutorService getThreadPool() {
        if (executorService == null) {
            synchronized (ExecutorConfig.class) {
                if (executorService == null) {
                    executorService = newThreadPool();
                }
            }
        }
        return executorService;
    }

    private static ExecutorService newThreadPool() {
        int corePool = Math.min(5, MAX_POOL_SIZE);
        return new ThreadPoolExecutor(corePool, MAX_POOL_SIZE, 10000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(QUEUE_SIZE), new ThreadPoolExecutor.AbortPolicy());
    }

    private ExecutorConfig() {
    }
}
```

方法一：共享一个事务管理

```
package com.lenovo.otmp.download.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tom
 */
@Service
public class MultiThreadingTransactionManager {

    /**
     * 数据源事务管理器
     */
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    public void setUserService(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    /**
     * 用于判断子线程业务是否处理完成
     * 处理完成时threadCountDownLatch的值为0
     */
    private CountDownLatch threadCountDownLatch;

    /**
     * 用于等待子线程全部完成后,子线程统一进行提交和回滚
     * 进行提交和回滚时mainCountDownLatch的值为0
     */
    private final CountDownLatch mainCountDownLatch = new CountDownLatch(1);

    /**
     * 是否提交事务,默认是true,当子线程有异常发生时,设置为false,回滚事务
     */
    private final AtomicBoolean isSubmit = new AtomicBoolean(true);

    public boolean execute(List<Runnable> runnableList) {
        // 超时时间
        long timeout = 30;
        setThreadCountDownLatch(runnableList.size());
        ExecutorService executorService = ExecutorConfig.getThreadPool();
        runnableList.forEach(runnable -> executorService.execute(() -> executeThread(runnable, threadCountDownLatch, mainCountDownLatch, isSubmit)));
        // 等待子线程全部执行完毕
        try {
            // 若计数器变为零了,则返回 true
            boolean isFinish = threadCountDownLatch.await(timeout, TimeUnit.SECONDS);
            if (!isFinish) {
                // 如果还有为执行完成的就回滚
                isSubmit.set(false);
                System.out.println("存在子线程在预期时间内未执行完毕,任务将全部回滚");
            }
        } catch (Exception exception) {
            System.out.println("主线程发生异常,异常为: " + exception.getMessage());
        } finally {
            // 计数器减1,代表该主线程执行完毕
            mainCountDownLatch.countDown();
        }
        // 返回结果,是否执行成功,事务提交即为执行成功,事务回滚即为执行失败
        return isSubmit.get();
    }

    private void executeThread(Runnable runnable, CountDownLatch threadCountDownLatch, CountDownLatch mainCountDownLatch, AtomicBoolean isSubmit) {
        System.out.println("子线程: [" + Thread.currentThread().getName() + "]");
        // 判断别的子线程是否已经出现错误,错误别的线程已经出现错误,那么所有的都要回滚,这个子线程就没有必要执行了
        if (!isSubmit.get()) {
            System.out.println("整个事务中有子线程执行失败需要回滚, 子线程: [" + Thread.currentThread().getName() + "] 终止执行");
            // 计数器减1,代表该子线程执行完毕
            threadCountDownLatch.countDown();
            return;
        }
        // 开启事务
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
        try {
            // 执行业务逻辑
            runnable.run();
        } catch (Exception exception) {
            // 发生异常需要进行回滚,设置isSubmit为false
            isSubmit.set(false);
            System.out.println("子线程: [" + Thread.currentThread().getName() + "]执行业务发生异常,异常为: " + exception.getMessage());
        } finally {
            // 计数器减1,代表该子线程执行完毕
            threadCountDownLatch.countDown();
        }
        try {
            // 等待主线程执行
            mainCountDownLatch.await();
        } catch (Exception exception) {
            System.out.println("子线程: [" + Thread.currentThread().getName() + "]等待提交或回滚异常,异常为: " + exception.getMessage());
        }
        try {
            // 提交
            if (isSubmit.get()) {
                dataSourceTransactionManager.commit(transactionStatus);
                System.out.println("子线程: [" + Thread.currentThread().getName() + "]进行事务提交");
            } else {
                dataSourceTransactionManager.rollback(transactionStatus);
                System.out.println("子线程: [" + Thread.currentThread().getName() + "]进行事务回滚");
            }
        } catch (Exception exception) {
            System.out.println("子线程: [" + Thread.currentThread().getName() + "]进行事务提交或回滚出现异常,异常为:" + exception.getMessage());
        }
    }

    private void setThreadCountDownLatch(int num) {
        this.threadCountDownLatch = new CountDownLatch(num);
    }

}
```

方法二: 多个事务管理

```
package com.lenovo.otmp.download.config;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tom
 */
@Service
public class MultiThreadingTransactionManagerTwo {

    /**
     * 数据源事务管理器
     */
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    public void setUserService(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    /**
     * 用于判断子线程业务是否处理完成
     * 处理完成时threadCountDownLatch的值为0
     */
    private CountDownLatch threadCountDownLatch;

    /**
     * 是否提交事务,默认是true,当子线程有异常发生时,设置为false,回滚事务
     */
    private final AtomicBoolean isSubmit = new AtomicBoolean(true);

    public boolean execute(List<Runnable> runnableList) {
        // 超时时间
        long timeout = 30;
        List<TransactionStatus> transactionStatusList = Collections.synchronizedList(new ArrayList<>());
        List<TransactionResource> transactionResourceList = Collections.synchronizedList(new ArrayList<>());
        setThreadCountDownLatch(runnableList.size());
        ExecutorService executorService = ExecutorConfig.getThreadPool();
        runnableList.forEach(runnable -> executorService.execute(() -> {
            try {
                // 执行业务逻辑
                executeThread(runnable, transactionStatusList, transactionResourceList);
            } catch (Exception exception) {
                exception.printStackTrace();
                // 执行异常,需要回滚
                isSubmit.set(false);
            } finally {
                threadCountDownLatch.countDown();
            }
        }));
        // 等待子线程全部执行完毕
        try {
            // 若计数器变为零了,则返回 true
            boolean isFinish = threadCountDownLatch.await(timeout, TimeUnit.SECONDS);
            if (!isFinish) {
                // 如果还有为执行完成的就回滚
                isSubmit.set(false);
                System.out.println("存在子线程在预期时间内未执行完毕,任务将全部回滚");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        // 发生了异常则进行回滚操作,否则提交
        if (isSubmit.get()) {
            System.out.println("全部事务正常提交");
            for (int i = 0; i < runnableList.size(); i++) {
                transactionResourceList.get(i).autoWiredTransactionResource();
                dataSourceTransactionManager.commit(transactionStatusList.get(i));
                transactionResourceList.get(i).removeTransactionResource();
            }
        } else {
            System.out.println("发生异常,全部事务回滚");
            for (int i = 0; i < runnableList.size(); i++) {
                transactionResourceList.get(i).autoWiredTransactionResource();
                dataSourceTransactionManager.rollback(transactionStatusList.get(i));
                transactionResourceList.get(i).removeTransactionResource();
            }
        }
        // 返回结果,是否执行成功,事务提交即为执行成功,事务回滚即为执行失败
        return isSubmit.get();
    }

    private void executeThread(Runnable runnable, List<TransactionStatus> transactionStatusList, List<TransactionResource> transactionResourceList) {
        System.out.println("子线程: [" + Thread.currentThread().getName() + "]");
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
        // 开启新事务
        transactionStatusList.add(transactionStatus);
        // copy事务资源
        transactionResourceList.add(TransactionResource.copyTransactionResource());
        // 执行业务逻辑
        runnable.run();
    }

    private void setThreadCountDownLatch(int num) {
        this.threadCountDownLatch = new CountDownLatch(num);
    }

    /**
     * 保存当前事务资源,用于线程间的事务资源COPY操作
     * <p>
     * `@Builder`注解是Lombok库提供的一个注解,它可以用于自动生成Builder模式的代码,使用@Builder注解可以简化创建对象实例的过程,并且可以使代码更加清晰和易于维护
     */
    @Builder
    private static class TransactionResource {
        // TransactionSynchronizationManager类内部默认提供了下面六个ThreadLocal属性,分别保存当前线程对应的不同事务资源
        // 保存当前事务关联的资源,默认只会在新建事务的时候保存当前获取到的DataSource和当前事务对应Connection的映射关系
        // 当然这里Connection被包装为了ConnectionHolder
        // 事务结束后默认会移除集合中的DataSource作为key关联的资源记录
        private Map<Object, Object> resources;
        //下面五个属性会在事务结束后被自动清理,无需我们手动清理
        // 事务监听者,在事务执行到某个阶段的过程中,会去回调监听者对应的回调接口(典型观察者模式的应用),默认为空集合
        private Set<TransactionSynchronization> synchronizations;
        // 存放当前事务名字
        private String currentTransactionName;
        // 存放当前事务是否是只读事务
        private Boolean currentTransactionReadOnly;
        // 存放当前事务的隔离级别
        private Integer currentTransactionIsolationLevel;
        // 存放当前事务是否处于激活状态
        private Boolean actualTransactionActive;

        /**
         * 对事务资源进行复制
         *
         * @return TransactionResource
         */
        public static TransactionResource copyTransactionResource() {
            return TransactionResource.builder()
                    //返回的是不可变集合
                    .resources(TransactionSynchronizationManager.getResourceMap())
                    //如果需要注册事务监听者,这里记得修改,我们这里不需要,就采用默认负责,spring事务内部默认也是这个值
                    .synchronizations(new LinkedHashSet<>()).currentTransactionName(TransactionSynchronizationManager.getCurrentTransactionName()).currentTransactionReadOnly(TransactionSynchronizationManager.isCurrentTransactionReadOnly()).currentTransactionIsolationLevel(TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()).actualTransactionActive(TransactionSynchronizationManager.isActualTransactionActive()).build();
        }

        /**
         * 使用
         */
        public void autoWiredTransactionResource() {
            resources.forEach(TransactionSynchronizationManager::bindResource);
            //如果需要注册事务监听者,这里记得修改,我们这里不需要,就采用默认负责,spring事务内部默认也是这个值
            TransactionSynchronizationManager.initSynchronization();
            TransactionSynchronizationManager.setActualTransactionActive(actualTransactionActive);
            TransactionSynchronizationManager.setCurrentTransactionName(currentTransactionName);
            TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(currentTransactionIsolationLevel);
            TransactionSynchronizationManager.setCurrentTransactionReadOnly(currentTransactionReadOnly);
        }

        /**
         * 移除
         */
        public void removeTransactionResource() {
            // 事务结束后默认会移除集合中的DataSource作为key关联的资源记录
            // DataSource如果重复移除,unbindResource时会因为不存在此key关联的事务资源而报错
            resources.keySet().forEach(key -> {
                if (!(key instanceof DataSource)) {
                    TransactionSynchronizationManager.unbindResource(key);
                }
            });
        }
    }
}
```

方法三：使用CompletableFuture实现 推荐

```
package com.lenovo.otmp.download.config;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tom
 */
@Service
public class MultiThreadingTransactionManagerThree {

    /**
     * 数据源事务管理器
     */
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    public void setUserService(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    /**
     * 是否提交事务,默认是true,当子线程有异常发生时,设置为false,回滚事务
     */
    private final AtomicBoolean isSubmit = new AtomicBoolean(true);

    public boolean execute(List<Runnable> runnableList) {
        List<TransactionStatus> transactionStatusList = Collections.synchronizedList(new ArrayList<>());
        List<TransactionResource> transactionResourceList = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<?>> completableFutureList = new ArrayList<>(runnableList.size());
        runnableList.forEach(runnable -> completableFutureList.add(CompletableFuture.runAsync(() -> {
            try {
                // 执行业务逻辑
                executeThread(runnable, transactionStatusList, transactionResourceList);
            } catch (Exception exception) {
                exception.printStackTrace();
                // 执行异常,需要回滚
                isSubmit.set(false);
                // 终止其它还未执行的任务
                completableFutureList.forEach(completableFuture -> completableFuture.cancel(true));
            }
        })));
        // 等待子线程全部执行完毕
        try {
            // 阻塞直到所有任务全部执行结束,如果有任务被取消,这里会抛出异常,需要捕获
            CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[]{})).get();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        // 发生了异常则进行回滚操作,否则提交
        if (!isSubmit.get()) {
            System.out.println("发生异常,全部事务回滚");
            for (int i = 0; i < runnableList.size(); i++) {
                transactionResourceList.get(i).autoWiredTransactionResource();
                dataSourceTransactionManager.rollback(transactionStatusList.get(i));
                transactionResourceList.get(i).removeTransactionResource();
            }
        } else {
            System.out.println("全部事务正常提交");
            for (int i = 0; i < runnableList.size(); i++) {
                transactionResourceList.get(i).autoWiredTransactionResource();
                dataSourceTransactionManager.commit(transactionStatusList.get(i));
                transactionResourceList.get(i).removeTransactionResource();
            }
        }
        // 返回结果,是否执行成功,事务提交即为执行成功,事务回滚即为执行失败
        return isSubmit.get();
    }

    private void executeThread(Runnable runnable, List<TransactionStatus> transactionStatusList, List<TransactionResource> transactionResourceList) {
        System.out.println("子线程: [" + Thread.currentThread().getName() + "]");
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(defaultTransactionDefinition);
        // 开启新事务
        transactionStatusList.add(transactionStatus);
        // copy事务资源
        transactionResourceList.add(TransactionResource.copyTransactionResource());
        // 执行业务逻辑
        runnable.run();
    }

    /**
     * 保存当前事务资源,用于线程间的事务资源COPY操作
     * <p>
     * `@Builder`注解是Lombok库提供的一个注解,它可以用于自动生成Builder模式的代码,使用@Builder注解可以简化创建对象实例的过程,并且可以使代码更加清晰和易于维护
     */
    @Builder
    private static class TransactionResource {
        // TransactionSynchronizationManager类内部默认提供了下面六个ThreadLocal属性,分别保存当前线程对应的不同事务资源
        // 保存当前事务关联的资源,默认只会在新建事务的时候保存当前获取到的DataSource和当前事务对应Connection的映射关系
        // 当然这里Connection被包装为了ConnectionHolder
        // 事务结束后默认会移除集合中的DataSource作为key关联的资源记录
        private Map<Object, Object> resources;
        //下面五个属性会在事务结束后被自动清理,无需我们手动清理
        // 事务监听者,在事务执行到某个阶段的过程中,会去回调监听者对应的回调接口(典型观察者模式的应用),默认为空集合
        private Set<TransactionSynchronization> synchronizations;
        // 存放当前事务名字
        private String currentTransactionName;
        // 存放当前事务是否是只读事务
        private Boolean currentTransactionReadOnly;
        // 存放当前事务的隔离级别
        private Integer currentTransactionIsolationLevel;
        // 存放当前事务是否处于激活状态
        private Boolean actualTransactionActive;

        /**
         * 对事务资源进行复制
         *
         * @return TransactionResource
         */
        public static TransactionResource copyTransactionResource() {
            return TransactionResource.builder()
                    //返回的是不可变集合
                    .resources(TransactionSynchronizationManager.getResourceMap())
                    //如果需要注册事务监听者,这里记得修改,我们这里不需要,就采用默认负责,spring事务内部默认也是这个值
                    .synchronizations(new LinkedHashSet<>()).currentTransactionName(TransactionSynchronizationManager.getCurrentTransactionName()).currentTransactionReadOnly(TransactionSynchronizationManager.isCurrentTransactionReadOnly()).currentTransactionIsolationLevel(TransactionSynchronizationManager.getCurrentTransactionIsolationLevel()).actualTransactionActive(TransactionSynchronizationManager.isActualTransactionActive()).build();
        }

        /**
         * 使用
         */
        public void autoWiredTransactionResource() {
            resources.forEach(TransactionSynchronizationManager::bindResource);
            //如果需要注册事务监听者,这里记得修改,我们这里不需要,就采用默认负责,spring事务内部默认也是这个值
            TransactionSynchronizationManager.initSynchronization();
            TransactionSynchronizationManager.setActualTransactionActive(actualTransactionActive);
            TransactionSynchronizationManager.setCurrentTransactionName(currentTransactionName);
            TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(currentTransactionIsolationLevel);
            TransactionSynchronizationManager.setCurrentTransactionReadOnly(currentTransactionReadOnly);
        }

        /**
         * 移除
         */
        public void removeTransactionResource() {
            // 事务结束后默认会移除集合中的DataSource作为key关联的资源记录
            // DataSource如果重复移除,unbindResource时会因为不存在此key关联的事务资源而报错
            resources.keySet().forEach(key -> {
                if (!(key instanceof DataSource)) {
                    TransactionSynchronizationManager.unbindResource(key);
                }
            });
        }
    }
}
```

测试:

```
package com.lenovo.otmp.download.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TransactionApplicationFourTests {

    @Autowired
    private MultiThreadingTransactionManager multiThreadingTransactionManager;

    void contextLoads() {
        List<String> studentList = new ArrayList<>();
        studentList.add("tom");
        studentList.add("marry");
        List<Runnable> runnableList = new ArrayList<>();
        studentList.forEach(student -> runnableList.add(() -> {
            System.out.println("当前线程：[" + Thread.currentThread().getName() + "] 插入数据: " + student);
            try {
//                studentMapper.insert(student);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        boolean isSuccess = multiThreadingTransactionManager.execute(runnableList);
        System.out.println(isSuccess);
    }
}
```

31.Spring事务的失效原因?

```
Spring事务的失效原因可能涉及多个方面，以下是一些常见的原因及其详细解释：

1. @Transactional注解使用不当
非public方法：@Transactional注解只对public方法有效。如果注解被添加在非public方法（如private、protected等）上，Spring将不会对其进行事务管理。
同一个类中方法调用：如果在一个类的内部，一个方法直接调用了另一个带有@Transactional注解的方法，那么事务可能会失效。这是因为Spring的AOP机制是通过代理对象来实现事务管理的，而在同一个类内部调用时，不会通过代理对象，因此事务不会被触发。
2. 异常处理不当
异常被捕获且未重新抛出：如果事务方法中的异常被捕获并且没有重新抛出，那么Spring事务管理器会认为事务已经成功完成，从而提交事务。这可能导致数据不一致。
rollbackFor设置错误：如果@Transactional注解的rollbackFor属性没有正确设置，那么在某些异常发生时，事务可能不会被回滚。
3. 事务传播行为配置错误
传播行为不支持事务：如果事务方法的传播行为被设置为不支持事务（如Propagation.NOT_SUPPORTED），那么该事务方法将不会运行在事务环境中，从而导致事务失效。
错误的传播行为组合：当在一个事务方法内部调用另一个事务方法时，如果传播行为配置不当，可能会导致内部方法的事务失效或不符合预期。
4. Spring配置问题
事务管理器未配置或配置错误：如果Spring配置文件中没有正确配置事务管理器，或者事务管理器的配置有误，那么事务将不会生效。
tx:annotation-driven/未启用：在Spring配置文件中，如果没有启用tx:annotation-driven/，那么@Transactional注解将不会被识别和处理。
5. 数据库问题
数据库不支持事务：如果使用的数据库引擎不支持事务（如MySQL的MyISAM存储引擎），那么即使在代码中使用了事务注解，事务也不会生效。
数据库连接问题：如果数据库连接没有正确配置或连接池设置不当，也可能导致事务失效。
6. 其他因素
多线程环境：在多线程环境下，如果事务的上下文没有被正确传递，那么新线程中的操作可能不会被包含在原有的事务中。
Spring版本问题：在某些Spring版本中，可能存在已知的bug或限制，这些也可能导致事务失效。
解决方案
确保@Transactional注解只用于public方法，并避免在同一个类中直接调用事务方法。
正确处理异常，确保需要回滚的异常被正确捕获并重新抛出。
仔细配置事务的传播行为和rollbackFor属性。
检查并确认Spring配置文件中事务管理器的配置以及tx:annotation-driven/的启用。
确保使用的数据库引擎支持事务，并正确配置数据库连接。
在多线程环境下，确保事务上下文被正确传递。
关注Spring版本的更新和已知问题的修复。
```