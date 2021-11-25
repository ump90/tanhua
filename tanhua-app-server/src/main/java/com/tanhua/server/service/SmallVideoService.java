package com.tanhua.server.service;

import cn.hutool.core.io.FileTypeUtil;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.tanhua.dubbo.api.FocusUserApi;
import com.tanhua.dubbo.api.UserInfoApi;
import com.tanhua.dubbo.api.VideoApi;
import com.tanhua.enums.LogType;
import com.tanhua.mongo.Video;
import com.tanhua.pojo.UserInfo;
import com.tanhua.server.utils.UserThreadLocal;
import com.tanhua.template.OosTemplate;
import com.tanhua.utils.Constants;
import com.tanhua.vo.PageVo;
import com.tanhua.vo.VideoVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author UMP90
 * @date 2021/11/18
 */
@Service
public class SmallVideoService {
  @Autowired private OosTemplate oosTemplate;
  @DubboReference private VideoApi videoApi;
  @Autowired private FastFileStorageClient fastFileStorageClient;
  @Autowired private FdfsWebServer fdfsWebServer;
  @DubboReference private UserInfoApi userInfoApi;
  @DubboReference private FocusUserApi focusUserApi;
  @Autowired RedisTemplate<String, Object> redisTemplate;
  @Autowired LogService logService;
  @Autowired CheckFreezeUseService checkFreezeUseService;

  public void save(MultipartFile videoThumbnail, MultipartFile video) throws IOException {
    checkFreezeUseService.check();
    String thumbNailUrl =
        oosTemplate.upload(videoThumbnail.getOriginalFilename(), videoThumbnail.getInputStream());
    String videoName = video.getOriginalFilename();
    if (videoName == null) {
      throw new RuntimeException("上传视频文件名不存在");
    }
    String type = FileTypeUtil.getType(video.getInputStream());
    if (!Objects.equals(type, "mp4")) {
      throw new RuntimeException("上传视频类型错误");
    }
    String extName = videoName.substring(videoName.lastIndexOf(".") + 1);
    StorePath videoStorePath =
        fastFileStorageClient.uploadFile(video.getInputStream(), video.getSize(), extName, null);
    String videoUrl = fdfsWebServer.getWebServerUrl() + videoStorePath.getFullPath();
    Video video1 =
        Video.builder()
            .videoUrl(videoUrl)
            .picUrl(thumbNailUrl)
            .likeCount(0)
            .loveCount(0)
            .userId(UserThreadLocal.getId())
            .text("我发布了新的小视频，快来看看吧")
            .commentCount(0)
            .build();
    logService.sendLog("video", LogType.POSTVIDEO, "");
  }

  public PageVo list(Integer page, Integer pageSize) {
    PageVo pageVo = videoApi.list(page, pageSize, null);
    List<?> videoList = pageVo.getItems();
    ArrayList<VideoVo> videoVos = new ArrayList<>(videoList.size());
    Long currentUserId = UserThreadLocal.getId();
    for (Object o : videoList) {
      Video video = (Video) o;
      UserInfo userInfo = userInfoApi.getById(video.getUserId());
      VideoVo videoVo = VideoVo.init(video, userInfo);
      String redisKey = Constants.VIDEO_INTERACT_KEY + videoVo.getId();
      String userHashKey = Constants.VIDEO_LIKE_HASHKEY + currentUserId;
      boolean hasLiked = redisTemplate.opsForHash().hasKey(redisKey, userHashKey);
      boolean hasFollowed =
          redisTemplate
              .opsForHash()
              .hasKey(
                  Constants.FOLLOWED_USER + videoVo.getUserId(),
                  Constants.FOCUS_USER + currentUserId);
      videoVo.setHashLiked(hasLiked ? 1 : 0);
      videoVo.setHasFocus(hasFollowed ? 1 : 0);
      videoVos.add(videoVo);
    }
    pageVo.setItems(videoVos);
    return pageVo;
  }

  public void focus(Long followedUserId, boolean goToFollow) {
    Long userId = UserThreadLocal.getId();
    String redisKey = Constants.FOLLOWED_USER + followedUserId;
    String hashKey = Constants.FOCUS_USER + userId;
    focusUserApi.focus(userId, followedUserId, goToFollow);
    if (goToFollow) {
      redisTemplate.opsForHash().put(redisKey, hashKey, 1);
    } else {
      redisTemplate.opsForHash().delete(redisKey, hashKey);
    }
  }
}
