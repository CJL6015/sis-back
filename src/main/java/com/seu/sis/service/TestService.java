package com.seu.sis.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.seu.sis.dao.domain.Zksy1;
import com.seu.sis.dao.domain.Zksy2;
import com.seu.sis.dao.domain.ZksySignal;
import com.seu.sis.dao.service.Zksy1Service;
import com.seu.sis.dao.service.Zksy2Service;
import com.seu.sis.dao.service.ZksySignalService;
import com.seu.sis.influx.InfluxService;
import com.seu.sis.model.param.TestReportParam;
import com.seu.sis.model.vo.TestResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {
    private final Zksy1Service zksy1Service;

    private final Zksy2Service zksy2Service;

    private final InfluxService influxService;

    private final ZksySignalService zksySignalService;

    public List<String> getTestReport(TestReportParam param) {
        Integer unitId = param.getUnitId();
        if (unitId == 1) {
            LambdaQueryWrapper<Zksy1> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.gt(Zksy1::getSy, param.getStart())
                    .lt(Zksy1::getTjjs, param.getEnd());
            List<Zksy1> list = zksy1Service.list(queryWrapper);
            return list.stream().map(zksy1 -> {
                if ("Auto".equals(zksy1.getSzd())) {
                    return zksy1.getSy() + "(自动)";
                } else {
                    return zksy1.getSy() + "(手动)";
                }
            }).collect(Collectors.toList());
        } else {
            LambdaQueryWrapper<Zksy2> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.gt(Zksy2::getSy, param.getStart())
                    .lt(Zksy2::getTjjs, param.getEnd());
            List<Zksy2> list = zksy2Service.list(queryWrapper);
            return list.stream().map(zksy2 -> {
                if ("Auto".equals(zksy2.getSzd())) {
                    return zksy2.getSy() + "(自动)";
                } else {
                    return zksy2.getSy() + "(手动)";
                }
            }).collect(Collectors.toList());
        }
    }

    public TestResultVO getTestResult(String time, Integer unitId) {
        Map<String, Object> dataMap;
        List<String> points;
        if (unitId == 1) {
            LambdaQueryWrapper<Zksy1> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Zksy1::getSy, time);
            // 使用 selectObjs 方法结合 ResultTypeHandler 获取 Map 形式的结果
            dataMap = zksy1Service.getMap(queryWrapper);
            points = Arrays.asList("GBYPQWD_1", "DBYPQWD_1", "GBY_1", "DBY_1");
        } else {
            LambdaQueryWrapper<Zksy2> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Zksy2::getSy, time);
            dataMap = zksy2Service.getMap(queryWrapper);
            points = Arrays.asList("GBYPQWD_2", "DBYPQWD_2", "GBY_2", "DBY_2");
        }

        List<String> tableData = Arrays.asList(
                (String) dataMap.get("sy"),
                (String) dataMap.get("jl"),
                (String) dataMap.get("tjks"),
                (String) dataMap.get("tjjs"),
                (String) dataMap.get("dbyjz"),
                (String) dataMap.get("dbywdjz"),
                (String) dataMap.get("dbyyljz"),
                (String) dataMap.get("gbyjz"),
                (String) dataMap.get("gbywdjz"),
                (String) dataMap.get("gbyyljz")
        );

        Map<String, List<Object[]>> history = influxService.getHistory("HJB_XBSS", points,
                ((String) dataMap.get("sy")).replace(" ", "T") + "Z",
                ((String) dataMap.get("tjjs")).replace(" ", "T") + "Z", "1m");
        List<ZksySignal> list = zksySignalService.list();
        String status = "不在试验";
        for (ZksySignal zksySignal : list) {
            if (zksySignal.getTagname().equals("SYZT_" + unitId) && zksySignal.getValue().intValue() == 1) {
                status = "正在试验(自动)";
                break;
            }
            if (zksySignal.getTagname().equals("ManualSY_" + unitId) && zksySignal.getValue().intValue() == 1) {
                status = "正在试验(手动)";
                break;
            }
        }

        return new TestResultVO(tableData, history, status);
    }

    public Boolean submit(Integer unitId) {
        LambdaUpdateWrapper<ZksySignal> queryWrapper = new LambdaUpdateWrapper<>();
        if (unitId == 1) {
            queryWrapper.eq(ZksySignal::getTagname, "ManualSY_1");
        } else {
            queryWrapper.eq(ZksySignal::getTagname, "ManualSY_2");
        }
        queryWrapper.set(ZksySignal::getValue, 1);
        return zksySignalService.update(queryWrapper);
    }

}
