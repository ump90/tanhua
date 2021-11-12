package com.tanhua.server.service;

import com.tanhua.dubbo.api.RecommendUserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.pojo.RecommendUser;
import com.tanhua.pojo.RecommendUserDto;
import com.tanhua.pojo.UserInfo;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.utils.PageUtil;
import com.tanhua.vo.PageVo;
import com.tanhua.vo.TodayBestVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/6
 */
@Service
public class TanhuaService {
  @DubboReference private RecommendUserApi recommendUserApi;
  @DubboReference private UserInfoApi userInfoApi;

  public TodayBestVo getTodayBestVo() {
    Long userId = UserThreadLocal.getId();
    RecommendUser recommendUser = recommendUserApi.queryWithMaxScore(userId);
    UserInfo userInfo = userInfoApi.getById(recommendUser.getUserId());
    TodayBestVo todayBestVo = new TodayBestVo();
    BeanUtils.copyProperties(userInfo, todayBestVo);
    String tagString = userInfo.getTags();
    if (tagString != null) {
      String[] tags = tagString.split("\\.");
      todayBestVo.setTags(tags);
    }
    todayBestVo.setFateValue(recommendUser.getScore().intValue());
    return todayBestVo;
  }

  public PageVo getRecommendUserPageVo(RecommendUserDto recommendUserDto) {
    Long userId = UserThreadLocal.getId();
    int page = recommendUserDto.getPage();
    int pageSize = recommendUserDto.getPagesize();
    List<RecommendUser> recommendUserList =
        recommendUserApi.queryWithMaxScoreList(userId, pageSize, page);
    int totalCount = Math.toIntExact(recommendUserApi.count(userId));
    List<Long> userIdList = new ArrayList<>();
    for (RecommendUser recommendUser : recommendUserList) {
      userIdList.add(recommendUser.getUserId());
    }
    List<UserInfo> userInfoList = userInfoApi.listByIds(userIdList);
    return PageVo.builder()
        .items(userInfoList)
        .pages(PageUtil.convertPage(pageSize, totalCount))
        .counts(totalCount)
        .page(page)
        .pagesize(pageSize)
        .build();
  }
}
