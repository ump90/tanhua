package com.tanhua.server.service;

import cn.hutool.core.collection.CollUtil;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.UserLocationApi;
import com.tanhua.mongo.UserLocation;
import com.tanhua.pojo.UserInfo;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.vo.NearUserVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/16
 */
@Service
public class UserLocationService {
  @DubboReference private UserLocationApi userLocationApi;
  @DubboReference private UserInfoApi userInfoApi;

  public void save(String longitude, String latitude, String locationName) {
    GeoJsonPoint geoJsonPoint =
        new GeoJsonPoint(Double.parseDouble(latitude), Double.parseDouble(latitude));
    Long userId = UserThreadLocal.getId();
    UserLocation userLocation = new UserLocation();
    userLocation.setUserId(userId);
    userLocation.setAddress(locationName);
    userLocation.setLocation(geoJsonPoint);
    userLocationApi.updateLocation(userLocation);
  }

  public List<NearUserVo> getNearByUsers(String distance, String gender) {

    Long userId = UserThreadLocal.getId();
    List<Long> userList = userLocationApi.getNearByUser(userId, Double.parseDouble(distance));
    ArrayList<NearUserVo> nearUserVos = new ArrayList<>(userList.size());

    if (userList.size() == 0) {
      return nearUserVos;
    }
    List<UserInfo> userInfoList = userInfoApi.listByIds(userList);
    Map<Long, UserInfo> userInfoMap = CollUtil.fieldValueMap(userInfoList, "id");

    userList.forEach(
        user -> {
          UserInfo userInfo = userInfoMap.get(user);
          if (gender.equals(userInfo.getGender())) {
            NearUserVo nearUserVo = new NearUserVo();
            nearUserVo.setUserId(userId);
            nearUserVo.setAvatar(userInfo.getAvatar());
            nearUserVo.setNickname(userInfo.getNickname());
            nearUserVos.add(nearUserVo);
          }
        });
    return nearUserVos;
  }
}
