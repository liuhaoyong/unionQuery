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
        
//        StringBuilder result = new StringBuilder();
//        System.out.println(StringUtils.substringAfter("select ss from sdfsdf;", "select"));
//
//        result.append(" select top (100) ").append(StringUtils.substringAfter("select ss from sdfsdf;", "select") );
//        
//        System.out.println(result.toString());
//        DruidDataSource source = new DruidDataSource();
//        Properties pro = new Properties();
//        pro.setProperty(DruidDataSourceFactory.PROP_PASSWORD, "co2aiZRITm9E/Xqxae6At/ZcWQ7t8RO3kt3wQsRMIHdn7agU7vEuKbeE2m/x6VhBtaGy8FB/6MLBSnYI+Lo88g==");
//        new ConfigFilter().decrypt(source,pro);
        
       System.out.println(ConfigTools.encrypt("H1d2D_x8G"));
        System.out.println(ConfigTools.decrypt("It7jmzilkrGqaG71TPP0DwTQBaYP2GZhNcE6gn/E+S8T2aH6mq+4XNWynYhdLYfumOsFhCkT1l/0Kl4l0QwYKg=="));
    }
}