package cn.ztion.focusserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "focus")
@Data
public class FocusConfig {
    /**
     * 是否校验token
     */
    private Boolean checkToken;
    /**
     * 消息保留时长(秒)
     */
    private Integer msgExpiredTime;
    /**
     * token列表
     */
    private String[] tokens;
}
