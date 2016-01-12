package com.spider.dao.impl;

import com.spider.entity.SportteryAllEntity;
import com.spider.entity.SportteryAllHistoryEntity;
import com.spider.entity.TCrawlerSporttery;
import com.spider.entity.TCrawlerSportteryHistory;
import com.spider.repository.SportteryAllHistoryRepository;
import com.spider.repository.SportteryAllRepository;
import com.spider.repository.TCrawlerSportteryHistoryRepository;
import com.spider.repository.TCrawlerSportteryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by wsy on 2015/12/9.
 *
 * @author wsy
 */
@RunWith(MockitoJUnitRunner.class)
public class SportteryDaoImplTest {

    @InjectMocks
    private SportteryDaoImpl sportteryDao;

    @Mock
    private TCrawlerSportteryRepository tCrawlerSportteryRepository;

    @Mock
    private TCrawlerSportteryHistoryRepository tCrawlerSportteryHistoryRepository;

    @Mock
    private SportteryAllRepository sportteryAllRepository;

    @Mock
    private SportteryAllHistoryRepository sportteryAllHistoryRepository;

    @Test
    public void compareAndUpdateBeans_InsertOneBean_HistoryRepositoryShouldNotBeCalled() {

        List<TCrawlerSporttery> list = new ArrayList<>();
        TCrawlerSporttery e = new TCrawlerSporttery();
        e.setCompetitionNum("1001");
        list.add(e);
        when(tCrawlerSportteryRepository.findByCompetitionNum(isA(String.class))).thenAnswer(new Answer<TCrawlerSporttery>() {
            @Override
            public TCrawlerSporttery answer(InvocationOnMock invocation) throws Throwable {

                return null;
            }
        });
        sportteryDao.compareAndUpdateBeans(list);
        verify(tCrawlerSportteryRepository).findByCompetitionNum(isA(String.class));
        verify(tCrawlerSportteryRepository).save(isA(TCrawlerSporttery.class));
        verifyNoMoreInteractions(tCrawlerSportteryRepository);
    }

    @Test
    public void compareAndUpdateBeans_UpdateOneBean_HistoryRepositoryShouldBeCalled() {

        List<TCrawlerSporttery> list = new ArrayList<>();
        TCrawlerSporttery e = new TCrawlerSporttery();
        e.setDrawProbabilityOne("12.1");
        e.setCompetitionNum("1001");
        list.add(e);
        when(tCrawlerSportteryRepository.findByCompetitionNum(isA(String.class))).thenAnswer(new Answer<TCrawlerSporttery>() {
            @Override
            public TCrawlerSporttery answer(InvocationOnMock invocation) throws Throwable {

                return new TCrawlerSporttery();
            }
        });
        sportteryDao.compareAndUpdateBeans(list);
        verify(tCrawlerSportteryRepository).findByCompetitionNum(isA(String.class));
        verify(tCrawlerSportteryHistoryRepository).save(isA(TCrawlerSportteryHistory.class));
        verify(tCrawlerSportteryRepository).save(isA(TCrawlerSporttery.class));
        verifyNoMoreInteractions(tCrawlerSportteryHistoryRepository);
        verifyNoMoreInteractions(tCrawlerSportteryRepository);
    }

    @Test
    public void compareAndUpdateSportteryAllBeans_InsertOneBean_HistoryRepositoryShouldNotBeCalled() {

        List<SportteryAllEntity> list = new ArrayList<>();
        SportteryAllEntity sportteryAllEntity = new SportteryAllEntity();
        sportteryAllEntity.setMatchCode("1001");
        list.add(sportteryAllEntity);
        when(sportteryAllRepository.findByMatchCode(isA(String.class))).thenReturn(null);
        sportteryDao.compareAndUpdateSportteryAllBeans(list);
        verify(sportteryAllRepository).findByMatchCode(isA(String.class));
        verify(sportteryAllRepository).save(isA(SportteryAllEntity.class));
        verifyNoMoreInteractions(sportteryAllRepository);
    }

    @Test
    public void compareAndUpdateSportteryAllBeans_UpdateOneBean_HistoryRepositoryShouldBeCalled() {

        List<SportteryAllEntity> list = new ArrayList<>();
        SportteryAllEntity sportteryAllEntity = new SportteryAllEntity();
        sportteryAllEntity.setMatchCode("1001");
        list.add(sportteryAllEntity);
        when(sportteryAllRepository.findByMatchCode(isA(String.class))).thenReturn(new SportteryAllEntity());
        sportteryDao.compareAndUpdateSportteryAllBeans(list);
        verify(sportteryAllRepository).findByMatchCode(isA(String.class));
        verify(sportteryAllRepository).save(isA(SportteryAllEntity.class));
        verify(sportteryAllHistoryRepository).save(isA(SportteryAllHistoryEntity.class));
        verifyNoMoreInteractions(sportteryAllRepository);
        verifyNoMoreInteractions(sportteryAllHistoryRepository);
    }
}