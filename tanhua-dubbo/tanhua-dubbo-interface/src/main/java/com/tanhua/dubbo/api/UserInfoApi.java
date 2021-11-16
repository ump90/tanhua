package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanhua.pojo.UserInfo;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/10/31
 */
public interface UserInfoApi extends IService<UserInfo> {
  public List<UserInfo> getUserInfo(List<Long> ids, Integer page, Integer pageSize, String keyword);
}
