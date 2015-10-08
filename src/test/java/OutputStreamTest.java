import com.github.abel533.easyxls.EasyXls;
import com.github.abel533.easyxls.bean.ExcelConfig;
import org.junit.Test;
import po.Charges;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuzh_3nofxnp
 * @since 2015-10-08 22:03
 */
public class OutputStreamTest {

    @Test
    public void testOutputStream() {
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
            OutputStream os = new FileOutputStream("d:/testOutputStream.xls");
            EasyXls.list2Xls(config, list, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
