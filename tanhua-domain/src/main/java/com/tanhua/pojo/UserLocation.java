package com.tanhua.pojo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/16
 */
@Data
@Document("user_location")
public class UserLocation implements Serializable {

  private static final long serialVersionUID = 5556765601057748705L;
  @Id private ObjectId id;
  @Indexed private Long userId;
  private GeoJsonPoint location;
  private String address;
  private Long created;
  private Long updated;
  private Long lastUpdated;
}
