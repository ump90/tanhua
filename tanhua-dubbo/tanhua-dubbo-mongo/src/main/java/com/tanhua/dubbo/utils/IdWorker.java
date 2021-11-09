package com.tanhua.dubbo.utils;

import com.tanhua.mongo.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @author UMP90
 * @date 2021/11/7
 */
@Component
public class IdWorker {
  @Autowired private MongoTemplate mongoTemplate;

  public Long getNextId(String collName) {
    Query query = new Query(Criteria.where("collName").is(collName));
    FindAndModifyOptions options = new FindAndModifyOptions();
    options.upsert(true);
    options.returnNew(true);
    Update update = new Update();
    update.inc("seqId", 1);

    Sequence seq = mongoTemplate.findAndModify(query, update, options, Sequence.class);
    if (seq != null) {
      return seq.getSeqId();
    }
    return -1L;
  }
}
