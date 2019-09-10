package dev.posadskiy.skillrepeat.annotation.bpp;

import dev.posadskiy.skillrepeat.annotation.bpp.mock.TestClassWithSecurityAnnotation;
import dev.posadskiy.skillrepeat.annotation.bpp.mock.TestClassWithSecurityAnnotationImpl;
import dev.posadskiy.skillrepeat.annotation.bpp.mock.TestClassWithoutSecurityAnnotation;
import dev.posadskiy.skillrepeat.controller.SessionController;
import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.DbSession;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.exception.*;
import dev.posadskiy.skillrepeat.rest.RequestWrapper;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityAnnotationBeanPostProcessorTest {
	private static final String SESSION_ID = "SESSION_ID";
	private static final String USER_ID = "USER_ID";
	private static final String ANOTHER_USER_ID = "ANOTHER_USER_ID";
	private static final int SESSION_TIME = 0;
	private static final String USER_ROLE = "USER";
	private static final String ADMIN_ROLE = "ADMIN";
	private static final String BAD_ROLE = "BAD_ROLE";

	private final SecurityAnnotationBeanPostProcessor postProcessor = new SecurityAnnotationBeanPostProcessor();
	private final SessionController sessionController = mock(SessionController.class);
	private final UserRepository userRepository = mock(UserRepository.class);

	@Before
	public void beforeTests() throws IllegalAccessException {
		FieldUtils.writeField(postProcessor, "sessionController", sessionController, true);
		FieldUtils.writeField(postProcessor, "userRepository", userRepository, true);
	}

	@Test
	public void postProcess_BeanWithAnnotation_ProxyReturned() {
		TestClassWithSecurityAnnotationImpl bean = new TestClassWithSecurityAnnotationImpl();
		String beanName = "";

		Object beanBeforeInitialization = postProcessor.postProcessBeforeInitialization(bean, beanName);
		Object beanAfterInitialization = postProcessor.postProcessAfterInitialization(beanBeforeInitialization, beanName);

		assertNotEquals(bean.getClass().getSimpleName(), beanAfterInitialization.getClass().getSimpleName());
	}

	@Test
	public void postProcess_BeanWithoutAnnotation_OriginalBeanReturned() {
		TestClassWithoutSecurityAnnotation bean = new TestClassWithoutSecurityAnnotation();
		String beanName = "";

		Object beanBeforeInitialization = postProcessor.postProcessBeforeInitialization(bean, beanName);
		Object beanAfterInitialization = postProcessor.postProcessAfterInitialization(beanBeforeInitialization, beanName);

		assertEquals(bean.getClass().getSimpleName(), beanAfterInitialization.getClass().getSimpleName());
	}

	@Test(expected = SessionDoesNotExistException.class)
	public void postProcess_SessionDoesNotExistInSessionDb_SessionDoesNotExistExceptionThrew() {
		when(sessionController.isSessionExist(SESSION_ID)).thenReturn(false);
		TestClassWithSecurityAnnotationImpl bean = new TestClassWithSecurityAnnotationImpl();
		String beanName = "";
		RequestWrapper requestWrapper = new RequestWrapper().sessionId(SESSION_ID);

		TestClassWithSecurityAnnotation beanBeforeInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessBeforeInitialization(bean, beanName);
		TestClassWithSecurityAnnotation beanAfterInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessAfterInitialization(beanBeforeInitialization, beanName);

		beanAfterInitialization.testMethod(requestWrapper);
	}

	@Test(expected = SessionExpiredException.class)
	public void postProcess_SessionIsExpiredInSessionDb_SessionExpiredExceptionThrew() {
		when(sessionController.isSessionExist(SESSION_ID)).thenReturn(true);
		when(sessionController.isSessionExpired(SESSION_ID)).thenReturn(true);
		TestClassWithSecurityAnnotationImpl bean = new TestClassWithSecurityAnnotationImpl();
		String beanName = "";
		RequestWrapper requestWrapper = new RequestWrapper().sessionId(SESSION_ID);

		TestClassWithSecurityAnnotation beanBeforeInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessBeforeInitialization(bean, beanName);
		TestClassWithSecurityAnnotation beanAfterInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessAfterInitialization(beanBeforeInitialization, beanName);

		beanAfterInitialization.testMethod(requestWrapper);
	}

	@Test(expected = UserDoesNotExistException.class)
	public void postProcess_UserDoesNotExistInDb_UserDoesNotExistExceptionThrew() {
		when(sessionController.isSessionExist(SESSION_ID)).thenReturn(true);
		when(sessionController.isSessionExpired(SESSION_ID)).thenReturn(false);
		when(sessionController.getSessionById(SESSION_ID)).thenReturn(new DbSession(USER_ID, SESSION_TIME));
		when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(null));

		TestClassWithSecurityAnnotationImpl bean = new TestClassWithSecurityAnnotationImpl();
		String beanName = "";
		RequestWrapper requestWrapper = new RequestWrapper().sessionId(SESSION_ID);

		TestClassWithSecurityAnnotation beanBeforeInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessBeforeInitialization(bean, beanName);
		TestClassWithSecurityAnnotation beanAfterInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessAfterInitialization(beanBeforeInitialization, beanName);

		beanAfterInitialization.testMethod(requestWrapper);
	}

	@Test(expected = UserRolesDoesNotExistException.class)
	public void postProcess_UserRolesDoesNotExist_UserRolesDoesNotExistExceptionThrew() {
		when(sessionController.isSessionExist(SESSION_ID)).thenReturn(true);
		when(sessionController.isSessionExpired(SESSION_ID)).thenReturn(false);
		when(sessionController.getSessionById(SESSION_ID)).thenReturn(new DbSession(USER_ID, SESSION_TIME));
		DbUser dbUser = new DbUser();
		dbUser.setRoles(new ArrayList<>());
		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(dbUser));

		TestClassWithSecurityAnnotationImpl bean = new TestClassWithSecurityAnnotationImpl();
		String beanName = "";
		RequestWrapper requestWrapper = new RequestWrapper().sessionId(SESSION_ID);

		TestClassWithSecurityAnnotation beanBeforeInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessBeforeInitialization(bean, beanName);
		TestClassWithSecurityAnnotation beanAfterInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessAfterInitialization(beanBeforeInitialization, beanName);

		beanAfterInitialization.testMethod(requestWrapper);
	}

	@Test(expected = PermissionIsAbsentException.class)
	public void postProcess_DbUserWithoutAccessibleRole_PermissionIsAbsentExceptionThrew() {
		when(sessionController.isSessionExist(SESSION_ID)).thenReturn(true);
		when(sessionController.isSessionExpired(SESSION_ID)).thenReturn(false);
		when(sessionController.getSessionById(SESSION_ID)).thenReturn(new DbSession(USER_ID, SESSION_TIME));
		DbUser dbUser = new DbUser();
		List<String> roles = new ArrayList<>();
		roles.add(BAD_ROLE);
		dbUser.setRoles(roles);
		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(dbUser));

		TestClassWithSecurityAnnotationImpl bean = new TestClassWithSecurityAnnotationImpl();
		String beanName = "";
		RequestWrapper requestWrapper = new RequestWrapper().sessionId(SESSION_ID);

		TestClassWithSecurityAnnotation beanBeforeInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessBeforeInitialization(bean, beanName);
		TestClassWithSecurityAnnotation beanAfterInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessAfterInitialization(beanBeforeInitialization, beanName);

		beanAfterInitialization.testMethod(requestWrapper);
	}

	@Test(expected = PermissionForGetAnotherUserIsAbsentException.class)
	public void postProcess_RequestAnotherUserWithoutPermission_PermissionForGetAnotherUserIsAbsentExceptionThrew() {
		when(sessionController.isSessionExist(SESSION_ID)).thenReturn(true);
		when(sessionController.isSessionExpired(SESSION_ID)).thenReturn(false);
		when(sessionController.getSessionById(SESSION_ID)).thenReturn(new DbSession(USER_ID, SESSION_TIME));
		DbUser dbUser = new DbUser();
		List<String> roles = new ArrayList<>();
		roles.add(USER_ROLE);
		dbUser.setRoles(roles);
		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(dbUser));

		TestClassWithSecurityAnnotationImpl bean = new TestClassWithSecurityAnnotationImpl();
		String beanName = "";
		RequestWrapper requestWrapper = new RequestWrapper().sessionId(SESSION_ID).userId(ANOTHER_USER_ID);

		TestClassWithSecurityAnnotation beanBeforeInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessBeforeInitialization(bean, beanName);
		TestClassWithSecurityAnnotation beanAfterInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessAfterInitialization(beanBeforeInitialization, beanName);

		beanAfterInitialization.testMethod(requestWrapper);
	}

	@Test
	public void postProcess_PositiveWay_BeanReturned() {
		when(sessionController.isSessionExist(SESSION_ID)).thenReturn(true);
		when(sessionController.isSessionExpired(SESSION_ID)).thenReturn(false);
		when(sessionController.getSessionById(SESSION_ID)).thenReturn(new DbSession(USER_ID, SESSION_TIME));
		DbUser dbUser = new DbUser();
		List<String> roles = new ArrayList<>();
		roles.add(ADMIN_ROLE);
		dbUser.setRoles(roles);
		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(dbUser));

		TestClassWithSecurityAnnotationImpl bean = new TestClassWithSecurityAnnotationImpl();
		String beanName = "";
		RequestWrapper requestWrapper = new RequestWrapper().sessionId(SESSION_ID).userId(ANOTHER_USER_ID);

		TestClassWithSecurityAnnotation beanBeforeInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessBeforeInitialization(bean, beanName);
		TestClassWithSecurityAnnotation beanAfterInitialization =
			(TestClassWithSecurityAnnotation) postProcessor.postProcessAfterInitialization(beanBeforeInitialization, beanName);

		beanAfterInitialization.testMethod(requestWrapper);
	}
}