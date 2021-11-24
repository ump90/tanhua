package com.tanhua.mongo;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/18
 */
@Data
@Builder
@Document("video")
public class Video implements Serializable {

  private static final long serialVersionUID = -4331980139674264866L;
  private ObjectId id; // 主键id
  private Long vid; // 自动增长
  private Long created; // 创建时间

  private Long userId;
  private String text; // 文字
  private String picUrl; // 视频封面文件，URL
  private String videoUrl; // 视频文件，URL

  private Integer likeCount = 0; // 点赞数
  private Integer commentCount = 0; // 评论数
  private Integer loveCount = 0; // 喜欢数
}
