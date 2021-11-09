package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanhua.pojo.ErrorResult;
import com.tanhua.pojo.User;

/**
 * @author UMP90
 * @date 2021/10/29
 */

public interface UserApi extends IService<User> {
    User getUserByPhone(String phone);

}
