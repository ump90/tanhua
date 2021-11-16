package com.tanhua.template;

import com.easemob.im.server.EMProperties;
import com.easemob.im.server.EMService;
import com.easemob.im.server.model.EMMessage;
import com.easemob.im.server.model.EMTextMessage;
import com.tanhua.properties.HxProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

/**
 * @author UMP90
 * @date 2021/11/12
 */
@Slf4j
public class HxTemplate {
  @Autowired private final HxProperties hxProperties;
  private EMService emService;

  public HxTemplate(HxProperties hxProperties) {
    this.hxProperties = hxProperties;
    EMProperties properties =
        EMProperties.builder()
            .setAppkey(hxProperties.getAppKey())
            .setClientId(hxProperties.getClientId())
            .setClientSecret(hxProperties.getClientSecret())
            .build();
    this.emService = new EMService(properties);
  }

  public void createUser(String username, String password) {
    try {
      emService.user().create(username, password).block();
    } catch (Exception exception) {
      exception.printStackTrace();
      log.info("环信创建用户失败");
    }
  }

  public void addContract(String username, String contract) {
    try {
      emService.contact().add(username, contract).block();
    } catch (Exception exception) {
      exception.printStackTrace();
      log.info("环信添加好友失败");
    }
  }

  public void delContract(String username, String contract) {
    try {
      emService.contact().remove(username, contract).block();
    } catch (Exception exception) {
      exception.printStackTrace();
      log.info("环信删除好友失败");
    }
  }

  public void sendSystemMessage(String username, String content) {
    HashSet<String> userList = new HashSet<>();
    userList.add(username);
    EMMessage message = new EMTextMessage().text(content);
    try {
      emService.message().send("admin", "users", userList, message, null).block();
    } catch (Exception e) {
      e.printStackTrace();
      log.info("环信发送系统消息失败");
    }
  }

  public void sendMessage(String fromUser, String toUser, String content) {
    HashSet<String> userList = new HashSet<>();
    userList.add(toUser);
    EMMessage message = new EMTextMessage().text(content);
    try {
      emService.message().send(fromUser, "users", userList, message, null).block();
    } catch (Exception e) {
      e.printStackTrace();
      log.info("环信发送用户消息失败");
    }
  }
}
