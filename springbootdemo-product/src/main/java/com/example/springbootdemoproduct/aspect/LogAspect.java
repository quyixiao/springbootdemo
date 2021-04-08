package com.example.springbootdemoproduct.aspect;

import com.alibaba.fastjson.JSON;
import com.example.springbootdemoentity.entity.OrderUtil;
import com.example.springbootdemoentity.entity.R;
import com.example.springbootdemoentity.entity.ServletUtils;
import com.example.springbootdemoentity.entity.StringUtil;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Pointcut(value = "execution(* com..controller..*.*(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String logNo = OrderUtil.getUserPoolOrder("tr");
        long start = System.currentTimeMillis();
        ch.qos.logback.classic.Logger.inheritableThreadLocalNo.set(logNo);
        ch.qos.logback.classic.Logger.inheritableThreadLocalTime.set(start);
        Object result = null;
        String uri = "";
        StringBuilder cm = new StringBuilder();
        result = null;
        Object arg = result;
        String ip = "";
        String m = "";
        String userName = "";
        String params = "";
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            Object[] args = point.getArgs();

            //过滤掉spring的一些http请求信息，下面的转jsonstring会抛异常
            if (args != null && args.length > 0) {
                for (Object arg1 : args) {
                    if (arg instanceof HttpServletResponse) {
                        continue;
                    } else if (arg1 instanceof HttpServletRequest) {
                        continue;
                    } else if (arg1 instanceof MultipartFile) {
                        continue;
                    } else if (arg1 instanceof MultipartFile[]) {
                        continue;
                    } else if (arg1 instanceof ResponseFacade) {
                        continue;
                    } else {
                        arg = arg1;
                    }
                }
            }
            m = request.getMethod();
            uri = request.getRequestURI();
            ip = ServletUtils.getIpAddress(request);

            // result的值就是被拦截方法的返回值
            String classMethod = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
            if (StringUtils.isNotBlank(classMethod) && classMethod.length() > 0 && classMethod.contains(".")) {
                String classMethods[] = classMethod.split("\\.");
                if (classMethod.length() >= 2) {
                    cm.append(classMethods[classMethods.length - 2]).append(".").append(classMethods[classMethods.length - 1]);
                }
            }
            params = JSON.toJSONString(arg);
            result = point.proceed();
            return result;
        } catch (Exception e) {
            result = R.error(e.getMessage());
            logger.error("controller error.", e);
        } finally {
            logger.info(StringUtil.appendStrs(
                    "	", "cm=", cm.toString(),
                    "	", "m=", m,
                    "	", "uri=", uri,
                    "	", "userName=", userName,
                    "	", "ip=", ip,
                    "   ", "params=", params,
                    "   ", "result=", JSON.toJSONString(result)
            ));
            ch.qos.logback.classic.Logger.inheritableThreadLocalNo.remove();
            ch.qos.logback.classic.Logger.inheritableThreadLocalTime.remove();
        }
        return result;
    }

}
