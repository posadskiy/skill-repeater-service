package dev.posadskiy.skillrepeat;

import dev.posadskiy.skillrepeat.mapper.AuthMapper;
import dev.posadskiy.skillrepeat.mapper.AuthMapperImpl;
import dev.posadskiy.skillrepeat.mapper.UserMapper;
import dev.posadskiy.skillrepeat.mapper.UserMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    @Bean
    public UserMapper userMapper() {
        return new UserMapperImpl();
    }

    @Bean
    public AuthMapper authMapper() {
        return new AuthMapperImpl();
    }
}
