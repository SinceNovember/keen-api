//package com.simple.keen.common.aspect;
//
//import cn.dev33.satoken.stp.StpUtil;
//import com.simple.keen.annotation.ControllerEndpoint;
//import com.simple.keen.common.ex.KeenException;
//import com.simple.keen.common.utils.StringUtils;
//import com.simple.keen.monitor.service.IOperateLogService;
//import com.simple.keen.system.service.IUserService;
//import java.lang.reflect.Method;
//import javax.annotation.Resource;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
///**
// * 调用方式切入,主要用户统计日志，统一异常
// */
//@Slf4j
//@Aspect
//@Component
//@RequiredArgsConstructor
//public class ControllerEndpointAspect extends BaseAspectSupport {
//
//    @Resource
//    private IUserService userService;
//    @Resource
//    private IOperateLogService operateLogService;
//
//    @Pointcut("@annotation(com.simple.keen.annotation.ControllerEndpoint)")
//    public void pointcut() {
//    }
//
//    @Around("pointcut()")
//    public Object around(ProceedingJoinPoint point) {
//        Object result;
//        Method targetMethod = resolveMethod(point);
//        ControllerEndpoint annotation = targetMethod.getAnnotation(ControllerEndpoint.class);
//        String operation = annotation.operation();
//        long start = System.currentTimeMillis();
//        try {
//            result = point.proceed();
//            if (StringUtils.isNotBlank(operation)) {
//                RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
//                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) attributes;
//                String ip = "";
//                if (servletRequestAttributes != null) {
//                    ip = servletRequestAttributes.getRequest().getRemoteAddr();
//                }
//                // 设置操作用户
//                operateLogService.addOperateLog(
//                        userService.getById(StpUtil.getLoginIdAsInt()).getNickname(), point,
//                        targetMethod, ip, operation,
//                        start);
//            }
//            return result;
//        } catch (Throwable throwable) {
////            log.error(throwable.getMessage(), throwable);
//            String exceptionMessage = annotation.exceptionMessage();
//            String message = throwable.getMessage();
////            String error = StringUtils.containChinese(message) ? exceptionMessage + "，" + message
////                    : exceptionMessage;
//            throw new KeenException(throwable.getMessage());
//        }
//    }
//}
