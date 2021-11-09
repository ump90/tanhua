package com.tanhua.template;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tanhua.properties.SmsProperties;
import lombok.extern.slf4j.Slf4j;


/**
 * @author UMP90
 * @date 2021/10/28
 */
@Slf4j
public class SmsTemplate {
  private final SmsProperties properties;

  public SmsTemplate(SmsProperties properties) {
    this.properties = properties;
  }

  public void sendSms(String number, String code) throws ClientException {
    String accessKeyId = properties.getAccessKeyId();
    String accessKeySecret = properties.getAccessKeySecret();
    String signName = properties.getSignName();
    String templateCode = properties.getTemplateCode();

    System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
    System.setProperty("sun.net.client.defaultReadTimeout", "10000");

    final String product = "Dysmsapi";
    final String domain = "dysmsapi.aliyuncs.com";
    IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
    DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
    IAcsClient acsClient = new DefaultAcsClient(profile);
    SendSmsRequest request = new SendSmsRequest();
    request.setMethod(MethodType.POST);
    request.setPhoneNumbers(number);
    request.setSignName(signName);
    request.setTemplateCode(templateCode);
    request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"" + code + "\"}");
    SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
    if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
      log.info("验证码发送成功 Number:" + number + " Code:" + code);
    }
  }


  public void sendSmsTest(String number, String code){
    log.info("短信配置->"+properties.toString());
    log.info("短信目标号码->"+number+" Code->"+code);
  }
}
