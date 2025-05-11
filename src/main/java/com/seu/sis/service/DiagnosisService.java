package com.seu.sis.service;

import cn.hutool.core.collection.ListUtil;
import com.seu.sis.influx.InfluxService;
import com.seu.sis.model.vo.CalculateDataVO;
import com.seu.sis.model.vo.CalculateParam;
import com.seu.sis.model.vo.DiagnosisVO;
import com.seu.sis.model.vo.GateDiagnosisVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2025-05-07 19:46
 */
@Service
@RequiredArgsConstructor
public class DiagnosisService {
    private static List<CalculateDataVO.TableRow> tableData = Arrays.asList(
            new CalculateDataVO.TableRow("机组负荷(MW)", "U1P_QC", "U2P_QC"),
            new CalculateDataVO.TableRow("供热流量(t/h)", "GRLL_1", "GRLL_2"),
            new CalculateDataVO.TableRow("冷却水流量实时值(t/h)", "NQQJKZLLL_1", "NQQJKZLLL_2"),
            new CalculateDataVO.TableRow("冷却水流量基准值(t/h)", "LQSLLJZ_1", "LQSLLJZ_2"),
            new CalculateDataVO.TableRow("高背压进水温度(℃)", "GBYJSWD_1", "GBYJSWD_2"),
            new CalculateDataVO.TableRow("低背压进水温度(℃)", "DBYJSWD_1", "DBYJSWD_2"),
            new CalculateDataVO.TableRow("冷却塔出塔水温应达值(℃)", "CTSW_1", "CTSW_2"),
            new CalculateDataVO.TableRow("高背压温升(℃)", "GBYWS_1", "GBYWS_2"),
            new CalculateDataVO.TableRow("低背压温升(℃)", "DBYWS_1", "DBYWS_2"),
            new CalculateDataVO.TableRow("高背压端差(℃)", "GBYDCYX_1", "GBYDCYX_2"),
            new CalculateDataVO.TableRow("低背压端差(℃)", "DBYDCYX_1", "DBYDCYX_2"),
            new CalculateDataVO.TableRow("高背压凝汽器传热能效系数(/)", "GBYCRNX_1", "GBYCRNX_2"),
            new CalculateDataVO.TableRow("低背压凝汽器传热能效系数(/)", "DBYCRNX_1", "DBYCRNX_2"),
            new CalculateDataVO.TableRow("大气压力(kPa)", "DQYL_1", "DQYL_2"),
            new CalculateDataVO.TableRow("干球温度(℃)", "GQWD_1", "GQWD_2"));
    private static List<String> points = Arrays.asList(
            "U1P_QC", "U2P_QC",
            "GRLL_1", "GRLL_2",
            "NQQJKZLLL_1", "NQQJKZLLL_2",
            "GBYJSWD_1", "GBYJSWD_2",
            "DBYJSWD_1", "DBYJSWD_2",
            "CTSW_1", "CTSW_2",
            "GBYWS_1", "GBYWS_2",
            "DBYWS_1", "DBYWS_2",
            "GBYDCYX_1", "GBYDCYX_2",
            "DBYDCYX_1", "DBYDCYX_2",
            "GBYCRNX_1", "GBYCRNX_2",
            "DBYCRNX_1", "DBYCRNX_2",
            "DQYL_1", "DQYL_2",
            "GQWD_1", "GQWD_2");
    private static List<String> points2 = Arrays.asList("LQSLLJZ_1", "LQSLLJZ_2");
    private static List<String> scatterPoints = Arrays.asList(
            "U1P_QC",
            "DBYLXJF_1",
            "GBYLXJF_1",
            "U2P_QC",
            "DBYLXJF_2",
            "GBYLXJF_2",
            "U1P_QC",
            "DBYCRNX_1",
            "GBYCRNX_1",
            "U2P_QC",
            "DBYCRNX_2",
            "GBYCRNX_2");
    private static List<String> point2 = Arrays.asList(
            "GBYCKDFKD1_1",
            "GBYCKDFKD2_1",
            "DBYZHRZ_1",
            "DBYSZ_1",
            "DBYZLXS_1",
            "GBYZHRZ_1",
            "GBYSZ_1",
            "GBYZLXS_1");
    private static List<String> point3 = Arrays.asList(
            "GBYCKDFKD1_2",
            "GBYCKDFKD2_2",
            "DBYZHRZ_2",
            "DBYSZ_2",
            "DBYZLXS_2",
            "GBYZHRZ_2",
            "GBYSZ_2",
            "GBYZLXS_2"
    );

    public Map<String, List<Object[]>> getCleanData2(CalculateParam param) {
        return influxService.getHistory("HJB_XBSS", point4,
                param.getStartTime(), param.getEndTime(), param.getPeriod());
    }

    private static List<String> point4 = Arrays.asList(
            "DBYZHRZ_1",
            "U1DYAZKBDL_QC",
            "U1DYBZKBDL_QC",
            "DBYZHRZ_2",
            "U2DYAZKBDL_QC",
            "U2DYBZKBDL_QC",
            "GBYZHRZ_1",
            "U1GYAZKBDL_QC",
            "U1GYBZKBDL_QC",
            "GBYZHRZ_2",
            "U2GYAZKBDL_QC",
            "U2GYBZKBDL_QC"
    );


    private static List<String> point5 = Arrays.asList(
            "GTM1KSGZ_1",
            "GTM2KSGZ_1",
            "GTM3KSGZ_1",
            "GTM4KSGZ_1",
            "GTM1FXTLGZ_1",
            "GTM2FXTLGZ_1",
            "GTM3FXTLGZ_1",
            "GTM4FXTLGZ_1"
    );
    private static List<String> point6 = Arrays.asList(
            "GTMKD1_1",
            "GTMKD2_1",
            "GTMKD3_1",
            "GTMKD4_1",
            "GTMZL1_1",
            "GTMZL2_1",
            "GTMZL3_1",
            "GTMZL4_1"
    );


    private final InfluxService influxService;

    public List<CalculateDataVO.TableRow> getDiagnosisTable() {
        Map<String, Double> realtimeData = influxService.readGroupNow("HJB_XBSS", points);
        realtimeData.putAll(influxService.readGroupNow("HJB_SSYH", points2));
        return tableData.stream()
                .map(row -> {
                    String unit1Value = String.format("%.2f", realtimeData.getOrDefault(row.getUnit1(), 0D));
                    String unit2Value = String.format("%.2f", realtimeData.getOrDefault(row.getUnit2(), 0D));
                    return new CalculateDataVO.TableRow(row.getName(), unit1Value, unit2Value);
                })
                .collect(Collectors.toList());
    }

    public DiagnosisVO getDiagnosisData(CalculateParam param) {
        Map<String, List<Object[]>> data = influxService.getHistory("HJB_XBSS", scatterPoints,
                param.getStartTime(), param.getEndTime(), param.getPeriod());

        // 处理第一组数据
        Map<String, Double> u1p_qc_map = buildTimeValueMap(data.get("U1P_QC"));
        List<Double[]> dbylxjf_1_list = mergeToDoubleArrayList(data.get("DBYLXJF_1"), u1p_qc_map);
        List<Double[]> gbylxjf_1_list = mergeToDoubleArrayList(data.get("GBYLXJF_1"), u1p_qc_map);

        // 第二组
        Map<String, Double> u2p_qc_map = buildTimeValueMap(data.get("U2P_QC"));
        List<Double[]> dbylxjf_2_list = mergeToDoubleArrayList(data.get("DBYLXJF_2"), u2p_qc_map);
        List<Double[]> gbylxjf_2_list = mergeToDoubleArrayList(data.get("GBYLXJF_2"), u2p_qc_map);

        // 第三组（使用 U1P_QC）
        Map<String, Double> u1pQc_map = buildTimeValueMap(data.get("U1P_QC"));
        List<Double[]> dbycrnx_1_list = mergeToDoubleArrayList(data.get("DBYCRNX_1"), u1pQc_map);
        List<Double[]> gbycrnx_1_list = mergeToDoubleArrayList(data.get("GBYCRNX_1"), u1pQc_map);

        // 第四组（使用 U2P_QC）
        Map<String, Double> u2pQc_map = buildTimeValueMap(data.get("U2P_QC"));
        List<Double[]> dbycrnx_2_list = mergeToDoubleArrayList(data.get("DBYCRNX_2"), u2pQc_map);
        List<Double[]> gbycrnx_2_list = mergeToDoubleArrayList(data.get("GBYCRNX_2"), u2pQc_map);
        List<List<Double[]>> thermalEfficiency1 = ListUtil.of(dbycrnx_2_list, gbycrnx_2_list);

        return new DiagnosisVO(
                ListUtil.of(dbylxjf_1_list, gbylxjf_1_list),
                ListUtil.of(dbylxjf_2_list, gbylxjf_2_list),
                ListUtil.of(dbycrnx_1_list, gbycrnx_1_list),
                thermalEfficiency1);
    }

    private Map<String, Double> buildTimeValueMap(List<Object[]> list) {
        Map<String, Double> result = new HashMap<>();
        if (list != null) {
            for (Object[] obj : list) {
                result.put((String) obj[0], (Double) obj[1]);
            }
        }
        return result;
    }

    private List<Double[]> mergeToDoubleArrayList(List<Object[]> sourceList, Map<String, Double> valueMap) {
        List<Double[]> result = new ArrayList<>();
        if (sourceList != null) {
            for (Object[] obj : sourceList) {
                String timestamp = (String) obj[0];
                Double mapValue = valueMap.get(timestamp);
                if (mapValue != null) {
                    result.add(new Double[]{mapValue, (Double) obj[1]});
                }
            }
        }
        return result;
    }

    public Map<String, List<Object[]>> getCleanData(CalculateParam param) {
        return influxService.getHistory("HJB_XBSS", point2,
                param.getStartTime(), param.getEndTime(), param.getPeriod());
    }

    public Map<String, List<Object[]>> getCleanData1(CalculateParam param) {
        return influxService.getHistory("HJB_XBSS", point3,
                param.getStartTime(), param.getEndTime(), param.getPeriod());
    }

    public GateDiagnosisVO getGateDiagnosisData() {
        List<String> points = new ArrayList<>();
        points.addAll(point5);
        points.addAll(point6);
        Map<String, Double> hjbTm = influxService.readGroupNow("HJB_TM", points);
        //hjbTm中获取point5中对应的值，如果值为1，则显示i号调门卡涩，请排查油动机机械卡涩和阀杆机械卡涩，否则为空字符串
        List<String> faults = new ArrayList<>();
        for (int i = 0; i < point5.size(); i++) {
            if (hjbTm.get(point5.get(i)).intValue() == 1) {
                faults.add((i + 1) + "号调门卡涩，请排查油动机机械卡涩和阀杆机械卡涩");
            } else {
                faults.add("");
            }
        }
        //point6 前4个值组成一个list，后4个值组成另一个list
        List<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list1.add(hjbTm.get(point6.get(i)).intValue());
        }
        List<Integer> list2 = new ArrayList<>();
        for (int i = 4; i < 8; i++) {
            list2.add(hjbTm.get(point6.get(i)).intValue());
        }
        return new GateDiagnosisVO(faults, ListUtil.of(list1, list2));
    }

    private static List<String> point7 = Arrays.asList(
            "GTM1KSGZ_2",
            "GTM2KSGZ_2",
            "GTM3KSGZ_2",
            "GTM4KSGZ_2",
            "GTM1FXTLGZ_2",
            "GTM2FXTLGZ_2",
            "GTM3FXTLGZ_2",
            "GTM4FXTLGZ_2"
    );

    private static List<String> point8 = Arrays.asList(
            "GTMKD1_2",
            "GTMKD2_2",
            "GTMKD3_2",
            "GTMKD4_2",
            "GTMZL1_2",
            "GTMZL2_2",
            "GTMZL3_2",
            "GTMZL4_2"
    );
    public GateDiagnosisVO getGateDiagnosisData2() {
        List<String> points = new ArrayList<>();
        points.addAll(point7);
        points.addAll(point8);
        Map<String, Double> hjbTm = influxService.readGroupNow("HJB_TM", points);
        List<String> faults = new ArrayList<>();
        for (int i = 0; i < point7.size(); i++) {
            if (hjbTm.get(point7.get(i)).intValue() == 1) {
                faults.add((i + 1) + "号门卡涩，请排查油动机机械卡涩和阀杆机械卡涩");
            } else {
                faults.add("");
            }
        }
        List<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list1.add(hjbTm.get(point8.get(i)).intValue());
        }
        List<Integer> list2 = new ArrayList<>();
        for (int i = 4; i < 8; i++) {
            list2.add(hjbTm.get(point8.get(i)).intValue());
        }
        return new GateDiagnosisVO(faults, ListUtil.of(list1, list2));
    }
}
