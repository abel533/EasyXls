package com.github.abel533.easyxls.bean;

import com.github.abel533.easyxls.EasyXls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.Field;

/**
 * xml对象
 *
 * @author liuzh
 */
@XmlRootElement(name = "excel")
@XmlAccessorType(XmlAccessType.FIELD)
public class EasyExcel {
    /**
     * 是否缓存-启用缓存后，修改配置不会实时更新
     */
    private Boolean cache;
    /**
     * excel标题
     */
    private String title;
    /**
     * 文件说明
     */
    private String description;
    /**
     * sheet名
     */
    private String sheet;
    /**
     * Java对象
     */
    @XmlElement(name = "class")
    private String clazz;
    /**
     * 第几个sheet
     */
    private int sheetNum;
    /**
     * 从第几行开始读取
     */
    private int startRow;

    @XmlElement(name = "columns")
    private Columns dlColumns;

    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    public int getSheetNum() {
        return sheetNum;
    }

    public void setSheetNum(int sheetNum) {
        this.sheetNum = sheetNum;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public Columns getDlColumns() {
        return dlColumns;
    }

    public void setDlColumns(Columns dlColumns) {
        this.dlColumns = dlColumns;
    }

    /**
     * 获取excel标题
     *
     * @return
     */
    public String[] getHeaders() {
        String[] headers = new String[dlColumns.getColumns().size()];
        Column column = null;
        for (int i = 0; i < dlColumns.getColumns().size(); i++) {
            column = dlColumns.getColumns().get(i);
            if (column.getHeader() != null && !column.getHeader().equals("")) {
                headers[i] = dlColumns.getColumns().get(i).getHeader();
            } else {
                headers[i] = column.getName();
            }
        }
        return headers;
    }

    /**
     * 获取Object要输出的字段名
     *
     * @return
     */
    public String[] getNames() {
        String[] headers = new String[dlColumns.getColumns().size()];
        for (int i = 0; i < dlColumns.getColumns().size(); i++) {
            headers[i] = dlColumns.getColumns().get(i).getName();
        }
        return headers;
    }

    /**
     * 获取Object的字段类型
     *
     * @return
     */
    public String[] getTypes() {
        String[] types = new String[dlColumns.getColumns().size()];
        for (int i = 0; i < dlColumns.getColumns().size(); i++) {
            types[i] = dlColumns.getColumns().get(i).getType();
        }
        return types;
    }

    /**
     * 获取key值
     *
     * @return
     */
    public Field getKey() {
        String keyName = null;
        for (int i = 0; i < dlColumns.getColumns().size(); i++) {
            if (dlColumns.getColumns().get(i).getKey()) {
                keyName = dlColumns.getColumns().get(i).getName();
                break;
            }
        }
        if (keyName != null && !keyName.equals("")) {
            try {
                Field field = EasyXls.getField(Class.forName(clazz), keyName);
                field.setAccessible(true);
                return field;
            } catch (Exception e) {
                //ignore
                e.printStackTrace();
            }
        }
        return null;
    }

}
