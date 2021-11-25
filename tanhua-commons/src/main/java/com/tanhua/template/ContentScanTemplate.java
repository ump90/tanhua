package com.tanhua.template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.green.model.v20180509.ImageSyncScanRequest;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tanhua.properties.ContentScanProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author UMP90
 * @date 2021/11/24
 */
public class ContentScanTemplate {
  @Autowired private ContentScanProperties contentScanProperties;

  public String scanText(String text) throws Exception {

    IClientProfile profile =
        DefaultProfile.getProfile(
            "cn-shanghai",
            contentScanProperties.getAccessKeyId(),
            contentScanProperties.getAccessKeySecret());
    DefaultProfile.addEndpoint("cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
    IAcsClient client = new DefaultAcsClient(profile);
    TextScanRequest textScanRequest = new TextScanRequest();
    textScanRequest.setAcceptFormat(FormatType.JSON);
    textScanRequest.setHttpContentType(FormatType.JSON);
    textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST);
    textScanRequest.setEncoding("UTF-8");
    textScanRequest.setRegionId("cn-shanghai");
    List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
    Map<String, Object> task1 = new LinkedHashMap<String, Object>();
    task1.put("dataId", UUID.randomUUID().toString());
    /** 待检测的文本，长度不超过10000个字符。 */
    task1.put("content", text);
    tasks.add(task1);
    JSONObject data = new JSONObject();

    /** 检测场景。文本垃圾检测请传递antispam。 */
    data.put("scenes", Collections.singletonList("antispam"));
    data.put("tasks", tasks);
    System.out.println(JSON.toJSONString(data, true));
    textScanRequest.setHttpContent(
        data.toJSONString().getBytes(StandardCharsets.UTF_8), "UTF-8", FormatType.JSON);
    // 请务必设置超时时间。
    textScanRequest.setConnectTimeout(3000);
    textScanRequest.setReadTimeout(6000);
    try {
      HttpResponse httpResponse = client.doAction(textScanRequest);
      if (httpResponse.isSuccess()) {
        JSONObject scrResponse =
            JSON.parseObject(new String(httpResponse.getHttpContent(), StandardCharsets.UTF_8));
        System.out.println(JSON.toJSONString(scrResponse, true));
        if (200 == scrResponse.getInteger("code")) {
          JSONArray taskResults = scrResponse.getJSONArray("data");
          for (Object taskResult : taskResults) {
            if (200 == ((JSONObject) taskResult).getInteger("code")) {
              JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
              for (Object sceneResult : sceneResults) {
                String scene = ((JSONObject) sceneResult).getString("scene");
                String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                // 根据scene和suggetion做相关处理。
                // suggestion为pass表示未命中垃圾。suggestion为block表示命中了垃圾，可以通过label字段查看命中的垃圾分类。
                System.out.println("args = [" + scene + "]");
                System.out.println("args = [" + suggestion + "]");
                return suggestion;
              }
            } else {
              System.out.println(
                  "task process fail:" + ((JSONObject) taskResult).getInteger("code"));
            }
          }
        } else {
          System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
        }
      } else {
        System.out.println("response not success. status:" + httpResponse.getStatus());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public ContentScanTemplate(ContentScanProperties contentScanProperties) {
    this.contentScanProperties = contentScanProperties;
  }

  public String scanImage(List<String> imageList) {

    IClientProfile profile =
        DefaultProfile.getProfile(
            "cn-shanghai",
            contentScanProperties.getAccessKeyId(),
            contentScanProperties.getAccessKeySecret());
    DefaultProfile.addEndpoint("cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
    IAcsClient client = new DefaultAcsClient(profile);

    ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
    // 指定API返回格式。
    imageSyncScanRequest.setAcceptFormat(FormatType.JSON);
    // 指定请求方法。
    imageSyncScanRequest.setMethod(MethodType.POST);
    imageSyncScanRequest.setEncoding("utf-8");
    // 支持HTTP和HTTPS。
    imageSyncScanRequest.setProtocol(ProtocolType.HTTP);

    JSONObject httpBody = new JSONObject();
    /**
     * 设置要检测的风险场景。计费依据此处传递的场景计算。 一次请求中可以同时检测多张图片，每张图片可以同时检测多个风险场景，计费按照场景计算。
     * 例如，检测2张图片，场景传递porn和terrorism，计费会按照2张图片鉴黄，2张图片暴恐检测计算。 porn：表示鉴黄场景。
     */
    httpBody.put("scenes", Arrays.asList(contentScanProperties.getScenes().split(",")));

    /**
     * 设置待检测图片。一张图片对应一个task。 多张图片同时检测时，处理的时间由最后一个处理完的图片决定。
     * 通常情况下批量检测的平均响应时间比单张检测的要长。一次批量提交的图片数越多，响应时间被拉长的概率越高。 这里以单张图片检测作为示例, 如果是批量图片检测，请自行构建多个task。
     */
    JSONObject task = new JSONObject();
    imageList.forEach(
        img -> {
          task.put("dataId", UUID.randomUUID().toString());

          // 设置图片链接。
          task.put("url", img);
          task.put("time", new Date());
        });

    httpBody.put("tasks", Arrays.asList(task));

    imageSyncScanRequest.setHttpContent(
        org.apache.commons.codec.binary.StringUtils.getBytesUtf8(httpBody.toJSONString()),
        "UTF-8",
        FormatType.JSON);

    /** 请设置超时时间。服务端全链路处理超时时间为10秒，请做相应设置。 如果您设置的ReadTimeout小于服务端处理的时间，程序中会获得一个ReadTimeout异常。 */
    imageSyncScanRequest.setConnectTimeout(3000);
    imageSyncScanRequest.setReadTimeout(10000);
    HttpResponse httpResponse = null;
    try {
      httpResponse = client.doAction(imageSyncScanRequest);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 服务端接收到请求，完成处理后返回的结果。
    if (httpResponse != null && httpResponse.isSuccess()) {
      JSONObject scrResponse =
          JSON.parseObject(
              org.apache.commons.codec.binary.StringUtils.newStringUtf8(
                  httpResponse.getHttpContent()));
      System.out.println(JSON.toJSONString(scrResponse, true));
      int requestCode = scrResponse.getIntValue("code");
      // 每一张图片的检测结果。
      JSONArray taskResults = scrResponse.getJSONArray("data");
      if (200 == requestCode) {
        for (Object taskResult : taskResults) {
          // 单张图片的处理结果。
          int taskCode = ((JSONObject) taskResult).getIntValue("code");
          // 图片对应检测场景的处理结果。如果是多个场景，则会有每个场景的结果。
          JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
          if (200 == taskCode) {
            for (Object sceneResult : sceneResults) {
              String scene = ((JSONObject) sceneResult).getString("scene");
              String suggestion = ((JSONObject) sceneResult).getString("suggestion");
              // 根据scene和suggestion做相关处理。
              // 根据不同的suggestion结果做业务上的不同处理。例如，将违规数据删除等。
              System.out.println("scene = [" + scene + "]");
              System.out.println("suggestion = [" + suggestion + "]");
              if ("block".equals(suggestion)) {
                return "block";
              }
              if ("review".equals(suggestion)) {
                return "review";
              }
            }
            return "pass";
          } else {
            // 单张图片处理失败, 原因视具体的情况详细分析。
            System.out.println("task process fail. task response:" + JSON.toJSONString(taskResult));
          }
        }
      } else {
        /** 表明请求整体处理失败，原因视具体的情况详细分析。 */
        System.out.println(
            "the whole image scan request failed. response:" + JSON.toJSONString(scrResponse));
      }
    }
    return null;
  }
}
