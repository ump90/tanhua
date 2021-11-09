package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.dubbo.mapper.UserMapper;
import com.tanhua.pojo.User;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author UMP90
 * @date 2021/10/29
 */
@DubboService
public class UserApiImpl extends ServiceImpl<UserMapper, User> implements UserApi {

    @Override
    public User getUserByPhone(String phone) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getMobile, phone);
        return this.getOne(lambdaQueryWrapper);
    }
}
