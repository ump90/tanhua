package com.tanhua.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/16
 */
@Data
@Document("visitors")
public class Visitor implements Serializable {
  private static final long serialVersionUID = -253138818134638011L;
  private ObjectId id;
  private Long date;
  private Long userId;
  private Long visitorUserId;
  private Double score;
  private String from;
  private String visitDate;
}
