package com.github.abel533.easyxls.bean;

import com.github.abel533.easyxls.common.FieldUtil;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * xml对象
 *
 * @author liuzh
 */
@XmlRootElement(name = "excel")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExcelConfig {
    /**
     * 是否缓存-启用缓存后，修改配置不会实时更新
     */
    private Boolean cache;
    /**
     * sheet名 - 写入excel使用
     */
    private String sheet;
    /**
     * Java对象
     */
    @XmlElement(name = "class")
    private String clazz;
    /**
     * 读取第几个sheet
     */
    private int sheetNum;
    /**
     * 从第几行开始读取
     */
    private int startRow;
    /**
     * 读取最大行数
     */
    private int maxRow = -1;
    /**
     * 是否导出标题，默认导出
     */
    private Boolean header = Boolean.TRUE;

    @XmlElement(name = "columns")
    private Columns columns;

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

    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public Columns getColumns() {
        return columns;
    }

    public void setColumns(Columns columns) {
        this.columns = columns;
    }

    public Column getColumn(int index) {
        return getColumns().getColumns().get(index);
    }

    public Boolean getHeader() {
        return header;
    }

    public void setHeader(Boolean header) {
        this.header = header;
    }

    /**
     * 获取excel标题
     *
     * @return
     */
    public String[] getHeaders() {
        String[] headers = new String[columns.getColumns().size()];
        Column column = null;
        for (int i = 0; i < columns.getColumns().size(); i++) {
            column = columns.getColumns().get(i);
            if (column.getHeader() != null && !column.getHeader().equals("")) {
                headers[i] = columns.getColumns().get(i).getHeader();
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
        String[] headers = new String[columns.getColumns().size()];
        for (int i = 0; i < columns.getColumns().size(); i++) {
            headers[i] = columns.getColumns().get(i).getName();
        }
        return headers;
    }

    /**
     * 获取Object的字段类型
     *
     * @return
     */
    public String[] getTypes() {
        String[] types = new String[columns.getColumns().size()];
        for (int i = 0; i < columns.getColumns().size(); i++) {
            types[i] = columns.getColumns().get(i).getType();
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
        for (int i = 0; i < columns.getColumns().size(); i++) {
            if (columns.getColumns().get(i).getKey()) {
                keyName = columns.getColumns().get(i).getName();
                break;
            }
        }
        if (keyName != null && !keyName.equals("")) {
            try {
                Field field = FieldUtil.getField(Class.forName(clazz), keyName);
                return field;
            } catch (Exception e) {
                //ignore
                e.printStackTrace();
            }
        }
        return null;
    }


    public static class Builder {
        private ExcelConfig excel;
        private Class<?> clazz;
        private String key;
        private String separater = ",";

        public Builder(Class<?> clazz) {
            this.clazz = clazz;
            this.excel = new ExcelConfig();
            this.excel.sheetNum = 0;
            this.excel.startRow = 0;
            this.excel.clazz = clazz.getCanonicalName();

            Columns columns = new Columns();
            columns.setColumns(new ArrayList<Column>());
            this.excel.columns = columns;
        }

        public Builder sheetName(String sheetName) {
            this.excel.sheet = sheetName;
            return this;
        }

        public Builder sheetNum(int sheetNum) {
            this.excel.sheetNum = sheetNum;
            return this;
        }

        public Builder startRow(int startRow) {
            this.excel.startRow = startRow;
            return this;
        }

        public Builder maxRow(int maxRow) {
            this.excel.maxRow = maxRow;
            return this;
        }

        public Builder separater(String separater) {
            this.separater = separater;
            return this;
        }

        public Builder header(Boolean header) {
            this.excel.header = header;
            return this;
        }

        /**
         * 只能设置一个key
         *
         * @param key
         * @return
         */
        public Builder key(String key) {
            this.key = key;
            for (Column column : this.excel.columns.getColumns()) {
                if (column.getName() != null && column.getName().equals(key)) {
                    column.setKey(true);
                } else {
                    column.setKey(false);
                }
            }
            return this;
        }

        public Builder addColumn(Column column) {
            if (column.getType() == null || column.getType().equals("")) {
                Field field = FieldUtil.getField(clazz, column.getName());
                column.setType(field.getCanonicalName());
            }
            if (key != null && key.equals(column.getName())) {
                column.setKey(true);
            } else {
                column.setKey(false);
            }
            this.excel.columns.getColumns().add(column);
            return this;
        }

        public Builder addColumn(String... names) {
            if (names != null && names.length > 0) {
                for (String name : names) {
                    if (name.indexOf(separater) > 0) {
                        String[] ns = name.split(separater);
                        switch (ns.length) {
                            case 1:
                                addColumn(new Column(ns[0], ns[0]));
                                break;
                            case 2:
                                addColumn(new Column(ns[0], ns[1]));
                                break;
                            case 3:
                                addColumn(new Column(ns[0], ns[1], Integer.parseInt(ns[2])));
                                break;
                            case 4:
                                addColumn(new Column(ns[0], ns[1], Integer.parseInt(ns[2]), ns[3]));
                                break;
                        }
                    } else {
                        addColumn(new Column(name, name));
                    }
                }
            }
            return this;
        }

        public Builder addColumn(String name, String header) {
            return addColumn(new Column(name, header));
        }

        public Builder addColumn(String name, String header, Integer width) {
            return addColumn(new Column(name, header, width));
        }

        public Builder addColumn(String name, String header, Class<?> type) {
            return addColumn(new Column(name, header, type));
        }

        public Builder addColumn(String name, String header, Integer width, Class<?> type) {
            return addColumn(new Column(name, header, width, type));
        }

        public ExcelConfig build() {
            return this.excel;
        }

    }

}
