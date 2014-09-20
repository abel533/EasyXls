package com.github.abel533.easyxls;

import com.github.abel533.easyxls.bean.EasyExcel;
import com.github.abel533.easyxls.common.DateUtil;
import com.github.abel533.easyxls.common.XmlConfig;
import com.github.abel533.easyxls.generater.GenXml;
import jxl.*;
import jxl.write.*;

import java.io.File;
import java.io.InputStream;
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

    public static final String EXCEL = ".xls";
    private static Map<String, EasyExcel> cache = new HashMap<String, EasyExcel>();

    /**
     * 获取xml配置对象
     *
     * @param xmlPath xml完整路径
     * @return xml配置对象
     */
    private static EasyExcel getEasyExcel(String xmlPath) {
        EasyExcel easyExcel = cache.get(xmlPath);
        if (easyExcel == null) {
            easyExcel = XmlConfig.getXmlConfig(xmlPath);
        }
        if (easyExcel == null) {
            throw new RuntimeException("获取xml配置文件出错!");
        }
        if (easyExcel.getCache() == null || easyExcel.getCache()) {
            cache.put(xmlPath, easyExcel);
        }
        return easyExcel;
    }

    /**
     * 打开代码生成器，请在项目中执行，只有这样才能加载相应的类
     */
    public static void openGenerater() {
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
        Workbook wb = null;
        List<?> list = null;
        try {
            wb = Workbook.getWorkbook(xlsFile);
            list = workbook2List(xmlPath, wb);
        } catch (Exception e) {
            throw new Exception("转换xls出错:" + e.getMessage());
        } finally {
            if (wb != null) {
                wb.close();
            }
        }
        return list;
    }

    /**
     * 导入xml到List
     *
     * @param xmlPath     xml完整路径
     * @param inputStream xls文件流
     * @return List对象
     * @throws Exception
     */
    public static List<?> xls2List(String xmlPath, InputStream inputStream) throws Exception {
        Workbook wb = null;
        List<?> list = null;
        try {
            wb = Workbook.getWorkbook(inputStream);
            list = workbook2List(xmlPath, wb);
        } catch (Exception e) {
            throw new Exception("转换xls出错:" + e.getMessage());
        } finally {
            if (wb != null) {
                wb.close();
            }
        }
        return list;
    }

    /**
     * workbook转换为list
     *
     * @param xmlPath
     * @param wb
     * @return
     * @throws Exception
     */
    public static List<?> workbook2List(String xmlPath, Workbook wb) throws Exception {
        //获取配置文件
        EasyExcel config = getEasyExcel(xmlPath);
        String[] names = config.getNames();
        String[] types = config.getTypes();

        List<Object> list = new ArrayList<Object>();
        Sheet sheet = wb.getSheet(config.getSheetNum());
        for (int i = config.getStartRow(); i < sheet.getRows(); i++) {
            Object obj = Class.forName(config.getClazz()).newInstance();
            for (int j = 0; j < names.length; j++) {
                setValue(obj, names[j], types[j], sheet.getCell(j, i));
            }
            list.add(obj);
        }
        return list;
    }

    /**
     * 获取单元格的数据
     *
     * @param cell
     * @return
     * @throws Exception
     */
    private static Object getCellValue(Cell cell) throws Exception {
        Object value = null;
        if (cell instanceof ErrorCell) {
            value = null;
        } else if (cell instanceof LabelCell) {
            value = ((LabelCell) cell).getString();
        } else if (cell instanceof NumberCell) {
            value = ((NumberCell) cell).getValue();
        } else if (cell instanceof DateCell) {
            value = ((DateCell) cell).getDate();
        } else if (cell instanceof BooleanCell) {
            value = ((BooleanCell) cell).getValue();
        } else if (cell instanceof FormulaCell) {
            value = ((FormulaCell) cell).getFormula();
        } else {
            value = cell.getContents();
        }
        return value;
    }

    /**
     * 跟对象obj的某个field赋值value
     *
     * @param obj       属性对象
     * @param fieldName 字段名
     * @param cell      单元格
     * @throws Exception
     */
    private static void setValue(Object obj, String fieldName, String type, Cell cell) throws Exception {
        Object val = null;
        Object v = getCellValue(cell);
        if (v == null) {
            //不处理
        } else if (v.getClass().getName().equals(type)) {
            //类型一致的直接使用
            val = v;
        } else {
            //类型不一致进行转换
            String value = v.toString();
            if (value != null && !value.trim().equals("")) {
                value = value.trim();
                /**
                 * 对类型进行转换，支持int,long,float,double,boolean,Integer,Long,Double,Float,Date,String
                 */
                if (type.equals("int")) {
                    val = new BigDecimal(value).intValue();
                } else if (type.equals("long")) {
                    val = new BigDecimal(value).longValue();
                } else if (type.equals("float")) {
                    val = new BigDecimal(value).floatValue();
                } else if (type.equals("double")) {
                    val = new BigDecimal(value).doubleValue();
                } else if (type.equals("boolean")) {
                    val = Boolean.parseBoolean(value);
                } else {
                    Class clazz = Class.forName(type);
                    if (!clazz.equals(String.class)) {
                        if (clazz.equals(Date.class)) {
                            val = DateUtil.smartFormat(value);
                        } else if (clazz.equals(Integer.class)) {
                            val = new BigDecimal(value).intValue();
                        } else if (clazz.equals(Long.class)) {
                            val = new BigDecimal(value).longValue();
                        } else if (clazz.equals(Float.class)) {
                            val = new BigDecimal(value).floatValue();
                        } else if (clazz.equals(Double.class)) {
                            val = new BigDecimal(value).doubleValue();
                        } else if (clazz.equals(Boolean.class)) {
                            val = Boolean.parseBoolean(value);
                        } else if (clazz.equals(BigDecimal.class)) {
                            val = new BigDecimal(value);
                        }
                    } else {
                        val = value;
                    }
                }
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
    public static Field getField(Object source, String fieldName) {
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
            EasyExcel config = getEasyExcel(xmlPath);
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
