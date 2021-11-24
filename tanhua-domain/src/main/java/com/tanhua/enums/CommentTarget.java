package com.tanhua.enums;

/**
 * @author UMP90
 * @date 2021/11/22
 */
public enum CommentTarget {
  // 动态
  Movement(1),
  // 视频
  Video(2),

  Comment(3);

  int type;

  CommentTarget(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }
}
