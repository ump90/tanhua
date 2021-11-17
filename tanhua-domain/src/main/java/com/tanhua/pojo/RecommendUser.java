package com.tanhua.pojo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/6
 */
@Data
@Document(collection = "recommend_user")
public class RecommendUser extends BasePojo implements Serializable {
  private static final long serialVersionUID = 4383196135408385506L;
  private ObjectId id;
  private Long userId;
  private Long toUserId;
  private Double score = 0d;
  private String date;
}
