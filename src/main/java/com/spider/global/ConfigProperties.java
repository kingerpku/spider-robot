package com.spider.global;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.core.io.DefaultResourceLoader;

/**
 *
 * @author wsy
 */
public final class ConfigProperties {

    private static Map<String, String> propertiesMap = new HashMap<>();

    static {
        Properties properties = new Properties();
        try {
            //properties.load(new DefaultResourceLoader().getResource("file:/opt/spider/config.properties").getInputStream());
            properties.load(new DefaultResourceLoader().getResource("classpath:config.properties").getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("config file error");
        }
        Iterator<Entry<Object, Object>> it = properties.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Object, Object> entry = it.next();
            propertiesMap.put((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public static String $(String key) {

        return propertiesMap.get(key);
    }

    private ConfigProperties() {

    }
}
