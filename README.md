#EasyXls

使用EasyXls可以使你很方便的操作Excel。本项目目的是解决简单的（不带任何样式）xls文件的导入导出。  

##EasyXls特点

1. 支持xls转换为`List<Pojo>`对象

2. 支持xls转换为`List<Map>`对象

3. 支持`List<Pojo>`转换为xls

4. 支持`List<Map>`转换为xls

5. 支持xml配置形式

6. 支持java编码创建`Config`配置


##xml配置向导

项目中包含一个简单的xml生成向导，使用该项导时，需要在项目中创建main方法并调用如下方法：  

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

##示例

以下代码均来自测试代码（test目录下）  

部分xml：  

```xml
<?xml version="1.0" encoding="GBK" standalone="yes"?>
<excel>
  <cache>false</cache>
  <sheet>一次性费用</sheet>
  <class>po.Charges</class>
  <sheetNum>0</sheetNum>
  <startRow>1</startRow>
  <columns>
    <column>
      <name>year</name>
      <header>年度</header>
      <type>java.lang.Integer</type>
      <width>50</width>
    </column>
  </columns>
</excel>
```

使用xml配置读取xls：  

```java
@Test
public void testMap() {
    try {
        List list = EasyXls.xls2List(
                Xls2ListTest.class.getResource("/ChargesMap.xml").getPath(),
                new File(Xls2ListTest.class.getResource("2.xls").getPath()));
        System.out.println(list);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```  

使用java代码创建Config并读取xls：  

```java
@Test
public void testConfig() {
    try {
        //创建一个配置
        ExcelConfig config = new ExcelConfig.Builder(Charges.class)
                .sheetNum(0)
                .startRow(1)
                .key("name")
                .addColumn("year", "communityid", "roomno", "ownersid", "ownersname", "property").build();
        List list = EasyXls.xls2List(config, Xls2ListTest.class.getResourceAsStream("2.xls"));
        for (int i = 0; i < list.size(); i++) {
            System.out.println(((Charges) list.get(i)).getOwnersname());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

这里对列的处理很简单，excel文件的列和column的列一一对应即可。  

使用xml配置读取，使用Config方式写入到新的excel文件：  

```java
@Test
public void testMap2() {
    InputStream is = Xls2ListTest.class.getResourceAsStream("2.xls");
    try {
        String xmlPath = Xls2ListTest.class.getResource("/ChargesMap.xml").getPath();
        List list = EasyXls.xls2List(xmlPath, is);

        Map map = new HashMap();
        map.put("year", 2013);
        map.put("ownersname", "测试户主");
        list.add(map);

        EasyXls.list2Xls(list, xmlPath, "d:/", "testMap.xls");

        ExcelConfig config = new ExcelConfig.Builder(Charges.class)
                .sheetNum(0)
                .startRow(1)
                .separater(",")
                .key("name")
                .addColumn("year,年度", "communityid,小区ID",
                        "roomno,房号", "ownersid,户主ID",
                        "ownersname,户主姓名", "property,物业费").build();
        EasyXls.list2Xls(config, list, "d:/", "testMap2.xls");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```  

这里为了调用方法，使用了`.separater(",")`设定分隔符（默认为英文逗号），然后在`addColumn`方法中使用如`"year,年度"`这种方式赋值。  

这种赋值方式最多支持4个参数，分别对应name,header,width,column，如：  

>"year,年度,200,java.lang.Integer"  