import com.github.union.query.enums.DriverTypeEnum;

/**
 * @author liuhaoyong on 2015/6/23.
 */
public class Tester {
    public static void main(String[] args){
        Tester t = new Tester();
        t.test1();
    }

    public void test1(){
        System.out.print(DriverTypeEnum.Mysql.toMap());
    }
}
