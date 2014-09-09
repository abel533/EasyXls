#EasyXls

使你更方便的操作Excel，主要提供两个方法，读取excel到List对象，导出List对象到excel。 使用xml配置文件和Object对象进行操作。  

源码很简单，本项目目前仅支持的简单的Excel格式。  

##两个和Excel和List有关的方法：   

```java
/**
 * 导入xml到List
 *
 * @param xmlPath xml完整路径
 * @param xlsFile xls文件路径
 * @return List对象
 * @throws Exception
 */
public static List<?> xls2List(String xmlPath, File xlsFile) throws Exception;

/**
 * 导出list对象到excel
 *
 * @param list     导出的list
 * @param xmlPath  xml完整路径
 * @param filePath 保存xls路径
 * @param fileName 保存xls文件名
 * @return 处理结果，true成功，false失败
 * @throws Exception
 */
public static boolean list2Xls(List<?> list, String xmlPath, String filePath, String fileName) throws Exception;
```

##另外还有一个简单的xml生成向导，使用该项导时，需要在项目中main方法中调用如下方法：  

```java
EasyXls.openGenerater();
```


##支持的列类型
int,
long,
float,
double,
java.lang.Integer,
java.lang.Long,
java.lang.Float,
java.lang.Double,
java.util.Date,
java.math.BigDecimal

##项目依赖  

```xml
<dependency>
  <groupId>net.sourceforge.jexcelapi</groupId>
  <artifactId>jxl</artifactId>
  <version>2.6.12</version>
</dependency>
```

##XML配置文件示例

```xml  
<?xml version="1.0" encoding="UTF-8"?>
<excel>
  <!--
       必填项为:name,header，其他全部选填

       title:excel表格中第一行合并单元格居中显示的内容，空的时候不输出
       description：第二行显示的说明，空的时候不输出
       cache:是否缓存，默认为true，true时，解析一次xml后不会再次解析，也无法进行实时修改。如果想实时修改，请设为false
       class:类路径
       sheet:导出excel的sheet名（从0开始计算）
       startRow:从第几行开始读取(从0开始计算）
       columns:导出列的信息
           column:一个列
               name:列对应Object中的字段名，必须有标准的get方法
               header:导出excel中显示的标题
               width:列宽(单位像素)
               type:列的类型，读取时需要用到
    -->
  <title>人员信息导出</title>
  <description>这里是描述信息</description>
  <!-- 读取配置 开始 -->
  <class>com.isea.easyxls.test.TestObj</class>
  <sheetNum>0</sheetNum>
  <startRow>3</startRow>
  <!-- 读取配置 结束 -->
  <author>isea533</author>
  <sheet>人员信息</sheet>
  <columns>
    <column>
      <name>name</name>
      <type>java.lang.String</type>
      <header>姓名</header>
      <width>200</width>
    </column>
    <column>
      <name>country</name>
      <type>java.lang.String</type>
      <header>国籍</header>
      <width>50</width>
    </column>
    <column>
      <name>birthday</name>
      <type>java.util.Date</type>
      <header>生日</header>
      <width>160</width>
    </column>
    <column>
      <name>age</name>
      <type>int</type>
      <header>年龄</header>
    </column>
    <column>
      <name>str4</name>
      <type>java.lang.String</type>
      <header>字符串4</header>
    </column>
    <column>
      <name>str2</name>
      <type>java.lang.String</type>
      <header>字符串2</header>
    </column>
    <column>
      <name>str3</name>
      <type>java.lang.String</type>
      <header>字符串3</header>
    </column>
    <column>
      <name>str1</name>
      <type>java.lang.String</type>
      <header>字符串1</header>
    </column>
    <column>
      <name>str5</name>
      <type>java.lang.String</type>
      <header>字符串5</header>
    </column>
  </columns>
</excel>
```

##读取测试

```java  
public  static void ReadTest() throws Exception{
        String xmlPath = "E:\\TestObj.xml";
        String filePath = "E:/";
        String fileName = "text";
        File file = new File(filePath+fileName+".xls");
        List<TestObj> list = (List<TestObj>) ImpXls2ListObj.impXls2ListObj(xmlPath,file);
        for(TestObj testObj:list){
                System.out.println(testObj.getName()+","+testObj.getAge()+","+testObj.getCountry());
        }
}
```  