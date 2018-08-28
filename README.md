# uiharu framework

Uiharu framework is a simple java mvc framework. The word uiharu means early spring.



## Inspiration

This wheel is mainly inspired by Spring framework. I reinvent it because I want to learn java MVC framework by creating a poor wheel:smile: .



## Features

* IOC
* AOP
* Database connection and transaction
* Auto parsing request params and body
* Make request mapping by annotation



## Usage

First you should install uiharu framework in your local maven repository.

```shell
mvn clean install -DskipTests
```

Then, create a new project to use enjoy it.

There is a demo at [uiharu-demo](https://github.com/hgaol/uiharu-demo).

1. add dependence in `pom.xml`

```xml
	...
	<dependencies>
        <dependency>
            <groupId>com.github.hgaol</groupId>
            <artifactId>uiharu</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
	...
```

2. Controller

```java
@Action(path = "/test")
public Data paramTest(Param param, Customer customer) {
    List<Customer> customerList = customerService.getCustomerList();
    return new Data(customerList);
}

@Action(method = "post", path = "/test")
public Data postTest(Param param, @Body Customer customer) {
    List<Customer> customerList = customerService.getCustomerList();
    return new Data(customerList);
}

@Action(path = "/customers")
public View index() {
    List<Customer> customerList = customerService.getCustomerList();
    return new View("customer.jsp").addModel("customerList", customerList);
}
```

3. Database operation and transaction

```java
public List<Customer> getCustomersBySQL() {
    String sql = "SELECT * FROM customer";
    return DatabaseHelper.queryEntityList(Customer.class, sql);
}

public Customer getCustomersById() {
    String sql = "SELECT * FROM customer where id = ?";
    return DatabaseHelper.queryEntity(Customer.class, sql, "1");
}

@Transaction
public boolean insert() {
    Customer a = new Customer("a", "xiao a", "1234567890", "b@m.com");
    return DatabaseHelper.insert(a);
}

@Transaction
public boolean createFailure() {
    Customer a = new Customer("b", "xiao b", "1234567890", "b@m.com");
    boolean result = DatabaseHelper.insert(a);
    throw new Error("lalala");
}
```

4. Aop

```java
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {
    private long begin;
    @Override
    public void before(Class<?> cls, Method method, Object[] params) {
        System.out.println("---- before ----");
        begin = System.currentTimeMillis();
    }
    @Override
    public void after(Class<?> cls, Method method, Object[] params, Object result) {
        System.out.println(String.format("time: %dms", System.currentTimeMillis() - begin));
        System.out.println("----------- end -----------");
    }
}
```



## Run

Build demo and put xxx.war into your tomcat's webapps directory, and start tomcat.

```shell
mv target/uiharu-demo-1.0.war <your_tomcat_webapps_dir>
# cd to tomcat dir
./bin/startup.sh
```

then go to `http://localhost:<port>/<your_app_name>/data` to see the get api.



