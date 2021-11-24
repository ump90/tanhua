import com.tanhua.dubbo.MongoApplication;
import com.tanhua.dubbo.api.*;
import com.tanhua.mongo.UserLocation;
import com.tanhua.pojo.RecommendUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author UMP90
 * @date 2021/11/6
 */
@SpringBootTest(classes = MongoApplication.class)
@RunWith(SpringRunner.class)
public class test1 {
  @Autowired private MongoTemplate mongoTemplate;

  @Autowired private RecommendUserApi recommendUserApi;

  @Autowired private CommentApi commentApi;

  @Autowired private FriendApi friendApi;

  @Autowired private UserLocationApi userLocationApi;
  @Autowired private MovementApi movementApi;
  @Autowired private VideoApi videoApi;

  @Test
  public void test() {
    System.out.println(mongoTemplate.findOne(new Query(), RecommendUser.class));
  }

  @Test
  public void test2() {
    System.out.println(recommendUserApi.queryWithMaxScore(1L));
  }

  @Test
  public void test3() {
    friendApi.getAllByUserId(106L).forEach(friends -> System.out.println(friends.getUserId()));
  }

  //  @Test
  //  public void test4() {
  //    System.out.println(commentApi.isCommented("5e82dc406401952928c211cd", 1L));
  //  }

  @Test
  public void UpdateLocation() {
    UserLocation userLocation = new UserLocation();
    userLocation.setLocation(new GeoJsonPoint(116.353885, 40.065911));
    userLocation.setAddress("育新地铁站1");
    userLocation.setUserId(1L);
    this.userLocationApi.updateLocation(userLocation);
  }

  @Test
  public void getNearByUser() {
    System.out.println(userLocationApi.getNearByUser(2L, 50000.0));
  }

  @Test
  public void test4() {
    System.out.println(commentApi.getById("609917ecdf54dc7fe71f0d23"));
    System.out.println(movementApi.getById("5f0d73e65a319e6efab7fb50"));
    System.out.println(videoApi.getById("5e82dd6164019531fc471ff0"));
  }
}
