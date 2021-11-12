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
  private ObjectId publishId; // 发布id
  private Integer commentType; // 评论类型，1-点赞，2-评论，3-喜欢
  private String content; // 评论内容
  private Long userId; // 评论人
  private Long publishUserId; // 被评论人ID
  private Long created; // 发表时间
  private Integer likeCount = 0; // 当前评论的点赞数
}
