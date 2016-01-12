package com.spider.selector;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author code4crafter@gmail.com
 * @since 0.3.0
 */
public abstract class BaseElementSelector implements Selector, ElementSelector {

    private static final long serialVersionUID = 1567007285356383875L;

    @Override
    public String select(String text) {

        if (text != null) {
            return select(Jsoup.parse(text));
        }
        return null;
    }

    @Override
    public List<String> selectList(String text) {

        if (text != null) {
            return selectList(Jsoup.parse(text));
        } else {
            return new ArrayList<>();
        }
    }

}
