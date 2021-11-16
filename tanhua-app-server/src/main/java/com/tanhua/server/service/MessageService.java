package com.tanhua.server.service;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.dubbo.api.FriendApi;
import com.tanhua.dubbo.api.MovementApi;
import com.tanhua.dubbo.api.UserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.mongo.Friend;
import com.tanhua.pojo.UserInfo;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.template.HxTemplate;
import com.tanhua.utils.Constants;
import com.tanhua.utils.PageUtil;
import com.tanhua.vo.ContractVo;
import com.tanhua.vo.PageVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/13
 */
@Service
public class MessageService {
  @Autowired private HxTemplate hxTemplate;
  @DubboReference private FriendApi friendApi;
  @DubboReference private UserInfoApi userInfoApi;
  @DubboReference private UserApi userApi;
  @DubboReference private MovementApi movementApi;

  public PageVo getContractList(Integer page, Integer pageSize, String keyword) {
    Long userId = UserThreadLocal.getId();
    List<Friend> friends = friendApi.getAllByUserId(userId);
    List<Long> friendIds = CollUtil.getFieldValues(friends, "friendId", Long.class);
    List<UserInfo> userInfos = userInfoApi.getUserInfo(friendIds, page, pageSize, keyword);
    Map<Long, UserInfo> userInfoHashMap = CollUtil.fieldValueMap(userInfos, "id");
    ArrayList<ContractVo> contractVos = new ArrayList<>();
    for (Friend friend : friends) {
      Long friendId = friend.getFriendId();
      UserInfo friendInfo = userInfoHashMap.get(friendId);
      ContractVo contractVo =
          ContractVo.builder().userId(Constants.HX_USER_PREFIX + friendInfo.getId()).build();
      BeanUtils.copyProperties(friendInfo, contractVo);
    }
    int counts = userInfos.size();
    Integer pages = PageUtil.convertPage(pageSize, counts);
    return PageVo.builder()
        .items(contractVos)
        .counts(counts)
        .pages(pages)
        .page(page)
        .pagesize(pageSize)
        .build();
  }

  public void addContract(Long friendId, Long userId) {
    Friend friend =
        Friend.builder()
            .userId(userId)
            .friendId(friendId)
            .created(System.currentTimeMillis())
            .build();
    Friend reserveFriend =
        Friend.builder()
            .userId(friendId)
            .friendId(userId)
            .created(System.currentTimeMillis())
            .build();
    friendApi.save(friend);
    friendApi.save(reserveFriend);

    String userHxUsername = userApi.getById(userId).getHxUser();
    String friendHxUsername = userApi.getById(userId).getHxUser();
    hxTemplate.addContract(userHxUsername, friendHxUsername);
  }
}
