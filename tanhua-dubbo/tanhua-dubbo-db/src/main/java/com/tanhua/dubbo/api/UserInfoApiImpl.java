package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.dubbo.mapper.UserInfoMapper;
import com.tanhua.pojo.UserInfo;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author UMP90
 * @date 2021/10/31
 */
@DubboService
public class UserInfoApiImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoApi {}
