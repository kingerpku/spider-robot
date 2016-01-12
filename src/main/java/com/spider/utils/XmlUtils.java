package com.spider.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * xml工具类
 *
 * @author wsy
 */
public class XmlUtils {

    /**
     * 根据xpath找到某个节点
     *
     * @param content xml文件内容
     * @param xpath   xpath表达式
     * @return node
     * @throws DocumentException 解析出错时抛出
     */
    public static Node selectNode(String content, String xpath) throws DocumentException {

        SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(content.getBytes(Charset.forName("utf-8"))));
        return document.selectSingleNode(xpath);
    }

    /**
     * 根据xpath找到多个节点
     *
     * @param content xml文件内容
     * @param xpath   xpath表达式
     * @return nodes
     * @throws DocumentException 解析出错时抛出
     */
    public static List<Node> selectNodes(String content, String xpath) throws DocumentException {

        SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(content.getBytes(Charset.forName("utf-8"))));
        return document.selectNodes(xpath);
    }

    private XmlUtils() {

    }
}
