package com.github.abel533.easyxls.common;

import com.github.abel533.easyxls.bean.ExcelConfig;
import com.github.abel533.easyxls.bean.Field;
import com.github.abel533.easyxls.generater.GenXml;
import jxl.*;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * excel，对导入导出进行封装
 *
 * @author liuzh
 */
public class XlsUtil {

    public static final String EXCEL = ".xls";
    private static Map<String, ExcelConfig> cache = new HashMap<String, ExcelConfig>();

    /**
     * 获取xml配置对象
     *
     * @param xmlPath xml完整路径
     * @return xml配置对象
     */
    private static ExcelConfig getEasyExcel(String xmlPath) {
        ExcelConfig easyExcel = cache.get(xmlPath);
        if (easyExcel == null) {
            easyExcel = XmlConfig.getXmlConfig(xmlPath);
        }
        if (easyExcel == null) {
            throw new RuntimeException("无法获取xml配置文件!");
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
     * @param config  配置
     * @param xlsFile xls文件路径
     * @return List对象
     * @throws Exception
     */
    public static List<?> xls2List(ExcelConfig config, File xlsFile) throws Exception {
        Workbook wb = null;
        List<?> list = null;
        try {
            wb = Workbook.getWorkbook(xlsFile);
            list = workbook2List(config, wb);
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
     * 导入xml到List
     *
     * @param config      配置
     * @param inputStream xls文件流
     * @return List对象
     * @throws Exception
     */
    public static List<?> xls2List(ExcelConfig config, InputStream inputStream) throws Exception {
        Workbook wb = null;
        List<?> list = null;
        try {
            wb = Workbook.getWorkbook(inputStream);
            list = workbook2List(config, wb);
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
        ExcelConfig config = getEasyExcel(xmlPath);
        return workbook2List(config, wb);
    }

    /**
     * workbook转换为list
     *
     * @param config 配置
     * @param wb     excel
     * @return
     * @throws Exception
     */
    public static List<?> workbook2List(ExcelConfig config, Workbook wb) throws Exception {
        String[] names = config.getNames();
        String[] types = config.getTypes();
        Field key = config.getKey();

        List<Object> list = new ArrayList<Object>();
        Sheet sheet = wb.getSheet(config.getSheetNum());
        int length = sheet.getColumns() < names.length ? sheet.getColumns() : names.length;
        //计算行数
        int rowLength = sheet.getRows() < config.getMaxRow() ?
                sheet.getRows() : (config.getMaxRow() > 0 ? (config.getMaxRow() + config.getStartRow()) : sheet.getRows());

        for (int i = config.getStartRow(); i < rowLength; i++) {
            //Map类型要特殊处理
            Class clazz = Class.forName(config.getClazz());
            Object obj = null;
            if (Map.class.isAssignableFrom(clazz)) {
                obj = new HashMap();
            } else {
                obj = clazz.newInstance();
            }
            for (int j = 0; j < length; j++) {
                setValue(obj, names[j], types[j], sheet.getCell(j, i));
            }
            //checkKey
            if (key != null) {
                //当主键为空时，不在继续读取excel
                if (key.get(obj) == null || "".equals(String.valueOf(key.get(obj)))) {
                    break;
                }
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
        } /*else if (cell instanceof FormulaCell) {
            value = ((FormulaCell) cell).getFormula();
        }*/ else {
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
        } else if (Object.class.getCanonicalName().equals(type)) {
            //类型一致的直接使用
            val = v;
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
        Field field = FieldUtil.getField(obj, fieldName);
        if (field != null) {
            field.set(obj, val);
        }
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
            ExcelConfig config = getEasyExcel(xmlPath);
            return list2Xls(config, list, filePath, fileName);
        } catch (Exception e1) {
            return false;
        }
    }

    /**
     * 导出list对象到excel
     *
     * @param list     导出的list
     * @param xmlPath  xml完整路径
     * @param outputStream 输出流
     * @return 处理结果，true成功，false失败
     * @throws Exception
     */
    public static boolean list2Xls(List<?> list, String xmlPath, OutputStream outputStream) throws Exception {
        try {
            ExcelConfig config = getEasyExcel(xmlPath);
            return list2Xls(config, list, outputStream);
        } catch (Exception e1) {
            return false;
        }
    }

    /**
     * 导出list对象到excel
     *
     * @param config   配置
     * @param list     导出的list
     * @param filePath 保存xls路径
     * @param fileName 保存xls文件名
     * @return 处理结果，true成功，false失败
     * @throws Exception
     */
    public static boolean list2Xls(ExcelConfig config, List<?> list, String filePath, String fileName) throws Exception {
        //创建目录
        File file = new File(filePath);
        if (!(file.exists())) {
            if (!file.mkdirs()) {
                throw new RuntimeException("创建导出目录失败!");
            }
        }
        OutputStream outputStream = null;
        try {
            if (!fileName.toLowerCase().endsWith(EXCEL)) {
                fileName += EXCEL;
            }
            File excelFile = new File(filePath + "/" + fileName);
            outputStream = new FileOutputStream(excelFile);
            return list2Xls(config, list, outputStream);
        } catch (Exception e1) {
            return false;
        } finally {
            if(outputStream != null){
                outputStream.close();
            }
        }
    }

    /**
     * 导出list对象到excel
     *
     * @param config   配置
     * @param list     导出的list
     * @param outputStream 输出流
     * @return 处理结果，true成功，false失败
     * @throws Exception
     */
    public static boolean list2Xls(ExcelConfig config, List<?> list, OutputStream outputStream) throws Exception {
        try {
            String[] header = config.getHeaders();
            String[] names = config.getNames();
            String[] values;
            WritableWorkbook wb = Workbook.createWorkbook(outputStream);
            String sheetName = (config.getSheet() != null && !config.getSheet().equals("")) ? config.getSheet() : ("sheet" + config.getSheetNum());
            WritableSheet sheet = wb.createSheet(sheetName, 0);

            int row = 0;
            int column = 0;
            int rowadd = 0;
            //写入标题
            if (config.getHeader()) {
                for (column = 0; column < header.length; column++) {
                    sheet.addCell(new Label(column, row + rowadd, header[column]));
                    if (config.getColumn(column).getWidth() != null) {
                        sheet.setColumnView(column, config.getColumn(column).getWidth() / 7);
                    }
                }
                rowadd++;
            }
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
            field = FieldUtil.getField(source, fieldnames[i]);
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
