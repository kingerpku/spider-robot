package com.spider.service;

import com.spider.entity.ServiceStateEntity;
import com.spider.entity.ServiceStateHistoryEntity;
import com.spider.repository.ServiceStateHistoryRepository;
import com.spider.repository.ServiceStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author wsy
 */
@Service
public class HeartBeatServiceImpl implements HeartBeatService {

    @Autowired
    private ServiceStateRepository serviceStateRepository;

    @Autowired
    private ServiceStateHistoryRepository serviceStateHistoryRepository;

    @Override
    public synchronized void heartBeat(String serviceName, Date start, Date end, boolean isSuccess, String errorMsg) {

        ServiceStateEntity serviceStateEntity = serviceStateRepository.findByService(serviceName);
        if (serviceStateEntity == null) {
            serviceStateEntity = new ServiceStateEntity();
            serviceStateEntity.setService(serviceName);
            serviceStateEntity.setStartTime(start.getTime());
            serviceStateEntity.setEndTime(end.getTime());
            serviceStateEntity.setSuccess(isSuccess);
            if (errorMsg != null) {
                serviceStateEntity.setErrorMsg(errorMsg);
            }
            serviceStateRepository.save(serviceStateEntity);
        } else {
            ServiceStateHistoryEntity serviceStateHistoryEntity = new ServiceStateHistoryEntity(serviceStateEntity);
            serviceStateHistoryRepository.save(serviceStateHistoryEntity);
            serviceStateEntity.setStartTime(start.getTime());
            serviceStateEntity.setEndTime(end.getTime());
            serviceStateEntity.setSuccess(isSuccess);
            if (errorMsg != null) {
                serviceStateEntity.setErrorMsg(errorMsg);
            } else {
                serviceStateEntity.setErrorMsg("");
            }
            serviceStateRepository.save(serviceStateEntity);
        }
    }
}
