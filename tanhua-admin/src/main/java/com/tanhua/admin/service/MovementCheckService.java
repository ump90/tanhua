package com.tanhua.admin.service;

import com.tanhua.dubbo.api.MovementApi;
import com.tanhua.mongo.Movement;
import com.tanhua.template.ContentScanTemplate;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author UMP90
 * @date 2021/11/24
 */
@Service
public class MovementCheckService {
  @DubboReference MovementApi movementApi;
  @Autowired ContentScanTemplate contentScanTemplate;

  public void check(String movementId) throws Exception {
    Movement movement = movementApi.getById(movementId);
    String contentText = movement.getTextContent();
    String textCheckResult = contentScanTemplate.scanText(contentText);
    String picCheckResult = contentScanTemplate.scanImage(movement.getMedias());
    if ("pass".equals(textCheckResult) && "pass".equals(picCheckResult)) {
      movement.setState(1);
    } else if ("review".equals(textCheckResult) || "review".equals(picCheckResult)) {
      movement.setState(0);
    } else if ("block".equals(textCheckResult) || "block".equals(picCheckResult)) {
      movement.setState(2);
    }
    movementApi.updateById(movement);
  }
}
