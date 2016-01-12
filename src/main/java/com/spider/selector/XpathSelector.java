package com.spider.selector;

import org.jsoup.nodes.Element;

import us.codecraft.xsoup.XPathEvaluator;
import us.codecraft.xsoup.Xsoup;

import java.util.ArrayList;
import java.util.List;

/**
 * XPath selector based on Xsoup.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @author wsy
 * @since 0.3.0
 */
public class XpathSelector extends BaseElementSelector {

    private static final long serialVersionUID = -3270809873535701660L;

    private XPathEvaluator xPathEvaluator;

    public XpathSelector(String xpathStr) {

        this.xPathEvaluator = Xsoup.compile(xpathStr);
    }

    @Override
    public String select(Element element) {

        String result = xPathEvaluator.evaluate(element).get();
        return result == null ? "" : result;
    }

    @Override
    public List<String> selectList(Element element) {

        List<String> result = xPathEvaluator.evaluate(element).list();
        return result == null ? new ArrayList<String>() : result;
    }
}
