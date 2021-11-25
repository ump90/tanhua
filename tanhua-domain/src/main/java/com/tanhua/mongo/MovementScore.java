package com.tanhua.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author UMP90
 * @date 2021/11/25
 */
@Data
@Document(collection = "recomment_movement_score")
public class MovementScore {

  private ObjectId id;
  private Long userId;
  private String movementId;
  private Integer score;
  private Long data;
}
