package cn.ztion.focusserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class Message {

    @NotEmpty(message = "电话号码必传")
    private String phone;

    @NotEmpty(message = "内容必传")
    private String content;

    @NotNull(message = "消息收到时间必传")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
}
