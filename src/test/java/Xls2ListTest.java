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
}
