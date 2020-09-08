package com.safe.campus;


import com.safe.campus.about.exception.BizException;
import com.safe.campus.about.utils.Md5Utils;
import com.safe.campus.about.utils.service.GobalInterface;
import com.safe.campus.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
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
}
