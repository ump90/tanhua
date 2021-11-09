import com.aliyuncs.exceptions.ClientException;
import com.tanhua.AppServerApplication;
import com.tanhua.template.AiFaceTemplate;
import com.tanhua.template.OosTemplate;
import com.tanhua.template.SmsTemplate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
}
