package com.tanhua.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/7
 */
@Data
@Document(collection = "friend")
public class Friend implements Serializable {

  private static final long serialVersionUID = 5747610158934324740L;
  private ObjectId id;
  private Long userId;
  private Long friendId;
  private Long created;
}
