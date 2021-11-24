package com.tanhua.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author UMP90
 * @date 2021/11/19
 */
@Document("focus_user")
@Data
public class FocusUser {
  private ObjectId id;
  private Long userId;
  private Long followedUserId;
  private Long created;
}
