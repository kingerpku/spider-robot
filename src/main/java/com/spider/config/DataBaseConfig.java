package com.spider.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * 数据库配置，配置了jpa的entity manager，jpa provider，datasource
 *
 * @author wsy
 */
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.spider.repository")
public class DataBaseConfig {


    @Value("${com.mysql.jdbc.Driver}")
    private String PROPERTY_NAME_DATABASE_DRIVER;

    @Value("${jdbc.password}")
    private String PROPERTY_NAME_DATABASE_PASSWORD;

    @Value("${jdbc.url}")
    private String PROPERTY_NAME_DATABASE_URL;

    @Value("${jdbc.username}")
    private String PROPERTY_NAME_DATABASE_USERNAME;

    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";

    @Value("${hibernate.dialect}")
    private String HIBERNATE_DIALECT;

    private String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";

    @Value("${hibernate.show_sql}")
    private String HIBERNATE_SHOW_SQL;

    @Value("${hibernate.hbm2ddl.auto}")
    private String HIBERNATE_HBM2DDL;

    private String PROPERTY_NAME_HIBERNATE_HBM2DDL = "hibernate.hbm2ddl.auto";

    @Value("${connection.auto_connection}")
    private String HIBERNATE_AUTO_RECONNECT;

    private String PROPERTY_NAME_AUTO_RECONNECT = "connection.autoReconnect";


    @Value("${entitymanager.packages.to.scan}")
    private String ENTITYMANAGER_PACKAGES_TO_SCAN;

    @Bean
    public BasicDataSource dataSource() {

//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(PROPERTY_NAME_DATABASE_DRIVER);
//        dataSource.setUrl(PROPERTY_NAME_DATABASE_URL);
//        dataSource.setUsername(PROPERTY_NAME_DATABASE_USERNAME);
//        dataSource.setPassword(PROPERTY_NAME_DATABASE_PASSWORD);
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(PROPERTY_NAME_DATABASE_URL);
        basicDataSource.setUsername(PROPERTY_NAME_DATABASE_USERNAME);
        basicDataSource.setPassword(PROPERTY_NAME_DATABASE_PASSWORD);
        basicDataSource.setDriverClassName(PROPERTY_NAME_DATABASE_DRIVER);
        basicDataSource.setInitialSize(100);
        return basicDataSource;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {

        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN);
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());
        return entityManagerFactoryBean;
    }

    private Properties hibernateProperties() {

        Properties properties = new Properties();
        properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, HIBERNATE_DIALECT);
        properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, HIBERNATE_SHOW_SQL);
        properties.put(PROPERTY_NAME_HIBERNATE_HBM2DDL, HIBERNATE_HBM2DDL);
        //<property name="connection.autoReconnect">true</property>　
        properties.put(PROPERTY_NAME_AUTO_RECONNECT, HIBERNATE_AUTO_RECONNECT);
        return properties;
    }

    @Bean
    public JpaTransactionManager transactionManager() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

}