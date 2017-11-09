import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wac.query.models.KfDatabaseSource;
import com.wac.query.models.KfSingleQuery;
import com.wac.query.service.KfQueryService;
import com.wac.query.web.KfDatabaseSourceAction;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class QueryTest {

    @Autowired
    KfQueryService queryService;
    
    @Autowired
    KfDatabaseSourceAction  kfDatabaseSourceAction;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testQueryForMYSQL() throws Exception {
        String result = queryService.query(1, 2, "146");
        System.out.println(result);
    }
    
    @Test
    public void testQueryForMSSQL() throws Exception {
        String result = queryService.query(2, 1, "123");
        System.out.println(result);
    }
    
    @Test
    public void testQueryForMSSQL2() throws Exception {
        KfSingleQuery query = new KfSingleQuery();
        query.setBid(2);
        query.setParamIds(new String[]{"1"});
        query.setParamValues(new String[]{"123"});
        String result = queryService.singleQuery(query);
        System.out.println(result);
    }
    
    @Test
    public void testGetDatabaseQuery() throws UnsupportedEncodingException
    {
        KfDatabaseSource ds = new KfDatabaseSource();
        kfDatabaseSourceAction.list(null, null, "false", ds, null);
    }
    
    public static void main(String args[]) throws Exception
    {
        
        StringBuilder result = new StringBuilder();
        System.out.println(StringUtils.substringAfter("select ss from sdfsdf;", "select"));

        result.append(" select top (100) ").append(StringUtils.substringAfter("select ss from sdfsdf;", "select") );
        
        System.out.println(result.toString());
        
//        System.out.println(ConfigTools.encrypt("itsme999"));
//        System.out.println(ConfigTools.decrypt("flMB3tzvlR+JJEcxGffQNYZVyRvRSL0zmbulcXdphGyeru7tgUWVBmo8AJi+ZDQOb5j3RJoC6M2Sm7ZASdK6fg=="));
    }
}