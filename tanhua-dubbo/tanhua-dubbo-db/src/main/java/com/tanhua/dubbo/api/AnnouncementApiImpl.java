package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.dubbo.mapper.AnnouncementMapper;
import com.tanhua.pojo.Announcement;
import com.tanhua.vo.PageVo;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author UMP90
 * @date 2021/11/19
 */
@DubboService
public class AnnouncementApiImpl extends ServiceImpl<AnnouncementMapper, Announcement>
    implements AnnouncementApi {

  @Override
  public PageVo getAllAnnouncements(Integer page, Integer pageSize) {
    LambdaQueryWrapper<Announcement> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.orderByDesc(Announcement::getCreated);
    Page<Announcement> announcementPage = new Page<>(page, pageSize);
    IPage<Announcement> announcementIPage = this.page(announcementPage, lambdaQueryWrapper);
    return PageVo.builder()
        .page(page)
        .pagesize(pageSize)
        .pages((int) announcementIPage.getPages())
        .counts((int) announcementIPage.getTotal())
        .items(announcementIPage.getRecords())
        .build();
  }
}
