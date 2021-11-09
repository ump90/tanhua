package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author UMP90
 * @date 2021/10/30
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
