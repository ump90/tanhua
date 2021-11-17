package com.tanhua.dubbo.api;

import com.tanhua.mongo.UserLocation;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/16
 */
public interface UserLocationApi {
  void updateLocation(UserLocation userLocation);

  List<Long> getNearByUser(Long userId, Double distance);
}
