package com.tanhua.dubbo.api;

import com.tanhua.dubbo.utils.IdWorker;
import com.tanhua.mongo.Movement;
import org.apache.dubbo.config.annotation.DubboService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author UMP90
 * @date 2021/11/7
 */
@DubboService
public class MovementApiImpl implements MovementApi {
  @Autowired private MongoTemplate mongoTemplate;
  @Autowired private IdWorker idWorker;
  @Autowired private TimeLineApi timeLineApi;

  @Override
  public void publishMovement(Movement movement) {
    movement.setPid(idWorker.getNextId("movement"));
    movement.setCreated(System.currentTimeMillis());
    ObjectId id = ObjectId.get();
    movement.setId(id);
    mongoTemplate.save(movement);
    timeLineApi.saveMovement(id, movement.getUserId());
  }
}
