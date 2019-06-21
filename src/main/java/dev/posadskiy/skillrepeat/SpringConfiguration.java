package dev.posadskiy.skillrepeat;

import dev.posadskiy.skillrepeat.annotation.bpp.SecurityAnnotationBeanPostProcessor;
import dev.posadskiy.skillrepeat.controller.SessionController;
import dev.posadskiy.skillrepeat.controller.SessionControllerImpl;
import dev.posadskiy.skillrepeat.controller.UserController;
import dev.posadskiy.skillrepeat.controller.UserControllerImpl;
import dev.posadskiy.skillrepeat.mapper.AuthMapper;
import dev.posadskiy.skillrepeat.mapper.AuthMapperImpl;
import dev.posadskiy.skillrepeat.mapper.UserMapper;
import dev.posadskiy.skillrepeat.mapper.UserMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

	@Bean
	public UserController userController() {
		return new UserControllerImpl();
	}

	@Bean
	public SessionController sessionController() {
		return new SessionControllerImpl();
	}

	@Bean
	public UserMapper userMapper() {
		return new UserMapperImpl();
	}

	@Bean
	public AuthMapper authMapper() {
		return new AuthMapperImpl();
	}

	@Bean
	public SecurityAnnotationBeanPostProcessor securityAnnotationBeanPostProcessor() {
		return new SecurityAnnotationBeanPostProcessor();
	}
}
