package com.tanhua.server.service;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/23
 */
@Service
public class MqMessageService {
  private AmqpTemplate amqpTemplate;

  public void sendLog(Long userId, String type, String key, String objectId) {
    Map<String, String> map = new HashMap<>();
    map.put("userId", userId.toString());
    map.put("type", type);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    map.put("logTime", simpleDateFormat.format(new Date()));
    map.put("objectId", objectId);
    String logString = JSON.toJSONString(map);
    amqpTemplate.convertAndSend("tanhua.log.exchange", "log." + key, logString);
  }

  public void sendAuditMessage(String movementId) {
    amqpTemplate.convertAndSend("tanhua.audit.exchange", "audit.movement", movementId);
  }
}
