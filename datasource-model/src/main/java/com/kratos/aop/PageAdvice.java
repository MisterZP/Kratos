package com.kratos.aop;

import com.kratos.model.Page;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author zengping
 */
@Aspect
@Component
public class PageAdvice {

	@Pointcut("execution(* com.kratos.service..*.*(..))")
	public void invokeService() {}

	@Before("invokeService()")
	public void pageBefore(JoinPoint joinPoint) {}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@AfterReturning(value = "invokeService()", returning = "returnValue")
	public void afterReturn(JoinPoint joinPoint, Object returnValue) {
		Object[] parmas = joinPoint.getArgs();
		if (null != parmas && null != returnValue) {
			Page page = null;
			for (Object object : parmas) {
				if (object instanceof Page) {
					page = (Page) object;
					break;
				}
			}
			if (page == null) {
				return;
			}
			if (returnValue instanceof List) {
				page.setDatas((List) returnValue);
			}
		}
	}

}
