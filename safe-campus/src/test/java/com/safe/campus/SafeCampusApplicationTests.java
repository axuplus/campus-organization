package com.safe.campus;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.safe.campus.about.utils.HttpUtils;
import com.safe.campus.about.utils.Md5Utils;
import com.safe.campus.mapper.SchoolStudentMapper;
import com.safe.campus.mapper.SchoolTeacherMapper;
import com.safe.campus.model.domain.SchoolStudent;
import com.safe.campus.model.domain.SchoolTeacher;
import com.safe.campus.model.dto.MqSysDto;
import com.safe.campus.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        long count = strings.parallelStream().filter(string -> string.isEmpty()).count();
        System.err.println("count = " + count);
        ForkJoinPool forkJoinPool;
    }

    @Test
    public void update() {
        System.out.println("Md5Utils.md5(\"27399619753664\") = " + Md5Utils.md5Str("27399619753664"));
        System.out.println("\"9fd692e8c24f45a95fb0b330d8ce2d12\" = " + "9fd692e8c24f45a95fb0b330d8ce2d12");
    }

//    @Autowired
//    private SchoolStudentMapper studentMapper;
//    @Autowired
//    private SchoolTeacherMapper schoolTeacherMapper;
//
//
//    @Test
//    void test7() {
//        List<SchoolTeacher> students =schoolTeacherMapper .selectList(new QueryWrapper<SchoolTeacher>());
//        students.forEach(teacher -> {
//            MqSysDto mqSysDto = new MqSysDto();
//            mqSysDto.setType(1);
//            mqSysDto.setName(teacher.getTName());
//            mqSysDto.setIdNumber(teacher.getIdNumber());
//            mqSysDto.setMasterId(teacher.getMasterId());
//            mqSysDto.setUserId(teacher.getId());
//            HttpUtils.DO_POST("http://localhost:8890/sys/ttt", new Gson().toJson(mqSysDto), null, null);
//        });
//    }
}
