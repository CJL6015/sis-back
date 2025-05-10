package com.seu.sis.service;

import cn.hutool.core.collection.ListUtil;
import com.seu.sis.influx.InfluxService;
import com.seu.sis.model.vo.CalculateDataVO;
import com.seu.sis.model.vo.CalculateParam;
import com.seu.sis.model.vo.DiagnosisVO;
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
            "DBYCKDFKD2_1",
            "DBYZHRZ_1",
            "DBYSZ_1",
            "DBYZLXS_1",
            "GBYZHRZ_1",
            "GBYSZ_1",
            "GBYZLXS_1");
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
                    result.add(new Double[] { mapValue, (Double) obj[1] });
                }
            }
        }
        return result;
    }

    public Map<String, List<Object[]>> getCleanData(CalculateParam param) {
        return influxService.getHistory("HJB_XBSS", point2,
                param.getStartTime(), param.getEndTime(), param.getPeriod());
    }


}
