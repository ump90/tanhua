package com.tanhua.dubbo.api;

import com.tanhua.mongo.Friends;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/9
 */

@Service
public class FriendApiImpl implements FriendApi{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Friends> getAllFriends(Long userId) {
        Query query=new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, Friends.class);
    }
}
