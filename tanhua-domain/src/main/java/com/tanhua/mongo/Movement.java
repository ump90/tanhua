package com.tanhua.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/7
 */
@Data
@Document(collection = "movement")
public class Movement implements Serializable {
  private static final long serialVersionUID = 93264101266430665L;
  private ObjectId id;
  private Long pid;
  private Long userId;
  private String textContent;
  private List<String> medias;
  private Integer state = 0;
  private String longitude;
  private String latitude;
  private String locationName;
  private Long created;
  private Integer likeCount;
  private Integer commentCount;
  private Integer loveCount;
}
