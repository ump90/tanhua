package com.tanhua.template;

import com.baidu.aip.face.AipFace;
import com.tanhua.properties.AiFaceProperties;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author UMP90
 * @date 2021/10/31
 */
public class AiFaceTemplate {
  private final AiFaceProperties aiFaceProperties;

  public AiFaceTemplate(AiFaceProperties aiFaceProperties) {
    this.aiFaceProperties = aiFaceProperties;
  }

  public Boolean detectFace(String imageUrl) {
    String appId = aiFaceProperties.getAppId();
    String appSecretKey = aiFaceProperties.getAppSecretKey();
    String appKey = aiFaceProperties.getAppKey();

    AipFace aipFace = new AipFace(appId, appKey, appSecretKey);
    HashMap<String, String> options = new HashMap<String, String>();
    options.put("face_field", "age");
    options.put("max_face_num", "2");
    options.put("face_type", "LIVE");
    options.put("liveness_control", "LOW");

    String imageType = "URL";

    JSONObject res = aipFace.detect(imageUrl, imageType, options);
    System.out.println(res.toString(2));
    return ((Integer) res.get("error_code")) == 0;
  }
}
