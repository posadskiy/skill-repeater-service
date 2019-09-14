package dev.posadskiy.skillrepeat;

import dev.posadskiy.skillrepeat.annotation.bpp.SecurityAnnotationBeanPostProcessor;
import dev.posadskiy.skillrepeat.controller.*;
import dev.posadskiy.skillrepeat.controller.impl.*;
import dev.posadskiy.skillrepeat.manager.TelegramManager;
import dev.posadskiy.skillrepeat.mapper.SkillMapperImpl;
import dev.posadskiy.skillrepeat.mapper.SkillMapper;
import dev.posadskiy.skillrepeat.mapper.UserMapper;
import dev.posadskiy.skillrepeat.mapper.UserMapperImpl;
import dev.posadskiy.skillrepeat.telegram.TelegramBot;
import dev.posadskiy.skillrepeat.validator.UserValidator;
import dev.posadskiy.skillrepeat.worker.UserNotificationWorker;
import dev.posadskiy.skillrepeat.worker.OldSessionGarbageCollectorWorker;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
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
	public TelegramAuthCodeController telegramAuthCodeController() {
		return new TelegramAuthCodeControllerImpl();
	}

	@Bean
	public TelegramBot telegramBot() {
		return new TelegramBot();
	}

	@Bean
	public TelegramManager telegramManager() {
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			telegramBotsApi.registerBot(telegramBot());
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		}

		return new TelegramManager();
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
	public UserNotificationWorker emailNotificationWorker() {
		return new UserNotificationWorker();
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
