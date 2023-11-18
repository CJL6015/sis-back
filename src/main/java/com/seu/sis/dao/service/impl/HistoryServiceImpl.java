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
    public Map<String, List<Object[]>> getHistory(Integer unitId, String st, String et) {
        List<ThermalParam> list = thermalParamService.list();
        Optional<ThermalParam> load = list.stream().filter(thermalParam -> "机组负荷".equals(thermalParam.getName())).findFirst();
        Optional<ThermalParam> resistance = list.stream().filter(thermalParam -> "凝汽器污垢空气附加热阻".equals(thermalParam.getName())).findFirst();
        Optional<ThermalParam> exhaust = list.stream().filter(thermalParam -> "机组排汽温度过冷度".equals(thermalParam.getName())).findFirst();
        Optional<ThermalParam> saturation = list.stream().filter(thermalParam -> "机组饱和温度过冷度".equals(thermalParam.getName())).findFirst();
        Optional<ThermalParam> coefficient = list.stream().filter(thermalParam -> "凝汽器阻力系数".equals(thermalParam.getName())).findFirst();
        String loadPoint, resistancePoint, exhaustPoint, saturationPoint, coefficientPoint;
        switch (unitId) {
            case 2:
                loadPoint = load.get().getP2();
                resistancePoint = resistance.get().getP2();
                exhaustPoint = exhaust.get().getP2();
                saturationPoint = saturation.get().getP2();
                coefficientPoint = coefficient.get().getP2();
                break;
            case 3:
                loadPoint = load.get().getP3();
                resistancePoint = resistance.get().getP3();
                exhaustPoint = exhaust.get().getP3();
                saturationPoint = saturation.get().getP3();
                coefficientPoint = coefficient.get().getP3();
                break;
            case 4:
                loadPoint = load.get().getP4();
                resistancePoint = resistance.get().getP4();
                exhaustPoint = exhaust.get().getP4();
                saturationPoint = saturation.get().getP4();
                coefficientPoint = coefficient.get().getP4();
                break;
            default:
                loadPoint = load.get().getP1();
                resistancePoint = resistance.get().getP1();
                exhaustPoint = exhaust.get().getP1();
                saturationPoint = saturation.get().getP1();
                coefficientPoint = coefficient.get().getP1();
                break;
        }
        Map<String, List<Object[]>> history = influxService.getHistory("FC_XBSS", Arrays.asList(loadPoint, resistancePoint, exhaustPoint,
                saturationPoint, coefficientPoint), st, et);
        Map<String, List<Object[]>> result = new HashMap<>(16);
        result.put("load", history.get(loadPoint));
        result.put("resistance", history.get(resistancePoint));
        result.put("exhaust", history.get(exhaustPoint));
        result.put("saturation", history.get(saturationPoint));
        result.put("coefficient", history.get(coefficientPoint));

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
