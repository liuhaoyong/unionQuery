import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.druid.filter.config.ConfigTools;
import com.github.union.query.models.KfDatabaseSource;
import com.github.union.query.models.KfSingleQuery;
import com.github.union.query.service.KfQueryService;
import com.github.union.query.web.KfDatabaseSourceAction;


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
        String result = queryService.query(1, 2, "1460000");
        System.out.println(result);
    }
    
//    @Test
//    public void testQueryForMSSQL() throws Exception {
//        String result = queryService.query(2, 1, "123");
//        System.out.println(result);
//    }
    
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
    
    
    @Test
    public void testQueryForSingleQuery() throws Exception {
        KfSingleQuery query = new KfSingleQuery();
        query.setBid(5);
        query.setParamIds(new String[]{"4"});
        query.setParamValues(new String[]{"1"});
        query.setiDisplayStart(1);
        query.setiDisplayLength(100);
        query.setEndIndex(100);
        String result = queryService.singleQuery(query);
        System.out.println(result);
    }
    
    @Test
    public void testQueryForSingleTableTitle() throws ExecutionException
    {
        String str = queryService.getSingleParamTableTitle(queryService.getQueryRelatedInfo(5));
        System.out.println(str);
        
    }
    
    
    public static void main(String args[]) throws Exception
    {
        
        
       System.out.println(ConfigTools.encrypt(""));
        System.out.println(ConfigTools.decrypt(""));
    }
}
