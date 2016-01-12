package com.spider.dao.impl;

import com.spider.dao.impl.GamingCompanyHdcDaoImpl;
import com.spider.domain.GamingCompany;
import com.spider.entity.HdcOdds;
import com.spider.entity.HdcOddsHistory;
import com.spider.repository.HdcOddsHistoryRepository;
import com.spider.repository.HdcOddsRepository;
import com.spider.repository.HdcOddsHistoryRepository;
import com.spider.repository.HdcOddsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

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
public class GamingCompanyHdcDaoImplTest {

    @InjectMocks
    private GamingCompanyHdcDaoImpl gamingCompanyHdcDao;

    @Mock
    private HdcOddsRepository hdcOddsRepository;

    @Mock
    private HdcOddsHistoryRepository hdcOddsHistoryRepository;

    @Test
    public void saveOrUpdateHdcOdds_解析赔率列表为空_将赔率置为空表示错误值() {

        List<HdcOdds> queryHdcList = new ArrayList<>();
        List<HdcOddsHistory> hdcList = new ArrayList<>();
        gamingCompanyHdcDao.saveOrUpdateHdcOdds(queryHdcList, hdcList, 1001L, GamingCompany.LiJi.getName());
        verify(hdcOddsRepository).save(isA(HdcOdds.class));
        verifyNoMoreInteractions(hdcOddsRepository);
    }

    @Test
    public void saveOrUpdateHdcOdds_解析出赔率数据但是相比没有变化_不与Repository进行交互() throws Exception {

        List<HdcOdds> queryHdcList = new ArrayList<>();
        HdcOdds hdcOdds = new HdcOdds();
        hdcOdds.setOddsOne("1.86");
        queryHdcList.add(hdcOdds);
        List<HdcOddsHistory> hdcList = new ArrayList<>();
        HdcOddsHistory hdcOddsHistory = new HdcOddsHistory();
        hdcOddsHistory.setOddsOne("1.86");
        hdcList.add(hdcOddsHistory);
        gamingCompanyHdcDao.saveOrUpdateHdcOdds(queryHdcList, hdcList, 1001L, GamingCompany.LiJi.getName());
        verifyNoMoreInteractions(hdcOddsRepository);
        verifyNoMoreInteractions(hdcOddsHistoryRepository);
    }

    @Test
    public void saveOrUpdateHdcOdds_解析和查询都有赔率数据并且相比有变化_与Repository和HistoryRepository进行交互() throws Exception {

        List<HdcOdds> queryHdcList = new ArrayList<>();
        HdcOdds hdcOdds = new HdcOdds();
        hdcOdds.setOddsOne("1.86");
        queryHdcList.add(hdcOdds);
        List<HdcOddsHistory> hdcList = new ArrayList<>();
        HdcOddsHistory hdcOddsHistory = new HdcOddsHistory();
        hdcOddsHistory.setOddsOne("1.85");
        hdcList.add(hdcOddsHistory);
        gamingCompanyHdcDao.saveOrUpdateHdcOdds(queryHdcList, hdcList, 1001L, GamingCompany.LiJi.getName());
        verify(hdcOddsRepository).save(isA(HdcOdds.class));
        verify(hdcOddsHistoryRepository).save(isA(HdcOddsHistory.class));
        verifyNoMoreInteractions(hdcOddsRepository);
        verifyNoMoreInteractions(hdcOddsHistoryRepository);
    }
}