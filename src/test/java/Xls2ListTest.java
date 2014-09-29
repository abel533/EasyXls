import com.github.abel533.easyxls.EasyXls;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

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


   /* @Test
    public void test1() {
        try {
            List list = EasyXls.xls2List(
                    XlsTest.class.getResource("/xls/owners.xml").getPath(),
                    new FileInputStream("d:/owners.xls"));
            System.out.println(list != null ? list.size() : 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            //创建一个配置
            ExcelConfig config = new ExcelConfig.Builder(Owner.class)
                    .sheetNum(0)
                    .startRow(1)
                    .maxRow(1)
                    .key("name")
                    .addColumn("name", "address", "user", "conarea").build();
            List list = EasyXls.xls2List(config, new FileInputStream("d:/owners.xls"));
            for (int i = 0; i < list.size(); i++) {
                System.out.println(((Owner)list.get(i)).getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2Map() {
        try {
            //创建一个配置
            ExcelConfig config = new ExcelConfig.Builder(HashMap.class)
                    .sheetNum(0)
                    .startRow(1)
                    .maxRow(1)
                    .key("name")
                    .addColumn("name", "address", "user", "conarea").build();
            List list = EasyXls.xls2List(config, new FileInputStream("d:/owners.xls"));
            for (int i = 0; i < list.size(); i++) {
                System.out.println(((Map)list.get(i)).get("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        try {
            List list = new ArrayList();

            System.out.println(list.size());

            for (int i = 0; i < 100; i++) {
                Owner owner = new Owner();
                owner.setName("测试" + i);
                owner.setAddress("1-" + i);
                owner.setConarea(new BigDecimal(String.valueOf(100 * Math.random() + i * Math.random())).setScale(2,BigDecimal.ROUND_HALF_UP));
                list.add(owner);
            }

            ExcelConfig config = new ExcelConfig.Builder(Owner.class)
                    .sheetName("姓名sheet")
                    .addColumn("name", "姓名", true)
                    .addColumn("address","房号")
                    .addColumn("conarea","建筑面积").build();
            EasyXls.list2Xls(config, list, "d:/", "owners.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3Map() {
        try {
            List list = new ArrayList();

            System.out.println(list.size());

            for (int i = 0; i < 100; i++) {
                Map owner = new HashMap();
                owner.put("name","测试" + i);
                owner.put("address","1-" + i);
                owner.put("conarea",new BigDecimal(String.valueOf(100 * Math.random() + i * Math.random())).setScale(2,BigDecimal.ROUND_HALF_UP));
                list.add(owner);
            }

            ExcelConfig config = new ExcelConfig.Builder(HashMap.class)
                    .sheetName("姓名sheet")
                    .addColumn("name", "姓名", true)
                    .addColumn("address","房号")
                    .addColumn("conarea","建筑面积").build();
            EasyXls.list2Xls(config, list, "d:/", "owners2.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
