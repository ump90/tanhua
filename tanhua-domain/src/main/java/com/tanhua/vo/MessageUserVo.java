package com.tanhua.vo;

import com.tanhua.mongo.Comment;
import com.tanhua.pojo.UserInfo;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * @author UMP90
 * @date 2021/11/20
 */
@Data
public class MessageUserVo implements Serializable {
  private static final long serialVersionUID = -8922428658554392304L;
  private String id;
  private String avatar;
  private String nickname;
  private String createDate;

  public static MessageUserVo init(Comment comment, UserInfo userInfo) {
    MessageUserVo messageUserVo = new MessageUserVo();
    messageUserVo.setId(comment.getUserId().toString());
    messageUserVo.setAvatar(userInfo.getAvatar());
    messageUserVo.setNickname(userInfo.getNickname());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    messageUserVo.setCreateDate(simpleDateFormat.format(comment.getCreated()));
    return messageUserVo;
  }
}
