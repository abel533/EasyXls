package com.github.abel533.easyxls.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "columns")
@XmlAccessorType(XmlAccessType.FIELD)
public class DlColumns {
    /**
     * 列的配置信息
     */
    @XmlElement(name = "column")
    private List<DlColumn> columns;

    public List<DlColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DlColumn> columns) {
        this.columns = columns;
    }
}
