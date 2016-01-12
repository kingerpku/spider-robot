package com.spider.dao.impl;

import com.spider.dao.CompanyOddsDao;
import com.spider.entity.CompanyOddsEntity;
import com.spider.entity.CompanyOddsHistoryEntity;
import com.spider.repository.CompanyOddsHistoryRepository;
import com.spider.repository.CompanyOddsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wsy on 2016/1/8.
 */
@Repository
public class CompanyOddsDaoImpl implements CompanyOddsDao {

    @Autowired
    private CompanyOddsHistoryRepository companyOddsHistoryRepository;

    @Autowired
    private CompanyOddsRepository companyOddsRepository;

    @Override
    @Transactional
    public void saveOrUpdate(List<CompanyOddsEntity> list, Integer oddsType) {

        if (list.size() != 0) {
            CompanyOddsEntity companyOddsEntity = list.get(0);
            try {
                if (companyOddsEntity.valid()) {
                    CompanyOddsEntity query = companyOddsRepository.findByEuropeIdAndOddsTypeAndGamingCompany(
                            companyOddsEntity.getEuropeId(), oddsType, companyOddsEntity.getGamingCompany());
                    if (query == null) {
                        companyOddsRepository.save(companyOddsEntity);
                        companyOddsHistoryRepository.save(new CompanyOddsHistoryEntity(companyOddsEntity));
                    } else {
                        if (!query.equals(companyOddsEntity)) {
                            companyOddsEntity.setId(query.getId());
                            companyOddsRepository.save(companyOddsEntity);
                            companyOddsHistoryRepository.save(new CompanyOddsHistoryEntity(query));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(companyOddsEntity);
                e.printStackTrace();
            }
        } else {
            return;
        }
    }
}
