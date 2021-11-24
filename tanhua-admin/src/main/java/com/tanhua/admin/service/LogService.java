package com.tanhua.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanhua.pojo.Log;

/**
 * @author UMP90
 * @date 2021/11/23
 */
public interface LogService extends IService<Log> {
  Long countUser(String date, String type);

  Long countActiveUser(String date);

  Long countRetentionUser(String today, String yesterday);
}
