package com.tanhua.server.service;

import com.tanhua.dubbo.api.AnnouncementApi;
import com.tanhua.vo.PageVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @author UMP90
 * @date 2021/11/19
 */
@Service
public class AnnouncementService {
  @DubboReference private AnnouncementApi announcementApi;

  public PageVo list(Integer page, Integer pageSize) {
    return announcementApi.getAllAnnouncements(page, pageSize);
  }
}
