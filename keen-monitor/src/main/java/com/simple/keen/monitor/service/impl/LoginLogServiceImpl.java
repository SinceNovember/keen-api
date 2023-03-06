package com.simple.keen.monitor.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import com.simple.keen.common.utils.PageHelperUtils;
import com.simple.keen.kafka.message.LoginLogMessage;
import com.simple.keen.kafka.producer.KafkaProducers;
import com.simple.keen.kafka.producer.Producer;
import com.simple.keen.monitor.mapper.LoginLogMapper;
import com.simple.keen.monitor.mapping.LoginLogMapping;
import com.simple.keen.monitor.model.dto.LoginLogDTO;
import com.simple.keen.monitor.model.entity.LoginLog;
import com.simple.keen.monitor.model.query.LoginLogQuery;
import com.simple.keen.monitor.model.vo.LoginLogVO;
import com.simple.keen.monitor.service.ILoginLogService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import static com.simple.keen.common.utils.HttpUtils.*;

/**
 * .
 *
 * @author SinceNovember
 * @date 2023/2/6
 */
@Service
@RequiredArgsConstructor
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements
        ILoginLogService {

    private final List<Producer> producerList;

    @Override
    public PageSerializable<LoginLogVO> pageLoginLog(LoginLogQuery loginLogQuery) {
        PageHelper.startPage(loginLogQuery.getPageNum(), loginLogQuery.getPageSize());
        List<LoginLogDTO> loginLogDTOS = baseMapper.selectLoginLogList(
                loginLogQuery);

        if (CollectionUtils.isEmpty(loginLogDTOS)) {
            return new PageSerializable<>();
        }
        return PageHelperUtils.convertPageDto2Vo(loginLogDTOS,
                LoginLogMapping.INSTANCE::toLoginLogVOList);
    }

    @Override
    public void addLoginLog(LoginLogQuery loginLogQuery) {
        LoginLogDTO loginLogDTO = LoginLogMapping.INSTANCE.toLoginLogDTO(
                loginLogQuery);
        this.save(LoginLogMapping.INSTANCE.toLoginLog(loginLogDTO));
    }

    @Override
    public void addLoginLog(String nickname) {
        LoginLog loginLog = new LoginLog();
        loginLog.setNickname(nickname);
        loginLog.setBrowser(requestBrowser());
        loginLog.setSystem(requestSystem());
        loginLog.setIp(requestIp());
        loginLog.setLocation(requestLocation(requestIp()));
        loginLog.setLoginTime(LocalDateTime.now());

        LoginLogMessage loginLogMessage = new LoginLogMessage();
        loginLogMessage.setNickname(nickname);
        loginLogMessage.setBrowser(requestBrowser());
        loginLogMessage.setSystem(requestSystem());
        loginLogMessage.setIp(requestIp());
        loginLogMessage.setLocation(requestLocation(requestIp()));
        loginLogMessage.setLoginTime(LocalDateTime.now());

        KafkaProducers.LOGIN_LOG.sendAsync(loginLogMessage);
        save(loginLog);
    }

    @Override
    public LoginLogVO getLoginLogById(Integer id) {
        return LoginLogMapping.INSTANCE.toLoginLogVO(getById(id));
    }

    @Override
    public void deleteLoginLog(List<Integer> ids) {
        remove(Wrappers.<LoginLog>lambdaUpdate()
                .in(LoginLog::getId, ids));
    }
}
