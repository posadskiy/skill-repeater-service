package dev.posadskiy.skillrepeat.annotation.bpp;

import dev.posadskiy.skillrepeat.annotation.Security;
import dev.posadskiy.skillrepeat.controller.SessionController;
import dev.posadskiy.skillrepeat.exception.SessionDoesNotExistException;
import dev.posadskiy.skillrepeat.exception.SessionExpiredException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class SecurityAnnotationBeanPostProcessor implements BeanPostProcessor {

	@Autowired
	private SessionController sessionController;

	private Map<String, Class> beans = new HashMap<>();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println();
		for (Method method : bean.getClass().getMethods()) {
			if (method.getDeclaredAnnotation(Security.class) != null) {
				beans.put(beanName, bean.getClass());
			}
		}

		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		Class annotatedBean = beans.get(beanName);
		if (annotatedBean != null) {
			return Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {
				if (bean.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes()).isAnnotationPresent(Security.class)) {
					String sessionId = (String) args[args.length - 1];
					if (!sessionController.isSessionExist(sessionId)) {
						throw new SessionDoesNotExistException();
					}
					if (sessionController.isSessionExpired(sessionId)) {
						throw new SessionExpiredException();
					}

					return method.invoke(bean, args);
				} else {
					return method.invoke(bean, args);
				}
			});
		}
		return bean;
	}
}
