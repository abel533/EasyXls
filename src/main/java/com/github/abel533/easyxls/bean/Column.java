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
        this(name, header);
        this.width = width;
    }

    public Column(String name, String header, String type) {
        this(name, header);
        this.type = type;
    }

    public Column(String name, String header, Class<?> type) {
        this(name, header, type.getCanonicalName());
    }

    public Column(String name, String header, Integer width, String type) {
        this(name, header);
        this.width = width;
        this.type = type;
    }

    public Column(String name, String header, Integer width, Class<?> type) {
        this(name, header, width, type.getCanonicalName());
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
