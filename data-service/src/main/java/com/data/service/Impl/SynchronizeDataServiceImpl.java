package com.data.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data.config.Config;
import com.data.mapper.LanrunOpenLogMapper;
import com.data.model.domain.LanrunOpenLog;
import com.data.model.dto.LogDto;
import com.data.model.dto.Records;
import com.data.service.SynchronizeDataService;
import com.data.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SynchronizeDataServiceImpl extends ServiceImpl<LanrunOpenLogMapper, LanrunOpenLog> implements SynchronizeDataService {


    @Autowired
    private LanrunOpenLogMapper logMapper;


    @Scheduled(cron = "0 */60 * * * ?")
    @Override
    public void SynchronizeDataFromFuji() {
        // 获取当前时间 & 前一分钟时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now, pre;
        now = df.format(new Date());
        pre = df.format(calendar.getTime());
//        pre = "2020-10-13 09:19:33.000";
//        now = "2020-10-13 16:39:18.000";
        log.warn("now=====>>>>> {}", now);
        log.warn("pre=====>>>>> {}", pre);
        // where子句中between在sql server中不起作用 有时间我找一下原因
        String R_JSON = "{\n" +
                "\"PageSize\": 5000,\n" +
                "\"CurrentPage\": 1,\n" +
                "\"OrderBy\": \"\",\n" +
                "\"OrderType\": false,\n" +
                "\"where\": \"OpenDate <= '" + now + "' and OpenDate >= '" + pre + "'\",\n" +
                "\"Append\": \"\",\n" +
                "\"TotalCount\": 10\n" +
                "}";
        String r_json = HttpUtils.DO_POST(Config.FUJI_API_OPEN_RECORDS, R_JSON, null, null);
        log.warn("数据记录r_json============>>>>> {}", r_json);
        JsonObject jo = new JsonParser().parse(r_json).getAsJsonObject();
        List<Records> records = new Gson().fromJson(jo.get("Records").toString(), new TypeToken<List<Records>>() {
        }.getType());
        if (records.size() != 0) {
            // 线程池大小 = 数组大小
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(records.size());
            records.forEach(record -> {
                fixedThreadPool.submit(new execDetails(record, logMapper));
            });
            fixedThreadPool.shutdown();
        }
    }
}


@Slf4j
class execDetails implements Runnable {
    private Records record;
    private LanrunOpenLogMapper logMapper;

    public execDetails(Records record, LanrunOpenLogMapper logMapper) {
        this.record = record;
        this.logMapper = logMapper;
    }

    @Override
    public void run() {
        try {
            Map map = getRecordOpenPictureByOpenPicNo(record.getOpenPicNo(), record.getStaffId(), record.getGid());
            log.warn("map = !!!!!!!!!!!!!!!! {}", map);
            LogDto logDto = new LogDto();
            logDto.setName(record.getStaffName());
            logDto.setPhone(map.get(Config.PHONE_KEY).toString());
            logDto.setIdNumber(map.get(Config.ID_NUMBER_KEY).toString());
            logDto.setPhotoPath(map.get(Config.PICTURE_KEY).toString());
            logDto.setUserId(record.getStaffId());
            logDto.setEquipment(record.getDevNo());
            logDto.setEquipmentName(record.getDevName());
            logDto.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(record.getOpenDate().replace("T", " ")));
            logDto.setTime(DateFormat.getDateInstance().format(logDto.getCreatedAt()));
            LanrunOpenLog openLog = new ModelMapper().map(logDto, LanrunOpenLog.class);
            int insert = logMapper.insert(openLog);
            log.warn("保存成功！！！ {}", insert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map getRecordOpenPictureByOpenPicNo(String openPicNo, String staffId, String gId) {
        Map<String, String> map = new HashMap<>();
        String P_JSON = "{\n" +
                "\"PageSize\": 10,\n" +
                "\"CurrentPage\": 1,\n" +
                "\"OrderBy\": \"ID\",\n" +
                "\"OrderType\": false,\n" +
                "\"where\": \"OpenPicNo = '" + openPicNo + " ' and VedioId = 1\", \n" +
                "\"Append\": \"\",\n" +
                "\"TotalCount\": 10\n" +
                "}";
        String p_json = HttpUtils.DO_POST(Config.FUJI_API_OPEN_PICTURE, P_JSON, null, null);
        String picture = null;
        if (null != p_json) {
            JsonObject jo = new JsonParser().parse(p_json).getAsJsonObject();
            picture = jo
                    .get("Records").getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("PicAddr").getAsString();
            if (null != picture) {
                map.put(Config.PICTURE_KEY, picture);
            }
            log.warn("抓拍的图片是===> {}", picture);
        }
        String S_JSON = "{\n" +
                "\"staffRid\": [\n" +
                "\"" + staffId + "\"\n" +
                "],\n" +
                "\"pageSize\": 5,\n" +
                "\"currentPage\": 1,\n" +
                "\"gid\": \"" + gId + "\"\n" +
                "}";
        String s_json = HttpUtils.DO_POST(Config.FUJI_API_GET_STAFFS, S_JSON, null, null);
        String idNumber = null;
        String phone = null;
        if (null != s_json) {
            JsonObject jo = new JsonParser().parse(s_json).getAsJsonObject();
            idNumber = jo
                    .get("result").getAsJsonObject()
                    .get("list").getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("idNo").getAsString();
            if (null != idNumber && !"".equals(idNumber)) {
                map.put(Config.ID_NUMBER_KEY, idNumber);
            } else {
                map.put(Config.ID_NUMBER_KEY, "暂无");
            }
            phone = jo
                    .get("result").getAsJsonObject()
                    .get("list").getAsJsonArray()
                    .get(0)
                    .getAsJsonObject()
                    .get("mobileNo").getAsString();
            if (null != phone && !"".equals(phone)) {
                map.put(Config.PHONE_KEY, phone);
            } else {
                map.put(Config.PHONE_KEY, "暂无");
            }
            log.warn("身份证号码是===> {}", idNumber + "电话号码是===> {}", phone);
        }
        return map;
    }
}





