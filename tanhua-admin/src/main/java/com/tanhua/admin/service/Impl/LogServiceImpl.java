package com.tanhua.admin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.admin.mapper.LogMapper;
import com.tanhua.admin.service.LogService;
import com.tanhua.pojo.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author UMP90
 * @date 2021/11/23
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
  @Autowired LogMapper logMapper;

  @Override
  public Long countUser(String date, String type) {
    LambdaQueryWrapper<Log> logLambdaQueryWrapper = new LambdaQueryWrapper<>();
    logLambdaQueryWrapper.eq(Log::getLogTime, date).eq(Log::getType, type);
    return count(logLambdaQueryWrapper);
  }

  @Override
  public Long countActiveUser(String date) {
    return logMapper.countActiveUser(date);
  }
}
