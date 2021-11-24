package com.tanhua.admin.listener;

import com.alibaba.fastjson.JSON;
import com.tanhua.admin.service.LogService;
import com.tanhua.pojo.Log;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/23
 */
@Component
public class LogListener {
  @Autowired private LogService logService;

  @RabbitListener(
      bindings =
          @QueueBinding(
              value = @Queue(value = "tanhua.log.queue"),
              exchange = @Exchange(value = "tanhua.log.exchange"),
              key = "{log.#}"))
  public void receiveLog(String logString) {
    Map<String, Object> map = JSON.parseObject(logString);
    Long userId = Long.parseLong((String) map.get("userId"));
    String type = (String) map.get("type");
    String logTime = (String) map.get("logTime");
    String businessId = (String) map.get("businessId");
    Log log = new Log();
    log.setUserId(userId);
    log.setType(type);
    log.setLogTime(logTime);
    logService.save(log);
  }
}