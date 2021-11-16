package com.tanhua.server.controller;

import com.tanhua.server.service.UserLocationService;
import com.tanhua.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author UMP90
 * @date 2021/11/16
 */
@RestController
@RequestMapping("/baidu")
public class UserLocationController {
  @Autowired UserLocationService userLocationService;

  @PostMapping("/location")
  public ResponseEntity<Object> updateLocation(@RequestBody Map<String, String> map) {

    String latitude = map.get("latitude");
    String longitude = map.get("longitude");
    String locationName = map.get("addrStr");
    userLocationService.save(longitude, latitude, locationName);
    return ResponseEntity.ok(Constants.EMPTY_BODY);
  }
}
