package com.tanhua.dubbo.api;

import com.tanhua.mongo.Friend;
import com.tanhua.mongo.MovementTimeLine;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/9
 */
@Service
public class TimeLineApiImpl implements TimeLineApi {
  @Autowired private MongoTemplate mongoTemplate;
  @Autowired private FriendApi friendApi;

  @Override
  public void saveMovement(ObjectId movementId, Long userId) {
    List<Friend> friends = friendApi.getAllByUserId(userId);
    for (Friend friend : friends) {
      MovementTimeLine movementTimeLine = new MovementTimeLine();
      movementTimeLine.setUserId(userId);
      movementTimeLine.setFriendId(friend.getFriendId());
      movementTimeLine.setMovementId(movementId);
      mongoTemplate.save(movementTimeLine);
    }
  }
}
