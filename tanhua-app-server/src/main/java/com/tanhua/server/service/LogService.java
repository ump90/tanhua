package com.tanhua.server.service;

import com.alibaba.fastjson.JSON;
import com.tanhua.enums.LogType;
import com.tanhua.server.utils.UserThreadLocal;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/25
 */
@Service
public class LogService {
  @Autowired private AmqpTemplate amqpTemplate;

  public void sendLog(String key, LogType logType, String objId) {
    Map<String, Object> msg = new HashMap<>();
    msg.put("userId", UserThreadLocal.getId());
    msg.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    msg.put("objId", objId);
    msg.put("type", logType.getType());
    String message = JSON.toJSONString(msg);
    // 发送消息
    try {
      amqpTemplate.convertSendAndReceive("tanhua.log.exchange", "log." + key, message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
