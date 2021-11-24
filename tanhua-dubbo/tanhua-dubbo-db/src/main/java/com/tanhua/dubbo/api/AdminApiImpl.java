package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.dubbo.mapper.AdminMapper;
import com.tanhua.pojo.Admin;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author UMP90
 * @date 2021/11/22
 */
@DubboService
public class AdminApiImpl extends ServiceImpl<AdminMapper, Admin> implements AdminApi {
  @Override
  public Admin getAdminByName(String name) {
    LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(Admin::getName, name);
    return this.getOne(lambdaQueryWrapper);
  }
}
