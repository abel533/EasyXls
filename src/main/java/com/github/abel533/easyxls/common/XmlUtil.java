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
    private static final String ENCODING = "GBK";

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
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(xmlPath), ENCODING));
            String line = null;
            sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            object = (T) u.unmarshal(new ByteArrayInputStream(xml.getBytes(ENCODING)));
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
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
            throw new NullPointerException("object对象不存在!");
        }
        JAXBContext jc = null;
        Marshaller m = null;
        try {
            jc = JAXBContext.newInstance(object.getClass());
            m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(Marshaller.JAXB_ENCODING, ENCODING);
            m.marshal(object, xml);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            throw new NullPointerException("object对象不存在!");
        }
        JAXBContext jc = null;
        Marshaller m = null;
        String xml = null;
        try {
            jc = JAXBContext.newInstance(object.getClass());
            m = jc.createMarshaller();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            m.marshal(object, bos);
            xml = new String(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return xml;
    }
}
