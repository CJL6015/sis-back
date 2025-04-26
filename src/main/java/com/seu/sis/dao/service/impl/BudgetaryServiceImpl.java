package com.seu.sis.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.sis.dao.domain.C3output;
import com.seu.sis.dao.domain.Jhyh;
import com.seu.sis.dao.domain.Ssyh;
import com.seu.sis.dao.mapper.JhyhMapper;
import com.seu.sis.dao.mapper.SsyhMapper;
import com.seu.sis.dao.service.BudgetaryService;
import com.seu.sis.dao.service.C3outputService;
import com.seu.sis.dao.service.JhyhService;
import com.seu.sis.dao.service.SsyhService;
import com.seu.sis.influx.InfluxConfig;
import com.seu.sis.influx.InfluxService;
import com.seu.sis.model.vo.BudgetaryParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-15 21:51
 */
@Service
@RequiredArgsConstructor
public class BudgetaryServiceImpl implements BudgetaryService {
    private static final String[] PREFIX_LIST = {"QCZFH_C3_", "LXBS_C3_", "XDLR_C3A_", "XDLR_C3B_",
            "XDLR_C3C_", "XDLR_C3D_", "LXZDSY_C3_"};
    private final InfluxService influxService;

    private final InfluxConfig influxConfig;

    private final SsyhMapper ssyhMapper;

    private final JhyhMapper jhyhMapper;

    private final SsyhService ssyhService;

    private final JhyhService jhyhService;

    private final C3outputService c3outputService;


    @Override
    public List<List<Double>> getBudgetaryData() {
        List<List<String>> points = new ArrayList<>();
        for (String prefix : PREFIX_LIST) {
            points.add(IntStream.rangeClosed(1, 45)
                    .mapToObj(i -> prefix + i)
                    .collect(Collectors.toList()));
        }

        List<String> p = points.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        Map<String, Double> data = influxService.readGroupNow("FC_SSYH", p);
        List<List<Double>> result = new ArrayList<>();
        for (List<String> point : points) {
            List<Double> values = point.stream().map(data::get).collect(Collectors.toList());
            result.add(values);
        }
        return result;
    }


    @Override
    public Map<String, Double> getParam() {
        Map<String, Double> params = new HashMap<>(8);
        List<Ssyh> ssyhList = ssyhService.list();
        for (Ssyh ssyh : ssyhList) {
            String description = ssyh.getDescription();
            if (description.contains("电价")) {
                params.put("electricity", ssyh.getValue());
            } else {
                params.put("coal", ssyh.getValue());
            }
        }
        String unit1 = "U1P_QC";
        String unit2 = "U2P_QC";
        String temperaturePoint1 = "DBYJSWD_1";
        String temperaturePoint2 = "DBYJSWD_2";
        Map<String, Double> groupNow = influxService.readGroupNow("HJB_XBSS", Arrays.asList(unit1, unit2, temperaturePoint1, temperaturePoint2));
        params.put("unit1", groupNow.get(unit1));
        params.put("unit2", groupNow.get(unit2));
        params.put("temperature1", groupNow.get(temperaturePoint1));
        params.put("temperature2", groupNow.get(temperaturePoint2));
        return params;
    }

    @Override
    public boolean submit(BudgetaryParam param) {
        jhyhMapper.updateValue("SWDJ", param.getElectricity());
        jhyhMapper.updateValue("MJ", param.getCoal());
        jhyhMapper.updateValue("C3_SIGNAL", 1D);
        return true;
    }

    @Override
    public Boolean getStatus() {
        LambdaQueryWrapper<Jhyh> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Jhyh::getTagname, "C3_SIGNAL");
        Jhyh one = jhyhService.getOne(queryWrapper);
        return Objects.equals(0, one.getValue().intValue());
    }

    @Override
    public List<List<Object>> getData() {


        List<C3output> list = c3outputService.list();
        Map<String, Double> data = new HashMap<>();
        list.forEach(c -> {
            data.put(c.getTagname(), c.getValue());
        });
        int read = (int) influxService.read("HJB_XBSS", "DQJZSL_QC");
        List<Object> xData = new ArrayList<>();
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
            for (int i = 1; i <= 91; i++) {
                String xPoint = "QCZFH_" + i;
                xData.add(data.get(xPoint).intValue());
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
        } else {
            List<Object> d1 = new ArrayList<>();
            List<Object> d2 = new ArrayList<>();
            List<Object> d3 = new ArrayList<>();
            for (int i = 1; i <= 91; i++) {
                String xPoint = "QCZFH_" + i;
                xData.add(data.get(xPoint).intValue());
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
}
