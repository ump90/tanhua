package com.tanhua.vo;

import com.tanhua.mongo.Movement;
import com.tanhua.pojo.UserInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author UMP90
 * @date 2021/11/9
 */
@Data
@Builder
public class MovementVo implements Serializable {

  private static final long serialVersionUID = 450462100297084823L;

  private String id; // 动态id

  private Long userId; // 用户id
  private String avatar; // 头像
  private String nickname; // 昵称
  private String gender; // 性别 man woman
  private Integer age; // 年龄
  private String[] tags; // 标签

  private String textContent; // 文字动态
  private String[] imageContent; // 图片动态
  private String distance; // 距离
  private String createDate; // 发布时间 如: 10分钟前
  private Integer likeCount; // 点赞数
  private Integer commentCount; // 评论数
  private Integer loveCount; // 喜欢数

  private Integer hasLiked; // 是否点赞（1是，0否）
  private Integer hasLoved; // 是否喜欢（1是，0否）

  public static MovementVo init(Movement movement, UserInfo userInfo) {
    MovementVo movementVo = MovementVo.builder().build();
    BeanUtils.copyProperties(movement, movementVo);
    BeanUtils.copyProperties(userInfo, movementVo);
    movementVo.setId(movement.getId().toHexString());
    movementVo.setTags(userInfo.getTags().split(","));
    movementVo.setImageContent(movement.getMedias().toArray(new String[0]));
    movementVo.setDistance("500M");
    Date date = new Date(movement.getCreated());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    movementVo.setCreateDate(simpleDateFormat.format(date));
    movementVo.setHasLoved(0);
    movementVo.setHasLiked(0);
    return movementVo;
  }
}
