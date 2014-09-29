package com.github.abel533.easyxls.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "column")
@XmlAccessorType(XmlAccessType.FIELD)
public class Column {
    private String name;
    private String header;
    private Integer width;
    private String type;
    //一个配置中只有第一个key能起作用
    private Boolean key = Boolean.FALSE;

    public Column() {
    }

    public Column(String name, String header) {
        this.name = name;
        this.header = header;
    }

    public Column(String name, String header, Integer width) {
        this.name = name;
        this.header = header;
        this.width = width;
    }

    public Column(String name, String header, Boolean key) {
        this.name = name;
        this.header = header;
        this.key = key;
    }

    public Column(String name, String header, Integer width, Boolean key) {
        this.name = name;
        this.header = header;
        this.width = width;
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Boolean getKey() {
        return key;
    }

    public void setKey(Boolean key) {
        this.key = key;
    }
}
