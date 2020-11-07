package com.safe.campus;


import com.safe.campus.about.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class SafeCampusApplicationTests {


    @Test
    public void test1() {
        System.err.println(new test().getList(() -> new ArrayList<String>() {{
            add("你好");
            add("垃圾");
            add("测试");
        }}.stream()
                .filter(s -> !"垃圾".equals(s))
                .collect(Collectors.toList())));
    }

    @Test
    public void test2() {
        System.out.println("Md5Utils.md5(\"123456\") = " + Md5Utils.md5Str("123456"));
    }

    @Test
    public void test3() {
        List<Animal> zoo = new ArrayList<>();
//        Optional.of(new Animal()).ifPresent(zoo.add());add
    }

    @Test
    public void test4() {
        List<A> as = new ArrayList<>();
        A a1 = new A();
        a1.setName("joma");
        a1.setScore(10);
        a1.setYear(2020);
        as.add(a1);
        A a2 = new A();
        a2.setName("joma");
        a2.setScore(10);
        a2.setYear(2020);
        as.add(a2);
        A a3 = new A();
        a3.setName("jack");
        a3.setScore(10);
        a3.setYear(2020);
        as.add(a3);
        Map<String, Map<Integer, List<A>>> map = as.stream().collect(Collectors.groupingBy(A::getName, Collectors.groupingBy(A::getYear)));
        map.forEach((k, v) -> {
            AtomicInteger score = new AtomicInteger();
            System.out.println("k = " + k);
            v.forEach((a, b) -> {
                Arrays.asList(b).stream().forEach(n -> n.stream().forEach(l -> {
                    score.addAndGet(l.getScore());
                }));
            });
            System.out.println("score = " + score);
        });
    }


    @Test
    public void test5() {
        int i = 7 / 2;
        System.out.println("i = " + i);
    }

    @Test
    public void test6() {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        long count = strings.parallelStream().filter(string -> string.isEmpty()).count();
        System.err.println("count = " + count);
         ForkJoinPool forkJoinPool;
    }
}
