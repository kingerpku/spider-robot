package com.spider.dao.impl;

import com.spider.entity.HadOdds;
import com.spider.entity.HadOddsHistory;
import com.spider.repository.HadOddsHistoryRepository;
import com.spider.repository.HadOddsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by wsy on 2015/12/9.
 *
 * @author wsy
 */
@RunWith(MockitoJUnitRunner.class)
public class GamingCompanyHadDaoImplTest {


    @InjectMocks
    private GamingCompanyHadDaoImpl gamingCompanyHadDao;

    @Mock
    private HadOddsRepository hadOddsRepository;

    @Mock
    private HadOddsHistoryRepository hadOddsHistoryRepository;

    @Test
    public void saveOrUpdateHadOdds_解析赔率列表为空_将赔率置为空表示错误值() {

        List<HadOdds> queryHadList = new ArrayList<>();
        List<HadOddsHistory> hadList = new ArrayList<>();
        gamingCompanyHadDao.saveOrUpdateHadOdds(queryHadList, hadList, 1001L);
        verify(hadOddsRepository).save(isA(HadOdds.class));
        verifyNoMoreInteractions(hadOddsRepository);
    }

    @Test
    public void saveOrUpdateHadOdds_解析出赔率数据但是相比没有变化_不与Repository进行交互() {

        List<HadOdds> queryHadList = new ArrayList<>();
        HadOdds hadOdds = new HadOdds();
        hadOdds.setOddsA("1.86");
        queryHadList.add(hadOdds);
        List<HadOddsHistory> hadList = new ArrayList<>();
        HadOddsHistory hadOddsHistory = new HadOddsHistory();
        hadOddsHistory.setOddsA("1.86");
        hadList.add(hadOddsHistory);
        gamingCompanyHadDao.saveOrUpdateHadOdds(queryHadList, hadList, 1001L);
        verifyNoMoreInteractions(hadOddsRepository);
        verifyNoMoreInteractions(hadOddsHistoryRepository);
    }

    @Test
    public void saveOrUpdateHadOdds_解析和查询都有赔率数据并且相比有变化_与Repository和HistoryRepository进行交互() {

        List<HadOdds> queryHadList = new ArrayList<>();
        HadOdds hadOdds = new HadOdds();
        hadOdds.setOddsA("1.86");
        queryHadList.add(hadOdds);
        List<HadOddsHistory> hadList = new ArrayList<>();
        HadOddsHistory hadOddsHistory = new HadOddsHistory();
        hadOddsHistory.setOddsA("1.85");
        hadList.add(hadOddsHistory);
        gamingCompanyHadDao.saveOrUpdateHadOdds(queryHadList, hadList, 1001L);
        verify(hadOddsRepository).save(isA(HadOdds.class));
        verify(hadOddsHistoryRepository).save(isA(HadOddsHistory.class));
        verifyNoMoreInteractions(hadOddsRepository);
        verifyNoMoreInteractions(hadOddsHistoryRepository);
    }

}
