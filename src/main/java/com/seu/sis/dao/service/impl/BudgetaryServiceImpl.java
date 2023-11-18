package com.seu.sis.dao.service.impl;

import com.seu.sis.dao.domain.C3output;
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
        String numPoint = "JZYXSL_C2";
        String temperaturePoint = "PJJSWD_C2";
        Map<String, Double> groupNow = influxService.readGroupNow("FC_SSYH", Arrays.asList(numPoint, temperaturePoint));
        params.put("count", groupNow.get(numPoint));
        params.put("temperature", groupNow.get(temperaturePoint));
        return params;
    }

    @Override
    public boolean submit(BudgetaryParam param) {
        ssyhMapper.updateValue("C2_SWDJ", param.getElectricity());
        ssyhMapper.updateValue("C2_MJ", param.getCoal());
        jhyhMapper.updateValue("C3_SWDJ", param.getElectricity());
        jhyhMapper.updateValue("C2_MJ", param.getCoal());
        influxService.write("JZYXSL_C2", param.getCount(), "FC_SSYH");
        influxService.write("PJJSWD_C2", param.getTemperature(), "FC_SSYH");
        return true;
    }

    @Override
    public List<List<Object>> getData() {
        List<C3output> list = c3outputService.list();
        Map<String, Double> data = new HashMap<>();
        list.forEach(c -> {
            data.put(c.getTagname(), c.getValue());
        });
        List<Object> xData = new ArrayList<>();
        List<Object> d1 = new ArrayList<>();
        List<Object> d2 = new ArrayList<>();
        List<Object> d3 = new ArrayList<>();
        List<Object> d4 = new ArrayList<>();
        List<Object> d5 = new ArrayList<>();
        List<Object> d6 = new ArrayList<>();
        for (int i = 1; i <= 45; i++) {
            String xPoint = "QCZFH_C3_" + i;
            xData.add(data.get(xPoint).intValue());
            String d1Point = "LXBS_C3_" + i;
            d1.add(data.get(d1Point).intValue());
            String d2Point = "XDLR_C3A_" + i;
            d2.add(data.get(d2Point));
            String d3Point = "XDLR_C3B_" + i;
            d3.add(data.get(d3Point));
            String d4Point = "XDLR_C3C_" + i;
            d4.add(data.get(d4Point));
            String d5Point = "XDLR_C3D_" + i;
            d5.add(data.get(d5Point));
            String d6Point = "LXZDSY_C3_" + i;
            d6.add(data.get(d6Point));
        }
        List<List<Object>> result = new ArrayList<>();
        result.add(xData);
        result.add(d1);
        result.add(d2);
        result.add(d3);
        result.add(d4);
        result.add(d5);
        result.add(d6);
        return result;
    }
}
