package cn.ztion.focusserver.Interceptor;

import cn.ztion.focusserver.config.FocusConfig;
import cn.ztion.focusserver.util.R;
import com.alibaba.fastjson2.JSON;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    FocusConfig focusConfig;

    public TokenInterceptor(FocusConfig focusConfig) {
        this.focusConfig = focusConfig;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (!StringUtils.hasLength(token) || !Arrays.asList(focusConfig.getTokens()).contains(token)) {
            response.getOutputStream().write(JSON.toJSONString(R.fail("token is not found")).getBytes(StandardCharsets.UTF_8));
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
