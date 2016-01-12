package com.spider.xml;

import com.spider.utils.XmlUtils;
import org.dom4j.Node;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wsy on 2015/10/19.
 */
public class XmlUtilsTest {

    private final String xml = "<?xml version=\"1.0\"?>\n" +
            "<rsp status=\"ok\">\n" +
            "  <sports>\n" +
            "    <sport id=\"1\" feedContents=\"1\">Badminton</sport>\n" +
            "    <sport id=\"2\" feedContents=\"0\">Bandy</sport>\n" +
            "    <sport id=\"29\" feedContents=\"1\">Soccer</sport>\n" +
            "    <sport id=\"54\" feedContents=\"0\">Speed Skating</sport>\n" +
            "  </sports>\n" +
            "</rsp>";

    @Test
    public void testSelectNode() throws Exception {

        Node node = XmlUtils.selectNode(xml, "//sports/sport[@id=2]");
        assertTrue("Bandy".equals(node.getText()));
    }

    @Test
    public void testSelectNodes() throws Exception {

        List<Node> nodes = XmlUtils.selectNodes(xml, "//sports/sport");
        assertTrue("Bandy".equals(nodes.get(1).getText()));
    }
}