package com.tanhua.template;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.tanhua.properties.OosProperties;

import java.io.InputStream;

/**
 * @author UMP90
 * @date 2021/10/30
 */
public class OosTemplate {
  private final OosProperties oosProperties;

  public OosTemplate(OosProperties oosProperties) {
    this.oosProperties = oosProperties;
  }

  public String upload(String fileName, InputStream inputStream) {
    String endpoint = oosProperties.getEndpoint();
    String accessKeyId = oosProperties.getAccessKeyId();
    String accessKeySecret = oosProperties.getAccessKeySecret();
    String bucketName = oosProperties.getBucketName();
    String key = oosProperties.getKey();
    String url =oosProperties.getUrl();
    OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    PutObjectResult putObjectResult = client.putObject(bucketName, fileName, inputStream);
    return url+putObjectResult.getResponse().getUri();
  }
}
