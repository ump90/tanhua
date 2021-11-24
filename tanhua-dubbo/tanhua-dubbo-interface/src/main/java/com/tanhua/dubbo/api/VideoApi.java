package com.tanhua.dubbo.api;

import com.tanhua.mongo.Video;
import com.tanhua.vo.PageVo;

/**
 * @author UMP90
 * @date 2021/11/18
 */
public interface VideoApi {
  void save(Video video);

  PageVo list(Integer page, Integer pageSize, Long uid);

  Video getById(String id);
}
