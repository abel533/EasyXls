package com.github.abel533.easyxls.common;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * xml和对象互转
 *
 * @author liuzh
 */
public class XmlUtil {

    /**
     * 从xml文件构建
     *
     * @param xmlPath
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromXml(File xmlPath, Class<T> type) {
        BufferedReader reader = null;
        StringBuilder sb = null;
        try {
            reader = new BufferedReader(new FileReader(xmlPath));
            String line = null;
            sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
        return fromXml(sb.toString(), type);
    }

    /**
     * 从xml构建
     *
     * @param xml
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromXml(String xml, Class<T> type) {
        if (xml == null || xml.trim().equals("")) {
            return null;
        }
        JAXBContext jc = null;
        Unmarshaller u = null;
        T object = null;
        try {
            jc = JAXBContext.newInstance(type);
            u = jc.createUnmarshaller();
            object = (T) u.unmarshal(new ByteArrayInputStream(xml.getBytes()));
        } catch (JAXBException e) {
            return null;
        }
        return object;
    }

    /**
     * 对象转xml并保存到文件
     *
     * @param object
     * @return
     */
    public static boolean toXml(Object object, File xml) {
        if (object == null) {
            return false;
        }
        JAXBContext jc = null;
        Marshaller m = null;
        try {
            jc = JAXBContext.newInstance(object.getClass());
            m = jc.createMarshaller();
            m.marshal(object, xml);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 对象转xml
     *
     * @param object
     * @return
     */
    public static String toXml(Object object) {
        if (object == null) {
            return null;
        }
        JAXBContext jc = null;
        Marshaller m = null;
        String xml = null;
        try {
            jc = JAXBContext.newInstance(object.getClass());
            m = jc.createMarshaller();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            m.marshal(object, bos);
            xml = new String(bos.toByteArray(), "utf8");
        } catch (Exception e) {
            return null;
        }
        return xml;
    }
}