package com.tanhua.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/14
 */
@Data
@Document("user_like")
public class UserLike implements Serializable {
  private static final long serialVersionUID = 4684818075170251436L;
  private ObjectId objectId;
  @Indexed private Long userId;
  @Indexed private Long likeUserId;
  private Boolean isLike;
  private Long created;
  private Long updated;
}
