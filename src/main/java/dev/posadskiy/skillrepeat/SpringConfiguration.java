package dev.posadskiy.skillrepeat;

import dev.posadskiy.skillrepeat.annotation.bpp.SecurityAnnotationBeanPostProcessor;
import dev.posadskiy.skillrepeat.controller.*;
import dev.posadskiy.skillrepeat.controller.impl.*;
import dev.posadskiy.skillrepeat.manager.CookieManager;
import dev.posadskiy.skillrepeat.mapper.SkillMapperImpl;
import dev.posadskiy.skillrepeat.mapper.SkillMapper;
import dev.posadskiy.skillrepeat.mapper.UserMapper;
import dev.posadskiy.skillrepeat.mapper.UserMapperImpl;
import dev.posadskiy.skillrepeat.validator.UserValidator;
import dev.posadskiy.skillrepeat.worker.EmailNotificationWorker;
import dev.posadskiy.skillrepeat.worker.OldSessionGarbageCollectorWorker;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

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
	public ConfirmEmailController confirmEmailController() {
		return new ConfirmEmailControllerImpl();
	}

	@Bean
	public ResetPasswordController resetPasswordController() {
		return new ResetPasswordControllerImpl();
	}

	@Bean
	public MessageController messageController() {
		return new MessageControllerImpl();
	}

	@Bean
	public UserMapper userMapper() {
		return new UserMapperImpl();
	}

	@Bean
	public SkillMapper skillMapper() {
		return new SkillMapperImpl();
	}

	@Bean
	public UserValidator userValidator() {
		return new UserValidator();
	}

	@Bean
	public SecurityAnnotationBeanPostProcessor securityAnnotationBeanPostProcessor() {
		return new SecurityAnnotationBeanPostProcessor();
	}

	@Bean
	public OldSessionGarbageCollectorWorker oldSessionGarbageCollector() {
		return new OldSessionGarbageCollectorWorker();
	}

	@Bean
	public EmailNotificationWorker emailNotificationWorker() {
		return new EmailNotificationWorker();
	}

	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	@Bean
	public ITemplateResolver templateResolver()
	{
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);

		return templateResolver;
	}

}
