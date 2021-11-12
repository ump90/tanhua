import com.aliyuncs.exceptions.ClientException;
import com.tanhua.AppServerApplication;
import com.tanhua.dubbo.api.MovementApi;
import com.tanhua.mongo.Movement;
import com.tanhua.server.service.MovementService;
import com.tanhua.template.AiFaceTemplate;
import com.tanhua.template.OosTemplate;
import com.tanhua.template.SmsTemplate;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author UMP90
 * @date 2021/10/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class TanhuaAppServerTest {
  @Autowired private SmsTemplate smsTemplate;
  @Autowired private OosTemplate oosTemplate;
  @Autowired private AiFaceTemplate aiFaceTemplate;
  @DubboReference private MovementApi movementApi;
  @Autowired private MovementService movementService;

  @Test
  public void smsTemplateTest() throws ClientException {
    smsTemplate.sendSmsTest("10086", "1234");
  }

  @Test
  public void oosTemplateTest() throws FileNotFoundException {
    FileInputStream fileInputStream = new FileInputStream(new File("D:\\TradeNotes.md"));
    System.out.println(oosTemplate.upload("test.txt", fileInputStream));
  }

  @Test
  public void aiFaceTemplateTest() {
    System.out.println(
        aiFaceTemplate.detectFace(
            "https://tanhua001.oss-cn-beijing.aliyuncs.com/2021/04/19/a3824a45-70e3-4655-8106-a1e1be009a5e.jpg"));
  }

  @Test
  public void testPublish() {
    Movement movement = new Movement();
    movement.setUserId(106L);
    movement.setTextContent("你的酒窝没有酒，我却醉的像条狗");
    List<String> list = new ArrayList<>();
    list.add("https://tanhua-dev.oss-cn-zhangjiakou.aliyuncs.com/images/tanhua/avatar_1.png");
    list.add("https://tanhua-dev.oss-cn-zhangjiakou.aliyuncs.com/images/tanhua/avatar_2.png");
    movement.setMedias(list);
    movement.setLatitude("40.066355");
    movement.setLongitude("116.350426");
    movement.setLocationName("中国北京市昌平区建材城西路16号");
    movementApi.publish(movement);
  }

  @Test
  public void testMovementService() {
    System.out.println(movementService.getAllMovementOfFriends(1L, 1, 10));
  }

  @Test
  public void testRecommendMovement() {}
}
