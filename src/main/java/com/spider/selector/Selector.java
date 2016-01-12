package com.spider.selector;

import java.io.Serializable;
import java.util.List;

/**
 * Selector(extractor) for text.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public interface Selector extends Serializable {

    /**
     * Extract single result in text.<br>
     * If there are more than one result, only the first will be chosen.
     *
     * @param text
     * @return result
     */
    String select(String text);

    /**
     * Extract all results in text.<br>
     *
     * @param text
     * @return found results, if no result, return an empty list rather than null
     */
    List<String> selectList(String text);

}
