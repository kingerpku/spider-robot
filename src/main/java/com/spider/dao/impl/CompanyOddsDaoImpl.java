package com.spider.dao.impl;

import com.spider.dao.CompanyOddsDao;
import com.spider.entity.CompanyOddsEntity;
import com.spider.entity.CompanyOddsHistoryEntity;
import com.spider.repository.CompanyOddsHistoryRepository;
import com.spider.repository.CompanyOddsRepository;
import com.spider.utils.LogHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wsy on 2016/1/8.
 *
 * @author wsy
 */
@Repository
public class CompanyOddsDaoImpl implements CompanyOddsDao {

    private Logger logger = Logger.getLogger("company_odds_logger");

    @Autowired
    private CompanyOddsHistoryRepository companyOddsHistoryRepository;

    @Autowired
    private CompanyOddsRepository companyOddsRepository;

    @Override
    public void saveOrUpdate(List<CompanyOddsEntity> list, Integer oddsType) {

        if (list.size() != 0) {
            CompanyOddsEntity companyOddsEntity = list.get(0);
            try {
                if (companyOddsEntity.valid()) {
                    CompanyOddsEntity query = companyOddsRepository.findByEuropeIdAndOddsTypeAndGamingCompany(
                            companyOddsEntity.getEuropeId(), oddsType, companyOddsEntity.getGamingCompany());
                    if (query == null) {
                        companyOddsRepository.save(companyOddsEntity);
                        LogHelper.persist(logger, "save companyOddsEntity, " + companyOddsEntity);
                        CompanyOddsHistoryEntity companyOddsHistoryEntity = new CompanyOddsHistoryEntity(companyOddsEntity);
                        companyOddsHistoryRepository.save(companyOddsHistoryEntity);
                        LogHelper.persist(logger, "save companyOddsHistoryEntity, " + companyOddsHistoryEntity);
                    } else {
                        if (!query.equals(companyOddsEntity)) {
                            companyOddsEntity.setId(query.getId());
                            companyOddsRepository.save(companyOddsEntity);
                            LogHelper.persist(logger, "update companyOddsEntity, " + companyOddsEntity);
                            CompanyOddsHistoryEntity companyOddsHistoryEntity = new CompanyOddsHistoryEntity(query);
                            companyOddsHistoryRepository.save(companyOddsHistoryEntity);
                            LogHelper.persist(logger, "save companyOddsHistoryEntity, " + companyOddsHistoryEntity);
                        }
                    }
                }
            } catch (Exception e) {
                LogHelper.error(logger, "Exception", e);
            }
        } else {
            LogHelper.persist(logger, "same as odds in database");
            return;
        }
    }
}
