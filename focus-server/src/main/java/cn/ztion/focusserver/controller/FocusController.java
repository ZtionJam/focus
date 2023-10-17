package cn.ztion.focusserver.controller;

import cn.ztion.focusserver.entity.Message;
import cn.ztion.focusserver.util.R;
import cn.ztion.focusserver.util.TokensMap;
import cn.ztion.focusserver.websocket.FocusWebSocket;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/focus")
public class FocusController {
    @Autowired
    private TokensMap tokensMap;
    @Autowired
    private FocusWebSocket focusWebSocket;

    @PostMapping("/commit")
    public R<String> commit(@RequestBody @Validated Message data, @RequestHeader() String token) {
        tokensMap.put(token, data);
        focusWebSocket.sendOneMessage(token, JSON.toJSONString(data));
        return R.ok();
    }
}
