package cn.ztion.focusserver.config;

import cn.ztion.focusserver.Interceptor.TokenInterceptor;
import cn.ztion.focusserver.util.TokensMap;
import cn.ztion.focusserver.websocket.FocusWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableConfigurationProperties(FocusConfig.class)
public class FocusConfiguration implements WebMvcConfigurer {
    @Autowired
    FocusConfig focusConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (focusConfig.getCheckToken()) {
            registry.addInterceptor(new TokenInterceptor(focusConfig));
        }
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Bean
    public TokensMap createTokensMap() {
        FocusWebSocket.focusConfig=focusConfig;
        return new TokensMap();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
