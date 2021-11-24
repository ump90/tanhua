package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanhua.pojo.Announcement;
import com.tanhua.vo.PageVo;

/**
 * @author UMP90
 * @date 2021/11/19
 */
public interface AnnouncementApi extends IService<Announcement> {
  PageVo getAllAnnouncements(Integer page, Integer pageSize);
}
