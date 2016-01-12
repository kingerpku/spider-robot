package com.spider.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.spider.config.AppConfig;
import com.spider.config.DataBaseTestConfig;
import com.spider.entity.SportteryAllEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.junit.Assert.*;

/**
 * Created by wsy on 2015/11/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataBaseTestConfig.class})
@TestExecutionListeners(value = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@DirtiesContext
@DatabaseSetup(value = {"classpath:sporttery_all.xml"})
public class SportteryAllRepositoryTest {

    @Autowired
    private SportteryAllRepository sportteryAllRepository;

    @Test
    public void findByMatchCode_NoResultFound_ShouldReturnNull() {

        SportteryAllEntity sportteryAllEntity = sportteryAllRepository.findByMatchCode("8001");
        assertTrue(sportteryAllEntity == null);
    }

    @Test
    public void findByMatchCode_OneResultFound_ShouldReturnOneObject() {

        SportteryAllEntity sportteryAllEntity = sportteryAllRepository.findByMatchCode("4001");
        assertTrue(sportteryAllEntity != null);
    }
}