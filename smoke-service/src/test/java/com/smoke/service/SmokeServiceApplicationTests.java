package com.smoke.service;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.smoke.service.model.dto.DataTran;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;


@SpringBootTest
class SmokeServiceApplicationTests {

    @Test
    void contextLoads() {
        Integer[] msg = {114, 101, 112, 111, 114, 116, -16, 2, -111, 45, 0, 1, 1, -128, 0, 38, 0, 32, -16, 2, -111, 45, 0, 16, 0, 0, 0, 0, 0, 0, 0, 14, 0, 106, 100, 0};
        byte[] body = {114, 101, 112, 111, 114, 116};
        String s = new String(body, Charset.forName("UTF-8"));
        System.out.println("s = " + s);
        List<Integer> integers = Arrays.asList(msg);
        List<Integer> cmd = integers.subList(26, 27);
        System.out.println("cmd = " + cmd);
        String format = String.format("%08X", cmd.get(0));
        System.out.println("format = " + format);

    }

    @Test
    void test() {
        String s = "12345abcd";
        byte b[] = s.getBytes();
        System.out.println("b = " + b);
    }


    @Test
    void test2() {

        String str = "{\n" +
                "    \"errno\": 0,\n" +
                "    \"data\": {\n" +
                "        \"devices\": [\n" +
                "            {\n" +
                "                \"title\": \"机房1\",\n" +
                "                \"id\": \"647509416\",\n" +
                "                \"datastreams\": [\n" +
                "                    {\n" +
                "                        \"at\": \"2020-11-28 04:14:54\",\n" +
                "                        \"id\": \"3200_0_5505\",\n" +
                "                        \"value\": [\n" +
                "                            114,\n" +
                "                            101,\n" +
                "                            112,\n" +
                "                            111,\n" +
                "                            114,\n" +
                "                            116,\n" +
                "                            -16,\n" +
                "                            2,\n" +
                "                            -111,\n" +
                "                            45,\n" +
                "                            0,\n" +
                "                            1,\n" +
                "                            1,\n" +
                "                            -128,\n" +
                "                            0,\n" +
                "                            38,\n" +
                "                            0,\n" +
                "                            32,\n" +
                "                            -16,\n" +
                "                            2,\n" +
                "                            -111,\n" +
                "                            45,\n" +
                "                            0,\n" +
                "                            16,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            14,\n" +
                "                            0,\n" +
                "                            106,\n" +
                "                            100,\n" +
                "                            0\n" +
                "                        ]\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"title\": \"机房2\",\n" +
                "                \"id\": \"647509127\",\n" +
                "                \"datastreams\": [\n" +
                "                    {\n" +
                "                        \"at\": \"2020-11-28 04:31:11\",\n" +
                "                        \"id\": \"3200_0_5505\",\n" +
                "                        \"value\": [\n" +
                "                            114,\n" +
                "                            101,\n" +
                "                            112,\n" +
                "                            111,\n" +
                "                            114,\n" +
                "                            116,\n" +
                "                            -16,\n" +
                "                            2,\n" +
                "                            -111,\n" +
                "                            49,\n" +
                "                            0,\n" +
                "                            1,\n" +
                "                            1,\n" +
                "                            -128,\n" +
                "                            0,\n" +
                "                            38,\n" +
                "                            0,\n" +
                "                            31,\n" +
                "                            -16,\n" +
                "                            2,\n" +
                "                            -111,\n" +
                "                            49,\n" +
                "                            0,\n" +
                "                            16,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            0,\n" +
                "                            12,\n" +
                "                            0,\n" +
                "                            106,\n" +
                "                            100,\n" +
                "                            0\n" +
                "                        ]\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"error\": \"succ\"\n" +
                "}";
        String array = new JsonParser().parse(str)
                .getAsJsonObject()
                .get("data")
                .getAsJsonObject()
                .get("devices")
                .getAsJsonArray().toString();
        List<DataTran> list = new Gson().fromJson(array, new TypeToken<List<DataTran>>() {
        }.getType());
        System.out.println("array = " + array);
        System.out.println("list = " + list);
    }
}
