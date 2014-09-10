package com.github.abel533.easyxls;

import com.github.abel533.easyxls.bean.EasyExcel;
import com.github.abel533.easyxls.common.DateUtil;
import com.github.abel533.easyxls.common.XmlConfig;
import com.github.abel533.easyxls.generater.GenXml;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.*;

import java.io.File;
import java.lang.Boolean;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * excel，对导入导出进行封装
 *
 * @author liuzh
 */
public class EasyXls {

    private static Map<String, EasyExcel> cache = new HashMap<String, EasyExcel>();

    public static final String EXCEL = ".xls";

    /**
     * 获取xml配置对象
     *
     * @param xmlPath xml完整路径
     * @return xml配置对象
     */
    private static EasyExcel getDlExcel(String xmlPath) {
        EasyExcel dlExcel = cache.get(xmlPath);
        if (dlExcel == null) {
            dlExcel = XmlConfig.getXmlConfig(xmlPath);
        }
        if (dlExcel == null) {
            throw new RuntimeException("获取xml配置文件出错!");
        }
        if (dlExcel.getCache() != null && dlExcel.getCache()) {
            cache.put(xmlPath, dlExcel);
        }
        return dlExcel;
    }

    /**
     * 打开代码生成器，请在项目中执行，只有这样才能加载相应的类
     */
    public static void openGenerater(){
        GenXml.run();
    }

    /**
     * 导入xml到List
     *
     * @param xmlPath xml完整路径
     * @param xlsFile xls文件路径
     * @return List对象
     * @throws Exception
     */
    public static List<?> xls2List(String xmlPath, File xlsFile) throws Exception {
        List<Object> list = new ArrayList<Object>();
        try {
            //获取配置文件
            EasyExcel config = getDlExcel(xmlPath);
            String[] names = config.getNames();
            String[] types = config.getTypes();

            Workbook wb = Workbook.getWorkbook(xlsFile);
            Sheet sheet = wb.getSheet(config.getSheetNum());

            for (int i = config.getStartRow(); i < sheet.getRows(); i++) {
                Object obj = Class.forName(config.getClazz()).newInstance();
                for (int j = 0; j < sheet.getColumns(); j++) {
                    setValue(obj, names[j], types[j], sheet.getCell(j, i).getContents());
                }
                list.add(obj);
            }
        } catch (Exception e) {
            throw new Exception("转换xls出错:" + e.getMessage());
        }
        return list;
    }

    /**
     * 跟对象obj的某个field赋值value
     *
     * @param obj       属性对象
     * @param fieldName 字段名
     * @param value     字段值
     * @throws Exception
     */
    private static void setValue(Object obj, String fieldName, String type, String value) throws Exception {
        Object val = null;
        /**
         * 对类型进行转换，支持int,long,float,double,boolean,Integer,Long,Double,Float,Date,String
         */
        if (type.equals("int")) {
            val = Integer.parseInt(value);
        } else if (type.equals("long")) {
            val = Long.parseLong(value);
        } else if (type.equals("float")) {
            val = Float.parseFloat(value);
        } else if (type.equals("double")) {
            val = Double.parseDouble(value);
        } else if (type.equals("boolean")) {
            val = Boolean.parseBoolean(value);
        } else {
            Class clazz = Class.forName(type);
            if (!clazz.equals(String.class)) {
                if (clazz.equals(Date.class)) {
                    val = DateUtil.smartFormat(value);
                } else if (clazz.equals(Integer.class)) {
                    val = Integer.valueOf(value);
                } else if (clazz.equals(Long.class)) {
                    val = Long.valueOf(value);
                } else if (clazz.equals(Float.class)) {
                    val = Float.valueOf(value);
                } else if (clazz.equals(Double.class)) {
                    val = Double.valueOf(value);
                } else if (clazz.equals(Boolean.class)) {
                    val = Boolean.valueOf(value);
                } else if (clazz.equals(BigDecimal.class)) {
                    val = new BigDecimal(value);
                }
            } else {
                val = value;
            }
        }
        Field field = getField(obj, fieldName);
        if (field != null) {
            field.set(obj, val);
        }
    }

    /**
     * 获取字段中的Field
     *
     * @param source    对象
     * @param fieldName 字段名
     * @return 返回字段对象
     */
    private static Field getField(Object source, String fieldName) {
        Field field = null;
        Class clazz = source.getClass();
        while (field == null && clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
            } catch (Exception e) {
                clazz = clazz.getSuperclass();
            }
        }
        return field;
    }


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
    public static boolean list2Xls(List<?> list, String xmlPath, String filePath, String fileName) throws Exception {
        //创建目录
        File file = new File(filePath);
        if (!(file.exists())) {
            if (!file.mkdirs()) {
                throw new RuntimeException("创建导出目录失败!");
            }
        }
        try {
            EasyExcel config = getDlExcel(xmlPath);
            String[] header = config.getHeaders();
            String[] names = config.getNames();
            String[] values;

            if (!fileName.toUpperCase().endsWith(EXCEL)) {
                fileName += EXCEL;
            }
            File excelFile = new File(filePath + "/" + fileName);

            WritableWorkbook wb = Workbook.createWorkbook(excelFile);
            WritableSheet sheet = wb.createSheet((config.getSheet() != null && !config.getSheet().equals("")) ? config.getSheet() : "sheet", 0);

            int row = 0;
            int column = 0;
            int rowadd = 0;
            //写入title
            if (config.getTitle() != null && !config.getTitle().equals("")) {
                WritableFont wtf = new WritableFont(WritableFont.createFont("宋体"), 20, WritableFont.BOLD, false);
                WritableCellFormat cellFormat = new WritableCellFormat(wtf);
                cellFormat.setAlignment(Alignment.CENTRE);
                cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

                sheet.mergeCells(column, row + rowadd, header.length - 1, row + rowadd);
                sheet.setRowView(row + rowadd, 1000);
                sheet.addCell(new Label(column, row + rowadd, config.getTitle(), cellFormat));
                rowadd++;
            }
            //写入说明
            if (config.getDescription() != null && !config.getDescription().equals("")) {
                WritableFont wtf = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD, false);
                WritableCellFormat cellFormat = new WritableCellFormat(wtf);
                cellFormat.setAlignment(Alignment.LEFT);
                cellFormat.setVerticalAlignment(VerticalAlignment.JUSTIFY);

                sheet.mergeCells(column, row + rowadd, header.length - 1, row + rowadd);
                sheet.setRowView(row + rowadd, 1000);
                sheet.addCell(new Label(column, row + rowadd, "    " + config.getDescription(), cellFormat));
                rowadd++;
            }
            //写入标题
            for (column = 0; column < header.length; column++) {
                sheet.addCell(new Label(column, row + rowadd, header[column]));
                if (config.getDlColumns().getColumns().get(column).getWidth() != null && !config.getDlColumns().getColumns().get(column).getWidth().equals("")) {
                    sheet.setColumnView(column, Integer.parseInt(config.getDlColumns().getColumns().get(column).getWidth()) / 7);
                }
            }
            rowadd++;
            //写入内容//行
            for (row = 0; row < list.size(); row++) {
                Object rowData = list.get(row);
                values = getObjValues(rowData, names);
                //列
                for (column = 0; column < values.length; column++) {
                    sheet.addCell(new Label(column, row + rowadd, values[column]));
                }
            }
            wb.write();
            wb.close();
        } catch (Exception e1) {
            return false;
        }
        return true;
    }

    /**
     * 获取对象指定字段值
     *
     * @param source     对象
     * @param fieldnames 属性名数组
     * @return 对象的字符串数组，和属性名对应
     * @throws Exception
     */
    private static String[] getObjValues(Object source, String... fieldnames) throws Exception {
        String[] results = new String[fieldnames.length];
        Field field;
        String value;
        Object obj;
        for (int i = 0; i < fieldnames.length; i++) {
            field = getField(source, fieldnames[i]);
            if (field == null) {
                continue;
            }
            obj = field.get(source);
            if (obj == null) {
                value = "";
            } else if (obj instanceof Date) {
                value = DateUtil.smartFormat((Date) obj);
            } else {
                value = String.valueOf(obj);
            }
            results[i] = value;
        }
        return results;
    }
}
