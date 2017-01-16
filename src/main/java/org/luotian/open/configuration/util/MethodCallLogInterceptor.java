package org.luotian.open.configuration.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodCallLogInterceptor implements MethodInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(MethodCallLogInterceptor.class);

	public Object invoke(MethodInvocation invocation) throws Throwable {

		Object result = null;
		long startMilis = System.currentTimeMillis();

		logger.debug(">>>>>>>>>>>>>>>>>>>Into method-----{}.{}-----",
				invocation.getMethod().getDeclaringClass().getName(),
				invocation.getMethod().getName());
		result = invocation.proceed();
		long endMilis = System.currentTimeMillis();
		logger.debug(">>>>>>>>>>>>>>>>>>>Out of method({}ms)-----{}.{}-----",
				endMilis-startMilis,
				invocation.getMethod().getDeclaringClass().getName(),
				invocation.getMethod().getName());

		return result;
	}
}