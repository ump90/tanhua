package com.tanhua.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/10/30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResult implements Serializable {

  private static final long serialVersionUID = -4858874420767179965L;
  private String errCode = "999999";
  private String errMessage;

  public static ErrorResult error() {
    return ErrorResult.builder().errCode("999999").errMessage("系统异常稍后再试").build();
  }

  public static ErrorResult fail() {
    return ErrorResult.builder().errCode("000001").errMessage("发送验证码失败").build();
  }

  public static ErrorResult smsNotExpired() {
    return ErrorResult.builder().errCode("000010").errMessage("验证码已发送").build();
  }

  public static ErrorResult loginError() {
    return ErrorResult.builder().errCode("000002").errMessage("验证码失效").build();
  }

  public static ErrorResult loginCheckError() {
    return ErrorResult.builder().errCode("000010").errMessage("验证码错误").build();
  }

  public static ErrorResult faceError() {
    return ErrorResult.builder().errCode("000003").errMessage("图片非人像，请重新上传!").build();
  }

  public static ErrorResult mobileError() {
    return ErrorResult.builder().errCode("000004").errMessage("手机号码已注册").build();
  }

  public static ErrorResult contentError() {
    return ErrorResult.builder().errCode("000005").errMessage("动态内容为空").build();
  }

  public static ErrorResult likeError() {
    return ErrorResult.builder().errCode("000006").errMessage("用户已点赞").build();
  }

  public static ErrorResult disLikeError() {
    return ErrorResult.builder().errCode("000007").errMessage("用户未点赞").build();
  }

  public static ErrorResult loveError() {
    return ErrorResult.builder().errCode("000008").errMessage("用户已喜欢").build();
  }

  public static ErrorResult disloveError() {
    return ErrorResult.builder().errCode("000009").errMessage("用户未喜欢").build();
  }
}
