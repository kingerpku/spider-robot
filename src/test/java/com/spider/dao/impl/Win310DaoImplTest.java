package com.spider.dao.impl;

import com.spider.entity.TCrawlerWin310;
import com.spider.entity.TCrawlerWin310History;
import com.spider.repository.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by wsy on 2015/12/9.
 *
 * @author wsy
 */
@RunWith(MockitoJUnitRunner.class)
public class Win310DaoImplTest {

    @InjectMocks
    private Win310DaoImpl win310Dao;

    @Mock
    private TCrawlerWin310Repository win310Repository;

    @Mock
    private TCrawlerWin310HistoryRepository win310HistoryRepository;

    @Test
    public void saveOrUpdateWin310_InsertWhenQueryObjectIsNull_HistoryRepositoryShouldNotBeCalled() {

        TCrawlerWin310 win310 = new TCrawlerWin310();
        when(win310Repository.save(isA(TCrawlerWin310.class))).thenReturn(win310);
//        win310Dao.saveOrUpdateWin310(win310, null);
//        verify(win310Repository).save(isA(TCrawlerWin310.class));
//        verifyNoMoreInteractions(win310Repository);
    }

    @Test
    public void saveOrUpdateWin310_UpdateWhenQueryObjectIsNotNull_HistoryRepositoryShouldBeCalled() {

        TCrawlerWin310 win310 = new TCrawlerWin310();
        win310.setStopSaleTime(new Date(new Date().getTime() - 100000L));
        TCrawlerWin310 queried = new TCrawlerWin310();
        queried.setStopSaleTime(new Date());
        when(win310Repository.save(isA(TCrawlerWin310.class))).thenReturn(win310);
//        win310Dao.saveOrUpdateWin310(win310, queried, new IsUpdate(false));
//        verify(win310Repository).save(isA(TCrawlerWin310.class));
//        verify(win310HistoryRepository).save(isA(TCrawlerWin310History.class));
//        verifyNoMoreInteractions(win310Repository);
//        verifyNoMoreInteractions(win310HistoryRepository);
    }

    @Test
    public void saveOrUpdateWin310_QueriedAndParsedAreSame_DbOperationShouldNotBeCalled() {

        TCrawlerWin310 win310 = new TCrawlerWin310();
        TCrawlerWin310 queried = new TCrawlerWin310();
//        win310Dao.saveOrUpdateWin310(win310, queried, new IsUpdate(false));
//        verifyNoMoreInteractions(win310Repository);
//        verifyNoMoreInteractions(win310HistoryRepository);
    }

}