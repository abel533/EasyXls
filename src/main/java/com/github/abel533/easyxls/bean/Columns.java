package com.github.abel533.easyxls.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "columns")
@XmlAccessorType(XmlAccessType.FIELD)
public class Columns {
    /**
     * 列的配置信息
     */
    @XmlElement(name = "column")
    private List<Column> columns;

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
