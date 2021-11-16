package com.tanhua.mongo;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/11
 */
@Data
@Builder
@Document(collection = "comment")
public class Comment implements Serializable {
  private static final long serialVersionUID = 989864133485115447L;
  private ObjectId id;
  private ObjectId publishId;
  private Integer commentType;
  private String content;
  private Long userId;
  private Long publishUserId;
  private Long created;
  private Integer likeCount = 0;
}
