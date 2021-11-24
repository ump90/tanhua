package com.tanhua.vo;

import com.tanhua.mongo.Comment;
import com.tanhua.pojo.UserInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author UMP90
 * @date 2021/11/11
 */
@Data
@Builder
public class CommentVo implements Serializable {
  private static final long serialVersionUID = -6280138657776815312L;
  private String id; // 评论id
  private String avatar; // 头像
  private String nickname; // 昵称

  private String content; // 评论
  private String createDate; // 评论时间
  private Integer likeCount; // 点赞数
  private Integer hasLiked; // 是否点赞（1是，0否）
  private Integer hashLoved;

  public static CommentVo init(UserInfo userInfo, Comment item) {
    CommentVo vo = CommentVo.builder().build();
    BeanUtils.copyProperties(userInfo, vo);
    BeanUtils.copyProperties(item, vo);
    Date date = new Date(item.getCreated());
    vo.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
    vo.setId(item.getId().toHexString());
    return vo;
  }
}
