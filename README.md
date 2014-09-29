#EasyXls

使你更方便的操作Excel，主要提供两个方法，读取excel到List对象，导出List对象到excel。 使用xml配置文件和Object对象进行操作。  

源码很简单，本项目目前仅支持的简单的Excel格式。  

##项目即将发布



##另外还有一个简单的xml生成向导，使用该项导时，需要在项目中main方法中调用如下方法：  

```java
EasyXls.openGenerater();
```

##支持的列类型
 - int
 - long
 - float
 - double
 - java.lang.Integer
 - java.lang.Long
 - java.lang.Float
 - java.lang.Double
 - java.util.Date
 - java.math.BigDecimal

##项目依赖  

```xml
<dependency>
  <groupId>net.sourceforge.jexcelapi</groupId>
  <artifactId>jxl</artifactId>
  <version>2.6.12</version>
</dependency>