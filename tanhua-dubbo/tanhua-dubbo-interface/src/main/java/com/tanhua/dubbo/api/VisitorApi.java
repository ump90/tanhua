package com.tanhua.dubbo.api;

import com.tanhua.mongo.Visitor;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/16
 */
public interface VisitorApi {
  List<Visitor> getVisitorId(Long userId, Long lastTime);

  void saveVisitor(Visitor visitor);

  Boolean isVisited(Long userId, String dataString);
}
