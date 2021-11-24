package com.tanhua.enums;

/**
 * @author UMP90
 * @date 2021/11/24
 */
public enum LogType {
  /**
   * 操作类型, 0101为登录，0102为注册， 0201为发动态，0202为浏览动态，0203为动态点赞，0204为动态喜欢，0205为评论，0206为动态取消点赞，0207为动态取消喜欢，
   * 0301为发小视频，0302为小视频点赞，0303为小视频取消点赞，0304为小视频评论
   */
  LOGIN("0101"),
  REGISTER("0102"),
  POSTMOVEMENT("0201"),
  VIEWMOVEMENT("0202"),
  LIKEMOVEMENT("0203"),
  LOVEMOVEMENT("0204"),
  COMMENTMOVEMENT("0205"),
  UNLIKEMOVEMENT("0206"),
  UNLOVEMOVEMENT("0207"),
  POSTVIDEO("0301"),
  LIKEVIDEO("0302"),
  UNLIKEVIDEO("0303"),
  COMMENTVIDEO("0304");

  String type;

  LogType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
