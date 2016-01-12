package com.spider.dao.impl;

import com.spider.domain.GamingCompany;
import com.spider.entity.HiloOdds;
import com.spider.entity.HiloOddsHistory;
import com.spider.repository.HiloOddsHistoryRepository;
import com.spider.repository.HiloOddsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
public class GamingCompanyHiloDaoImplTest {

    @InjectMocks
    private GamingCompanyHiloDaoImpl gamingCompanyHiloDao;

    @Mock
    private HiloOddsRepository hiloOddsRepository;

    @Mock
    private HiloOddsHistoryRepository hiloOddsHistoryRepository;

    @Test
    public void saveOrUpdateHiloOdds_解析赔率列表为空_将赔率置为空表示错误值() {

        List<HiloOdds> queryHiloList = new ArrayList<>();
        List<HiloOddsHistory> hiloList = new ArrayList<>();
        gamingCompanyHiloDao.saveOrUpdateHiloOdds(queryHiloList, hiloList, 1001L, GamingCompany.LiJi.getName());
        verify(hiloOddsRepository).save(isA(HiloOdds.class));
        verifyNoMoreInteractions(hiloOddsRepository);
    }

    @Test
    public void saveOrUpdateHiloOdds_解析出赔率数据但是相比没有变化_不与Repository进行交互() throws Exception {

        List<HiloOdds> queryHiloList = new ArrayList<>();
        HiloOdds hiloOdds = new HiloOdds();
        hiloOdds.setOddsHigh("1.86");
        queryHiloList.add(hiloOdds);
        List<HiloOddsHistory> hiloList = new ArrayList<>();
        HiloOddsHistory hiloOddsHistory = new HiloOddsHistory();
        hiloOddsHistory.setOddsHigh("1.86");
        hiloList.add(hiloOddsHistory);
        gamingCompanyHiloDao.saveOrUpdateHiloOdds(queryHiloList, hiloList, 1001L, GamingCompany.LiJi.getName());
        verifyNoMoreInteractions(hiloOddsRepository);
        verifyNoMoreInteractions(hiloOddsHistoryRepository);
    }

    @Test
    public void saveOrUpdateHiloOdds_解析和查询都有赔率数据并且相比有变化_与Repository和HistoryRepository进行交互() throws Exception {

        List<HiloOdds> queryHiloList = new ArrayList<>();
        HiloOdds hiloOdds = new HiloOdds();
        hiloOdds.setOddsHigh("1.86");
        queryHiloList.add(hiloOdds);
        List<HiloOddsHistory> hiloList = new ArrayList<>();
        HiloOddsHistory hiloOddsHistory = new HiloOddsHistory();
        hiloOddsHistory.setOddsHigh("1.85");
        hiloList.add(hiloOddsHistory);
        gamingCompanyHiloDao.saveOrUpdateHiloOdds(queryHiloList, hiloList, 1001L, GamingCompany.LiJi.getName());
        verify(hiloOddsRepository).save(isA(HiloOdds.class));
        verify(hiloOddsHistoryRepository).save(isA(HiloOddsHistory.class));
        verifyNoMoreInteractions(hiloOddsRepository);
        verifyNoMoreInteractions(hiloOddsHistoryRepository);
    }
}