package com.tanhua.dubbo.api;

import org.bson.types.ObjectId;

/**
 * @author UMP90
 * @date 2021/11/9
 */
public interface TimeLineApi {
  void saveMovement(ObjectId movementId, Long userId);
}
