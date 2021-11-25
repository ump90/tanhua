package com.tanhua.recommend.listener;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.tanhua.enums.LogType;
import com.tanhua.mongo.Video;
import com.tanhua.mongo.VideoScore;
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
public class VideoListener {
  @Autowired MongoTemplate mongoTemplate;

  @RabbitListener(
      bindings =
          @QueueBinding(
              value = @Queue(value = "tanhua.log.queue"),
              exchange = @Exchange(value = "tanhua.log.exchange", type = "topic"),
              key = "{log.video}"))
  public void receive(String message) {
    Map<String, Object> map = JSON.parseObject(message);
    Long userId = (Long) map.get("userId");
    String date = (String) map.get("date");
    String objectId = (String) map.get("objectId");
    LogType logType = (LogType) map.get("type");

    Video video = mongoTemplate.findById(new ObjectId(objectId), Video.class);
    if (video != null) {
      VideoScore videoScore = new VideoScore();

      videoScore.setUserId(userId);
      videoScore.setData(DateUtil.current());
      videoScore.setScore(getScore(logType, video));
      videoScore.setMovementId(video.getId().toHexString());
      mongoTemplate.save(videoScore);
    }
  }

  public static int getScore(LogType logType, Video video) {
    int score = 0;

    switch (logType) {
      case POSTMOVEMENT:
        score = 5;
        break;
      case LIKEVIDEO:
        score = 2;
        break;
      case UNLIKEVIDEO:
        score = -2;
        break;
      case COMMENTVIDEO:
        score = 10;
        break;
      default:
        break;
    }
    return score;
  }
}
