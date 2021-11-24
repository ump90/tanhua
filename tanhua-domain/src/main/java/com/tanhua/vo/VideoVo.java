package com.tanhua.vo;

import com.tanhua.mongo.Video;
import com.tanhua.pojo.UserInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/19
 */
@Data
public class VideoVo implements Serializable {
  private static final long serialVersionUID = 6829240333104200488L;
  private String id;
  private Long userId;
  private String avatar;
  private String nickname;
  private Integer hasFocus;
  private String cover;
  private String videoUrl;
  private String signature;
  private Integer likeCount;
  private Integer hashLiked;
  private Integer commentCount;

  public static VideoVo init(Video video, UserInfo userInfo) {
    VideoVo videoVo = new VideoVo();
    BeanUtils.copyProperties(userInfo, videoVo);
    BeanUtils.copyProperties(video, videoVo);
    videoVo.setId(video.getId().toHexString());
    videoVo.setUserId(userInfo.getId());
    videoVo.setHasFocus(0);
    videoVo.setCover(video.getPicUrl());
    videoVo.setSignature(video.getText());
    return videoVo;
  }
}
