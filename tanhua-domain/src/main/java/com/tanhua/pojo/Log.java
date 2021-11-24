package com.tanhua.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/23
 */
@Data
public class Log extends BasePojo implements Serializable {
  private static final long serialVersionUID = 8183465630435613403L;

  @TableId(type = IdType.AUTO)
  private Long id;

  private Long userId;
  /**
   * 操作类型, 0101为登录，0102为注册， 0201为发动态，0202为浏览动态，0203为动态点赞，0204为动态喜欢，0205为评论，0206为动态取消点赞，0207为动态取消喜欢，
   * 0301为发小视频，0302为小视频点赞，0303为小视频取消点赞，0304为小视频评论
   */
  private String type;

  private String logTime;
  private String place;
  private String equipment;
}
