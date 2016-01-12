package com.spider.repository.specifications;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.spider.entity.TCrawlerSporttery;

public class SpotterySpecifications {

    public static Specification<TCrawlerSporttery> startDateTimeBetween(final Date startDate, final Date endDate) {

        return new Specification<TCrawlerSporttery>() {

            @Override
            public Predicate toPredicate(Root<TCrawlerSporttery> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                return cb.between(root.<Date> get("startDateTime"), startDate, endDate);
            }

        };
    }

    public static Specification<TCrawlerSporttery> equalsCompetitionNum(final String competitionNum) {

        return new Specification<TCrawlerSporttery>() {

            @Override
            public Predicate toPredicate(Root<TCrawlerSporttery> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                return cb.equal(root.<String> get("competitionNum"), competitionNum);
            }

        };
    }
}