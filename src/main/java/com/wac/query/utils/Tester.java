package com.wac.query.utils;

import com.wac.common.utils.RandomUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author huangjinsheng on 2015/7/29.
 */
public class Tester {

    public static void main(String[] args){
        Tester t = new Tester();
        t.test2();
    }

    public void test2(){
            String s = "sdst  ";
            String[] arr = StringUtils.split(s," ");
            for (String s1 : arr) {
                System.out.println(s1);
            }
    }

    public void test1(){
        List<String> list = new LinkedList<>();
        list.add("a");
        list.add("b");

        List<String> list2 = list.stream().map(c->{return c;})
                .collect(Collectors.toList());

        list2.stream().forEach(p->{
            System.out.println(p);
        });


        Map<Integer, String> map = list.stream().collect(
                Collectors.toMap(i -> RandomUtils.nextInt(), (p) -> p));

        list.stream().forEach(p->{
            System.out.println(p);
        });

        System.out.println(JsonTool.writeValueAsString(map));

    }
}
