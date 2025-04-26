package com.seu.sis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    private static final String BUCKET = "HJB_XBSS";

    private final ThermalParamService thermalParamService;

    private final InfluxService influxService;

    private final InfluxConfig influxConfig;


    public List<ParamVO> getParams(String search) {
        LambdaQueryWrapper<ThermalParam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(ThermalParam::getName, search);
        List<ThermalParam> list = thermalParamService.list(queryWrapper);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        long start = System.currentTimeMillis();
        List<String> points = new ArrayList<>();
        list.forEach(s -> {
            points.add(s.getP1().replaceAll("\\r\\n|\\r|\\n", ""));
            points.add(s.getP2().replaceAll("\\r\\n|\\r|\\n", ""));
        });
        Map<String, Double> values = influxService.readGroupNow(BUCKET, points);
        List<ParamVO> paramVOS = list.stream().map(param -> {
            String p1 = param.getP1().replaceAll("\\r\\n|\\r|\\n", "");
            String p2 = param.getP2().replaceAll("\\r\\n|\\r|\\n", "");
            double value1 = values.getOrDefault(p1, -9999.9);
            double value2 = values.getOrDefault(p2, -9999.9);
            return ParamVO.builder()
                    .name(param.getName())
                    .unit(param.getUnit())
                    .p1(Double.parseDouble(decimalFormat.format(value1)))
                    .p2(Double.parseDouble(decimalFormat.format(value2)))
                    .build();
        }).collect(Collectors.toList());
        long end = System.currentTimeMillis();
        log.info("总耗时:{}", (end - start));
        return paramVOS;
    }

    public List<String> getParamList() {
        List<ThermalParam> list = thermalParamService.list();
        return list.stream().map(ThermalParam::getName)
                .collect(Collectors.toList());
    }
}
