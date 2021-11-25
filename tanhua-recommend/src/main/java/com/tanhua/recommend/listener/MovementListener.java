package com.tanhua.recommend.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.tanhua.enums.LogType;
import com.tanhua.mongo.Movement;
import com.tanhua.mongo.MovementScore;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/25
 */
@RabbitListener
@Component
public class MovementListener {
  @Autowired MongoTemplate mongoTemplate;

  @RabbitListener(
      bindings =
          @QueueBinding(
              value = @Queue(value = "tanhua.log.queue"),
              exchange = @Exchange(value = "tanhua.log.exchange", type = "topic"),
              key = "{log.movement}"))
  public void receive(String message) {
    Map<String, Object> map = JSON.parseObject(message);
    Long userId = (Long) map.get("userId");
    String date = (String) map.get("date");
    String objectId = (String) map.get("objectId");
    LogType logType = (LogType) map.get("type");

    Movement movement = mongoTemplate.findById(new ObjectId(objectId), Movement.class);
    if (movement != null) {
      MovementScore movementScore = new MovementScore();
      movementScore.setUserId(userId);
      movementScore.setData(DateUtil.current());
      movementScore.setScore(getScore(logType, movement));
      movementScore.setMovementId(movement.getId().toHexString());
      mongoTemplate.save(movementScore);
    }
  }

  public static int getScore(LogType logType, Movement movement) {
    int score = 0;

    switch (logType) {
      case POSTMOVEMENT:
        score = 5;
        score += movement.getMedias().size();
        int length = StrUtil.length(movement.getTextContent());
        if (length >= 0 && length < 50) {
          score += 1;
        } else if (length < 100) {
          score += 2;
        } else {
          score += 3;
        }
        break;
      case VIEWMOVEMENT:
        score = 1;
        break;
      case LIKEMOVEMENT:
        score = 5;
        break;
      case LOVEMOVEMENT:
        score = 8;
        break;
      case COMMENTMOVEMENT:
        score = 10;
        break;
      case UNLIKEMOVEMENT:
        score = -5;
        break;
      case UNLOVEMOVEMENT:
        score = -8;
        break;
      default:
        break;
    }
    return score;
  }
}
