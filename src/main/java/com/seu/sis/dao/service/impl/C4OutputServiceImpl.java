package com.seu.sis.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.sis.dao.domain.C3output;
import com.seu.sis.dao.domain.C4Output;
import com.seu.sis.dao.service.C4OutputService;
import com.seu.sis.dao.mapper.C4OutputMapper;
import com.seu.sis.influx.InfluxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 陈小黑
 * @description 针对表【C4_Output】的数据库操作Service实现
 * @createDate 2024-05-12 12:58:45
 */
@Service
@RequiredArgsConstructor
public class C4OutputServiceImpl extends ServiceImpl<C4OutputMapper, C4Output>
        implements C4OutputService {

    private final InfluxService influxService;

    @Override
    public List<List<Object>> getData() {
        List<C4Output> list = list();
        Map<String, Double> data = new HashMap<>();
        list.forEach(c -> {
            data.put(c.getTagname(), c.getValue());
        });
        List<Object> xData = generateTimeList();
        int read = (int) influxService.read("HJB_XBSS", "DQJZSL_QC");
        List<List<Object>> result = new ArrayList<>();
        List<Object> y1 = new ArrayList<>();
        List<Object> y2 = new ArrayList<>();
        result.add(xData);
        result.add(y1);
        result.add(y2);
        if (read == 2) {
            List<Object> d1 = new ArrayList<>();
            List<Object> d2 = new ArrayList<>();
            List<Object> d3 = new ArrayList<>();
            List<Object> d4 = new ArrayList<>();
            List<Object> d5 = new ArrayList<>();
            List<Object> d6 = new ArrayList<>();
            for (int i = 1; i <= 96; i++) {
                String d1Point = "PW6_XDLR_" + i;
                d1.add(data.get(d1Point));
                String d2Point = "PW5_XDLR_" + i;
                d2.add(data.get(d2Point));
                String d3Point = "PW4_XDLR_" + i;
                d3.add(data.get(d3Point));
                String d4Point = "PW3_XDLR_" + i;
                d4.add(data.get(d4Point));
                String d5Point = "PW2_XDLR_" + i;
                d5.add(data.get(d5Point));
                String d6Point = "PW1_XDLR_" + i;
                d6.add(data.get(d6Point));
                String d7Point = "LXZDSY_" + i;
                y1.add(data.get(d7Point));
                String d8Point = "LXXBPWXH_" + i;
                y2.add(data.get(d8Point));
            }
            result.add(d1);
            result.add(d2);
            result.add(d3);
            result.add(d4);
            result.add(d5);
            result.add(d6);
        }else {
            List<Object> d1 = new ArrayList<>();
            List<Object> d2 = new ArrayList<>();
            List<Object> d3 = new ArrayList<>();
            for (int i = 1; i <= 96; i++) {
                String d1Point = "PW9_XDLR_" + i;
                d1.add(data.get(d1Point));
                String d2Point = "PW8_XDLR_" + i;
                d2.add(data.get(d2Point));
                String d3Point = "PW7_XDLR_" + i;
                d3.add(data.get(d3Point));
                String d7Point = "LXZDSY_" + i;
                y1.add(data.get(d7Point));
                String d8Point = "LXXBPWXH_" + i;
                y2.add(data.get(d8Point));
            }
            result.add(d1);
            result.add(d2);
            result.add(d3);
        }
        return result;
    }

    public static List<Object> generateTimeList() {
        List<Object> timeList = new ArrayList<>();
        for (int hour = 0; hour <= 23; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String hourStr = String.format("%02d", hour);
                String minuteStr = String.format("%02d", minute);
                String time = hourStr + ":" + minuteStr;
                timeList.add(time);
            }
        }
        return timeList;
    }
}




