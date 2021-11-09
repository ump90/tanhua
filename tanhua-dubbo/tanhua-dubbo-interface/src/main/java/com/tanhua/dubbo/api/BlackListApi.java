package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanhua.pojo.BlackList;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/4
 */

public interface BlackListApi extends IService<BlackList> {
   void deleteByBlackUserId(Long userId,Long BlackUserId);
   List<BlackList> getBlackListByUserId(Long userId);
}
