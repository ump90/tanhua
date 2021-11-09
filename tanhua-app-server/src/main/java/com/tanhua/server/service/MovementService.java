package com.tanhua.server.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.tanhua.dubbo.api.MovementApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.mongo.Movement;
import com.tanhua.pojo.ErrorResult;
import com.tanhua.server.exception.BusinessException;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.template.OosTemplate;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author UMP90
 * @date 2021/11/7
 */
@Service
public class MovementService {
  @DubboReference private MovementApi movementApi;
  @DubboReference private UserInfoApi userInfoApi;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private OosTemplate oosTemplate;


  /**
   * 发布动态
   *
   * @param movement
   * @param files
   * @throws IOException
   */
  public void sendMovement(Movement movement, MultipartFile[] files) throws IOException {
    if (StringUtils.isBlank(movement.getTextContent())) {
      throw new BusinessException(ErrorResult.contentError());
    }
    ArrayList<String> urlList = new ArrayList<>();
    for (MultipartFile file : files) {
      String url = oosTemplate.upload(file.getOriginalFilename(), file.getInputStream());
      urlList.add(url);
    }
    movement.setMedias(urlList);
    movement.setUserId(UserThreadLocal.getId());




  }
}
