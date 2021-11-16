package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.dubbo.mapper.UserInfoMapper;
import com.tanhua.pojo.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/10/31
 */
@DubboService
public class UserInfoApiImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoApi {
  @Override
  public List<UserInfo> getUserInfo(
      List<Long> ids, Integer page, Integer pageSize, String keyword) {
    LambdaQueryWrapper<UserInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    Page<UserInfo> userInfoPage = new Page<>(page, pageSize);
    lambdaQueryWrapper.like(StringUtils.isNotBlank(keyword), UserInfo::getNickname, keyword);
    IPage<UserInfo> iPage = this.page(userInfoPage, lambdaQueryWrapper);
    return iPage.getRecords();
  }
}
