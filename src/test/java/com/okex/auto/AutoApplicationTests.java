package com.okex.auto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AutoApplicationTests {

    @Test
    public void contextLoads() {

        List<String> testList = new ArrayList<>();
        testList.add("a");
        testList.add("b");
        testList.add("c");

        System.out.println(testList);
    }

}
