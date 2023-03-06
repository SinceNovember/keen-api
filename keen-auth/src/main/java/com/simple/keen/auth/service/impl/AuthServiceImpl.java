package com.simple.keen.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.simple.keen.auth.mapper.AuthMapper;
import com.simple.keen.auth.model.query.AuthQuery;
import com.simple.keen.auth.service.IAuthService;
import com.simple.keen.common.consts.MsgConsts;
import com.simple.keen.common.ex.KeenException;
import com.simple.keen.monitor.service.ILoginLogService;
import com.simple.keen.system.model.enums.StatusType;
import com.simple.keen.system.model.vo.UserVO;
import com.simple.keen.system.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * .
 *
 * @author SinceNovember
 * @date 2023/1/18
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final AuthMapper authMapper;

    private final IUserService userService;

    private final ILoginLogService loginLogService;

    @Override
    public UserVO getLoginUserInfo() {
        return userService.getUserById(StpUtil.getLoginIdAsInt());
    }

    @Override
    public SaTokenInfo login(AuthQuery query) {
        UserVO userVO = authMapper.selectUserIdByUsernameAndPassword(query);
        if (userVO == null) {
            throw new KeenException(MsgConsts.LOGIN_ERROR_MSG);
        }
        if (userVO.getStatus() == StatusType.LOCK) {
            throw new KeenException(MsgConsts.USER_LOCK_MSG);
        }
        loginLogService.addLoginLog(userVO.getNickname());
        StpUtil.login(userVO.getId(), query.isRememberMe());
        return StpUtil.getTokenInfo();
    }

    @Override
    public void logout(String token) {
        StpUtil.logoutByTokenValue(token);
    }
}
