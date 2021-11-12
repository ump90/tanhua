package com.tanhua.dubbo.api;

import com.tanhua.mongo.Comment;
import com.tanhua.mongo.Movement;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/7
 */
public interface MovementApi {
  void publish(Movement movement);

  List<Movement> list(Long userId, Integer page, Integer pageSize);

  Long countByUserId(Long userId);

  Long countByFriendId(Long friendId);

  List<Movement> listFriends(Long userId, Integer page, Integer pageSize);

  List<Movement> getByPid(List<Long> pidList);

  List<Movement> getRandom(Integer number);

  Movement getById(String id);

  void updateWithComment(Comment comment);
}
