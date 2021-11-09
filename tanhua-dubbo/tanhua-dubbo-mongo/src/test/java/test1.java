import com.tanhua.dubbo.MongoApplication;
import com.tanhua.dubbo.api.RecommendUserApi;
import com.tanhua.pojo.RecommendUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author UMP90
 * @date 2021/11/6
 */
@SpringBootTest(classes = MongoApplication.class)
@RunWith(SpringRunner.class)
public class test1 {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RecommendUserApi recommendUserApi;
    @Test
    public void test(){
    System.out.println(mongoTemplate.findOne(new Query(), RecommendUser.class));
    }

    @Test
    public void  test2(){
    System.out.println(recommendUserApi.queryWithMaxScore(1L));
    }
}