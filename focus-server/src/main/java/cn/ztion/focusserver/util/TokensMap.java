package cn.ztion.focusserver.util;

import cn.ztion.focusserver.entity.Message;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZtionJam
 * @Date: 2023/8/3 10:54
 * @Description: TokensMap
 * @Version 1.0.0
 */
@Data
public class TokensMap {
    private Map<String, List<Message>> map = new HashMap<>();

    public List<Message> get(String token) {
        return map.get(token) == null ? new ArrayList<>() : map.get(token);
    }

    public void set(String token, List<Message> list) {
        map.put(token, list);
    }

    public void put(String token, Message msg) {
        List<Message> messages = this.get(token);
        messages.add(msg);
        this.set(token, messages);
    }
}
