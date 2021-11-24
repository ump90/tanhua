package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanhua.pojo.Admin;

/**
 * @author UMP90
 * @date 2021/11/22
 */
public interface AdminApi extends IService<Admin> {
  public Admin getAdminByName(String name);
}
