package com.seu.sis.service;

import com.seu.sis.dao.domain.ThermalParam;
import com.seu.sis.dao.service.ThermalParamService;
import com.seu.sis.influx.InfluxConfig;
import com.seu.sis.influx.InfluxService;
import com.seu.sis.model.vo.ParamVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-12 10:18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ParamService {
    private static final String BUCKET = "FC_XBSS";

    private final ThermalParamService thermalParamService;

    private final InfluxService influxService;

    private final InfluxConfig influxConfig;


    public List<ParamVO> getParams() {
        List<ThermalParam> list = thermalParamService.list();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        long start = System.currentTimeMillis();
        List<String> points = new ArrayList<>();
        list.forEach(s -> {
            points.add(s.getP1());
            points.add(s.getP2());
            points.add(s.getP3());
            points.add(s.getP4());
        });
        Map<String, Double> values = influxService.readGroupNow(BUCKET, points);
        List<ParamVO> paramVOS = list.stream().map(param -> {
            String p1 = param.getP1();
            String p2 = param.getP2();
            String p3 = param.getP3();
            String p4 = param.getP4();
            double value1 = values.get(p1);
            double value2 = values.get(p2);
            double value3 = values.get(p3);
            double value4 = values.get(p4);
            return ParamVO.builder()
                    .name(param.getName())
                    .unit(param.getUnit())
                    .p1(Double.parseDouble(decimalFormat.format(value1)))
                    .p2(Double.parseDouble(decimalFormat.format(value2)))
                    .p3(Double.parseDouble(decimalFormat.format(value3)))
                    .p4(Double.parseDouble(decimalFormat.format(value4)))
                    .build();
        }).collect(Collectors.toList());
        long end = System.currentTimeMillis();
        log.info("总耗时:{}", (end - start));
        return paramVOS;
    }

    public List<String> getParamList() {
        List<ThermalParam> list = thermalParamService.list();
        return list.stream().map(ThermalParam::getName).collect(Collectors.toList());
    }
}
