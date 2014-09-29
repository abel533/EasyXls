package com.github.abel533.easyxls.common;


import com.github.abel533.easyxls.bean.ExcelConfig;

import java.io.File;

/**
 * 读取xml文件
 *
 * @author liuzh
 */
public class XmlConfig {

    /**
     * 获取xml配置对象
     *
     * @param xmlPath xml完整路径
     * @return xml配置对象
     */
    public static ExcelConfig getXmlConfig(String xmlPath) {
        return XmlUtil.fromXml(new File(xmlPath), ExcelConfig.class);
    }

    /**
     * 写入到xml文件
     *
     * @param dlExcel 配置对象
     * @param xmlPath xml保存路径
     * @return true成功，false失败
     * @throws Exception
     */
    public static boolean WriteXml(ExcelConfig dlExcel, String xmlPath) throws Exception {
        //写入xml
        return XmlUtil.toXml(dlExcel, new File(xmlPath));
    }

}
