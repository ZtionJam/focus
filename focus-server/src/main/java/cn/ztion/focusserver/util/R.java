package cn.ztion.focusserver.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: ZtionJam
 * @Date: 2023/8/3 10:33
 * @Description: R
 * @Version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {
    private Integer code;
    private String msg;
    private T Data;

    public static <T> R<T> ok(T data) {
        return new R<>(200, "", data);
    }

    public static <T> R<T> ok() {
        return new R<>(200, "", null);
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(500, msg, null);
    }
}
