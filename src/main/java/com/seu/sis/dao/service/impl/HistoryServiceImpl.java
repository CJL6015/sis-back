package com.seu.sis.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.sis.dao.domain.ThermalParam;
import com.seu.sis.dao.service.HistoryService;
import com.seu.sis.dao.service.ThermalParamService;
import com.seu.sis.influx.InfluxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-18 12:41
 */
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final ThermalParamService thermalParamService;

    private final InfluxService influxService;

    @Override
    public Map<String, List<Object[]>> getHistory(Integer unitId, String st, String et,String points) {
        List<ThermalParam> list = thermalParamService.list();
        String[] pointList = points.split(",");
        List<Optional<ThermalParam>> params = new ArrayList<>();
        for (String name : pointList) {
            Optional<ThermalParam> first = list.stream().filter(thermalParam -> name.equals(thermalParam.getName())).findFirst();
            params.add(first);
        }
        Map<String, String> map = new HashMap<>(16);
        switch (unitId) {
            case 2:
                params.forEach(thermalParam -> {
                    if(thermalParam.isPresent()){
                        ThermalParam param = thermalParam.get();
                        map.put(param.getName().trim(), param.getP2());
                    }
                });
                break;
            case 3:
                params.forEach(thermalParam -> {
                    if(thermalParam.isPresent()){
                        ThermalParam param = thermalParam.get();
                        map.put(param.getName().trim(), param.getP3());
                    }
                });
                break;
            case 4:
                params.forEach(thermalParam -> {
                    if(thermalParam.isPresent()){
                        ThermalParam param = thermalParam.get();
                        map.put(param.getName().trim(), param.getP4());
                    }
                });
                break;
            default:
                params.forEach(thermalParam -> {
                    if(thermalParam.isPresent()){
                        ThermalParam param = thermalParam.get();
                        map.put(param.getName().trim(), param.getP2());
                    }
                });
                break;
        }

        Map<String, List<Object[]>> history = influxService.getHistory("FC_XBSS", new ArrayList<>(map.values()), st, et);
        Map<String, List<Object[]>> result = new HashMap<>(16);
        map.forEach((k,v)->{
            result.put(k, history.get(v.trim()));
        });
        return result;
    }

    @Override
    public List<Double[]> getScatter(Integer unitId, String x, String y, String st, String et) {
        LambdaQueryWrapper<ThermalParam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ThermalParam::getName, x);
        ThermalParam xParam = thermalParamService.getOne(queryWrapper);
        LambdaQueryWrapper<ThermalParam> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ThermalParam::getName, y);
        ThermalParam yParam = thermalParamService.getOne(queryWrapper1);
        String xPoint, yPoint;
        switch (unitId) {
            case 2:
                xPoint = xParam.getP2();
                yPoint = yParam.getP2();
                break;
            case 3:
                xPoint = xParam.getP3();
                yPoint = yParam.getP3();
                break;
            case 4:
                xPoint = xParam.getP4();
                yPoint = yParam.getP4();
                break;
            default:
                xPoint = xParam.getP1();
                yPoint = yParam.getP1();
                break;
        }
        Map<String, List<Object[]>> history = influxService.getHistory("FC_XBSS", Arrays.asList(xPoint, yPoint), st, et);
        List<Object[]> xData = history.get(xPoint);
        List<Object[]> yData = history.get(yPoint);
        List<Double[]> result = new ArrayList<>();
        xData.forEach(m -> {
            Optional<Object[]> first = yData.stream().filter(k -> m[0].toString().equals(k[0].toString())).findFirst();
            if (first.isPresent()) {
            Double[] d = {(Double) m[1], (Double) first.get()[1]};
                result.add(d);
            }
        });
        return result;
    }
}
