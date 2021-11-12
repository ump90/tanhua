package com.tanhua.enums;

/**
 * @author UMP90
 * @date 2021/11/11
 */
public enum CommentType {
  /** 1.点赞 2.评论 3.喜欢 */
  LIKE(1),
  COMMENT(2),
  LOVE(3);

  int type;

  CommentType(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }
}
