package com.spider.service;

import com.spider.entity.ServiceStateEntity;
import com.spider.entity.ServiceStateHistoryEntity;
import com.spider.global.ServiceName;
import com.spider.repository.ServiceStateHistoryRepository;
import com.spider.repository.ServiceStateRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Date;

import static org.mockito.Mockito.*;

/**
 * Created by wsy on 2015/12/8.
 *
 * @author wsy
 */
@RunWith(MockitoJUnitRunner.class)
public class HeartBeatServiceImplTest {

    @InjectMocks
    private HeartBeatServiceImpl heartBeatService;

    @Mock
    private ServiceStateRepository serviceStateRepository;

    @Mock
    private ServiceStateHistoryRepository serviceStateHistoryRepository;

    @Test(expected = NullPointerException.class)
    public void heartBeat_PassNullArgument_ThrowNullPointerException() {

        heartBeatService.heartBeat(ServiceName.LiJiRobot.getName(), null, null, true, "");
    }

    @Test
    public void heartBeat_InsertServiceState_HistoryRepositoryShouldNotBeCalled() {

        when(serviceStateRepository.findByService(isA(String.class))).thenAnswer(new Answer<ServiceStateEntity>() {
            @Override
            public ServiceStateEntity answer(InvocationOnMock invocation) throws Throwable {

                return null;
            }
        });
        when(serviceStateRepository.save(isA(ServiceStateEntity.class))).thenAnswer(new Answer<ServiceStateEntity>() {
            @Override
            public ServiceStateEntity answer(InvocationOnMock invocation) throws Throwable {

                return new ServiceStateEntity();
            }
        });
        heartBeatService.heartBeat(ServiceName.LiJiRobot.getName(), new Date(), new Date(), true, null);
        verify(serviceStateRepository).findByService(isA(String.class));
        verify(serviceStateRepository).save(isA(ServiceStateEntity.class));
        verifyNoMoreInteractions(serviceStateRepository);
    }

    @Test
    public void heartBeat_UpdateServiceState_HistoryRepositoryShouldBeCalled() {

        when(serviceStateRepository.findByService(isA(String.class))).thenAnswer(new Answer<ServiceStateEntity>() {
            @Override
            public ServiceStateEntity answer(InvocationOnMock invocation) throws Throwable {

                return new ServiceStateEntity();
            }
        });
        when(serviceStateRepository.save(isA(ServiceStateEntity.class))).thenAnswer(new Answer<ServiceStateEntity>() {
            @Override
            public ServiceStateEntity answer(InvocationOnMock invocation) throws Throwable {

                return new ServiceStateEntity();
            }
        });
        heartBeatService.heartBeat(ServiceName.LiJiRobot.getName(), new Date(), new Date(), true, "");
        verify(serviceStateRepository).findByService(isA(String.class));
        verify(serviceStateRepository).save(isA(ServiceStateEntity.class));
        verify(serviceStateHistoryRepository).save(isA(ServiceStateHistoryEntity.class));
        verifyNoMoreInteractions(serviceStateRepository);
    }
}