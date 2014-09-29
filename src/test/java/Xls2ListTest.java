import com.github.abel533.easyxls.EasyXls;
import com.github.abel533.easyxls.bean.ExcelConfig;
import org.junit.Test;
import po.Charges;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuzh
 */
public class Xls2ListTest {

    @Test
    public void test() {
        InputStream is = Xls2ListTest.class.getResourceAsStream("2.xls");
        try {
            List list = EasyXls.xls2List(Xls2ListTest.class.getResource("/Charges.xml").getPath(), is);
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Test
    public void testConfigMap() {
        try {
            //创建一个配置
            ExcelConfig config = new ExcelConfig.Builder(HashMap.class)
                    .sheetNum(0)
                    .startRow(1)
                    .key("name")
                    .addColumn("year", "communityid", "roomno", "ownersid", "ownersname", "property").build();
            List list = EasyXls.xls2List(config, Xls2ListTest.class.getResourceAsStream("2.xls"));
            for (int i = 0; i < list.size(); i++) {
                System.out.println(((Map) list.get(i)).get("ownersname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
