package dev.posadskiy.skillrepeat.annotation.bpp;

import dev.posadskiy.skillrepeat.annotation.Security;
import dev.posadskiy.skillrepeat.controller.SessionController;
import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.DbSession;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.exception.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class SecurityAnnotationBeanPostProcessor implements BeanPostProcessor {

	@Autowired
	private SessionController sessionController;

	@Autowired
	private UserRepository userRepository;

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
				Method declaredMethod = bean.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
				if (declaredMethod.isAnnotationPresent(Security.class)) {
					String[] roles = declaredMethod.getAnnotation(Security.class).roles();
					String sessionId = (String) args[args.length - 1];
					if (!sessionController.isSessionExist(sessionId)) {
						throw new SessionDoesNotExistException();
					}
					if (sessionController.isSessionExpired(sessionId)) {
						throw new SessionExpiredException();
					}

					DbSession sessionById = sessionController.getSessionById(sessionId);
					Optional<DbUser> byId = userRepository.findById(sessionById.getUserId());
					if (!byId.isPresent()) {
						throw new UserDoesNotExistException();
					}

					List<String> userRoles = byId.get().getRoles();
					if (CollectionUtils.isEmpty(userRoles)) {
						throw new UserRolesDoesNotExistException();
					}

					Object[] intersectionRoles = new HashSet<>(userRoles).stream().filter(Arrays.asList(roles)::contains).toArray();
					if (intersectionRoles.length == 0) {
						throw new PermissionIsAbsentException();
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
