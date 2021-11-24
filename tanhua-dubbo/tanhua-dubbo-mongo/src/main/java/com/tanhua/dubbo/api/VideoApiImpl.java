package com.tanhua.dubbo.api;

import com.tanhua.dubbo.utils.IdWorker;
import com.tanhua.mongo.Video;
import com.tanhua.utils.PageUtil;
import com.tanhua.vo.PageVo;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/18
 */
@Service
@DubboService
public class VideoApiImpl implements VideoApi {
  @Autowired private MongoTemplate mongoTemplate;
  @Autowired private IdWorker idWorker;

  @Override
  public void save(Video video) {
    video.setCreated(System.currentTimeMillis());
    video.setVid(idWorker.getNextId("video"));
    mongoTemplate.save(video);
  }

  @Override
  public PageVo list(Integer page, Integer pageSize, Long uid) {
    Query countQuery = new Query();
    Query pageQuery = new Query();
    if (uid != null) {
      countQuery.addCriteria(Criteria.where("userId").is(uid));
      pageQuery.addCriteria(Criteria.where("userId").is(uid));
    }
    int totalCount = Math.toIntExact(mongoTemplate.count(new Query(), Video.class));
    List<Video> videoList =
        mongoTemplate.find(
            pageQuery
                .with(Sort.by(Sort.Direction.DESC, "created"))
                .skip((long) (page - 1) * pageSize)
                .limit(pageSize),
            Video.class);

    Integer pages = PageUtil.convertPage(pageSize, totalCount);
    return PageVo.builder()
        .counts(totalCount)
        .page(page)
        .pages(pages)
        .pagesize(pageSize)
        .items(videoList)
        .build();
  }

  @Override
  public Video getById(String id) {

    return mongoTemplate.findById(id, Video.class);
  }
}
