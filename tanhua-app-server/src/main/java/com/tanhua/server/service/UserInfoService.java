package com.tanhua.server.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tanhua.dubbo.api.BlackListApi;
import com.tanhua.dubbo.api.QuestionApi;
import com.tanhua.dubbo.api.SettingApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.pojo.*;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.template.AiFaceTemplate;
import com.tanhua.template.OosTemplate;
import com.tanhua.vo.SettingVo;
import com.tanhua.vo.UserPageVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author UMP90
 * @date 2021/11/3
 */
@Service
public class UserInfoService {
  @Autowired private OosTemplate oosTemplate;

  @Autowired private AiFaceTemplate aiFaceTemplate;

  @DubboReference private UserInfoApi userInfoApi;
  @DubboReference private QuestionApi questionApi;
  @DubboReference private SettingApi settingApi;
  @DubboReference private BlackListApi blackListApi;

  public void updateUserAvatar(MultipartFile headPhoto) throws IOException {
    Long id = UserThreadLocal.getId();
    String imgName = headPhoto.getOriginalFilename();
    if (imgName == null || imgName.lastIndexOf(".") == -1) {
      throw new RuntimeException();
    }
    String imgExtension = imgName.substring(imgName.lastIndexOf("."));
    String newImageName = UUID.randomUUID() + imgExtension;
    String imageUrl = oosTemplate.upload(newImageName, headPhoto.getInputStream());
    Boolean isHuman = aiFaceTemplate.detectFace(imageUrl);
    if (isHuman) {
      UserInfo userInfo = userInfoApi.getById(id);
      userInfo.setAvatar(imageUrl);
      userInfoApi.save(userInfo);
    } else {
      throw new RuntimeException();
    }
  }

  public UserInfo getUserInfo(Long id) {
    if (id == null) {
      id = UserThreadLocal.getId();
    }
    return userInfoApi.getById(id);
  }

  public void updateUserInfo(UserInfo userInfo) {
    User user = UserThreadLocal.getUser();
    userInfo.setId(user.getId());
    userInfoApi.saveOrUpdate(userInfo);
  }

  public void updateUserQuestion(Question question) {
    question.setUserId(UserThreadLocal.getId());
    questionApi.saveOrUpdate(question);
  }

  public void updateSetting(Setting setting) {
    setting.setUserId(UserThreadLocal.getId());
    settingApi.saveOrUpdate(setting);
  }

  public SettingVo getDefaultSetting() {
    User user = UserThreadLocal.getUser();
    SettingVo settingVo = new SettingVo();
    settingVo.setPhone(user.getMobile());
    settingVo.setId(user.getId());
    Setting setting = settingApi.getById(user.getId());
    settingVo.setGonggaoNotification(setting.getGonggaoNotification() == 1);
    settingVo.setLikeNotification(setting.getLikeNotification() == 1);
    settingVo.setPinglunNotification(setting.getPinglunNotification() == 1);
    return settingVo;
  }

  public void deleteBlackList(Long blackUserId) {
    Long userId = UserThreadLocal.getId();
    blackListApi.deleteByBlackUserId(userId, blackUserId);
  }

  public UserPageVo getBlackList(int page, int pageSize) {
    Page<BlackList> blackListPage = new Page<>(page, pageSize);
    IPage<BlackList> blackListIPage = blackListApi.page(blackListPage);
    UserPageVo userPageVo = new UserPageVo();
    userPageVo.setPages((int) blackListIPage.getPages());
    userPageVo.setPage((int) blackListIPage.getCurrent());
    userPageVo.setPagesize((int) blackListIPage.getSize());
    ArrayList<Long> blackUserIds = new ArrayList<>();
    for (BlackList blackList : blackListIPage.getRecords()) {
      blackUserIds.add(blackList.getBlackUserId());
    }
    List<UserInfo> blackUserInfoList = userInfoApi.listByIds(blackUserIds);
    userPageVo.setItems(blackUserInfoList);
    return userPageVo;
  }
}
