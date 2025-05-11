package com.seu.sis.service;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.sis.dao.domain.Xbpw;
import com.seu.sis.dao.service.XbpwService;
import com.seu.sis.influx.InfluxService;
import com.seu.sis.model.vo.CalculateDataVO;
import com.seu.sis.model.vo.CalculateParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.seu.sis.model.vo.CalculateDataVO.TableRow;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculateService {
    private static List<String> unitPoints = Arrays.asList(
            "GTMKD1_1", "GTMKD1_2",
            "GTMKD2_1", "GTMKD2_2",
            "GTMKD3_1", "GTMKD3_2",
            "GTMKD4_1", "GTMKD4_2",
            "GTMZL1_1", "GTMZL1_2",
            "GTMZL2_1", "GTMZL2_2",
            "GTMZL3_1", "GTMZL3_2",
            "GTMZL4_1", "GTMZL4_2",
            "ZFWZL_1", "ZFWZL_2",
            "ZQYL_1", "ZQYL_2",
            "TJJYL_1", "TJJYL_2",
            "SJLL_1", "SJLL_2"
    );

    private static List<String> points = ListUtil.of("LXXBPWXH", "P_1", "LXZDSY", "P_2", "LXZDMLR", "DQJZSL", "SJXBPWXH", "XHSBAZT",
            "SJSY", "XHSBBZT", "JZSY", "XHSBCZT", "SSSYBFB", "XHSBDZT", "LJSJSY", "AXBDJDL",
            "LJLXZDSY", "BXBDJDL", "LJJZSY", "CXBDJDL", "PJSYBFB", "DXBDJDL", "QCNQQZRFH_6",
            "RMCB_6", "SDSY_6", "MLR_6", "ZRML_6", "XDLR_6", "QCNQQZRFH_5", "RMCB_5", "SDSY_5",
            "MLR_5", "ZRML_5", "XDLR_5", "QCNQQZRFH_4", "RMCB_4", "SDSY_4", "MLR_4", "ZRML_4",
            "XDLR_4", "QCNQQZRFH_3", "RMCB_3", "SDSY_3", "MLR_3", "ZRML_3", "XDLR_3",
            "QCNQQZRFH_2", "RMCB_2", "SDSY_2", "MLR_2", "ZRML_2", "XDLR_2", "QCNQQZRFH_1",
            "RMCB_1", "SDSY_1", "MLR_1", "ZRML_1", "XDLR_1", "QCNQQZRFH_9", "RMCB_9",
            "SDSY_9", "MLR_9", "ZRML_9", "XDLR_9", "QCNQQZRFH_8", "RMCB_8", "SDSY_8",
            "MLR_8", "ZRML_8", "XDLR_8", "QCNQQZRFH_7", "RMCB_7", "SDSY_7", "MLR_7",
            "ZRML_7", "XDLR_7");

    private List<TableRow> tableData = Arrays.asList(
            new TableRow("高调门开度1(%)", "GTMKD1_1", "GTMKD1_2"),
            new TableRow("高调门开度2(%)", "GTMKD2_1", "GTMKD2_2"),
            new TableRow("高调门开度3(%)", "GTMKD3_1", "GTMKD3_2"),
            new TableRow("高调门开度4(%)", "GTMKD4_1", "GTMKD4_2"),
            new TableRow("高调门指令1(%)", "GTMZL1_1", "GTMZL1_2"),
            new TableRow("高调门指令2(%)", "GTMZL2_1", "GTMZL2_2"),
            new TableRow("高调门指令3(%)", "GTMZL3_1", "GTMZL3_2"),
            new TableRow("高调门指令4(%)", "GTMZL4_1", "GTMZL4_2"),
            new TableRow("总阀位指令(%)", "ZFWZL_1", "ZFWZL_2"),
            new TableRow("主汽压力(MPa)", "ZQYL_1", "ZQYL_2"),
            new TableRow("实际流量(%)", "SJLL_1", "SJLL_2")
    );
    private final InfluxService influxService;
    private final XbpwService xbpwService;

    public CalculateDataVO getCalculateDataVO(CalculateParam param) {
        Integer unitId = param.getUnitId();
        List<List<Double>> combinedData = new ArrayList<>();
        List<String> unitPoint = new ArrayList<>();
        if (unitId == 1) {
            Map<String, List<Object[]>> hjbTm = influxService.getHistory("HJB_TM", ListUtil.of("ZFWZL_1", "SJLL_1"),
                    param.getStartTime(), param.getEndTime(), param.getPeriod());
            Map<String, Double> sjll1Data = hjbTm.get("ZFWZL_1").stream()
                    .collect(Collectors.toMap(data -> (String) data[0], data -> (Double) data[1]));
            Map<String, Double> sjll2Data = hjbTm.get("SJLL_1").stream()
                    .collect(Collectors.toMap(data -> (String) data[0], data -> (Double) data[1]));


            for (String time : sjll1Data.keySet()) {
                if (sjll2Data.containsKey(time)) {
                    combinedData.add(Arrays.asList(sjll1Data.get(time), sjll2Data.get(time)));
                }
            }
            unitPoint = tableData.stream()
                    .map(TableRow::getUnit1)
                    .collect(Collectors.toList());
        } else {
            Map<String, List<Object[]>> hjbTm = influxService.getHistory("HJB_TM", ListUtil.of("ZFWZL_2", "SJLL_2"),
                    param.getStartTime(), param.getEndTime(), param.getPeriod());
            Map<String, Double> sjll1Data = hjbTm.get("ZFWZL_2").stream()
                    .collect(Collectors.toMap(data -> (String) data[0], data -> (Double) data[1]));
            Map<String, Double> sjll2Data = hjbTm.get("SJLL_2").stream()
                    .collect(Collectors.toMap(data -> (String) data[0], data -> (Double) data[1]));

            for (String time : sjll1Data.keySet()) {
                if (sjll2Data.containsKey(time)) {
                    combinedData.add(Arrays.asList(sjll1Data.get(time), sjll2Data.get(time)));
                }
            }
            unitPoint = tableData.stream()
                    .map(TableRow::getUnit2)
                    .collect(Collectors.toList());
        }

        Map<String, Double> realtimeData = influxService.readGroupNow("HJB_TM", unitPoint);
        List<TableRow> processedTable;
        int maxRoundedUp = 0, minRoundedDown = 0;
        if (unitId == 1) {
            processedTable = tableData.stream()
                    .map(row -> {
                        String unit1Value = String.format("%.2f", realtimeData.getOrDefault(row.getUnit1(), 0D));
                        return new TableRow(row.getName(), unit1Value, "-");
                    })
                    .collect(Collectors.toList());
            List<Double> allValues = new ArrayList<>();
            for (List<Double> row : combinedData) {
                allValues.addAll(row);
            }
            // 计算最大值和最小值
            Double maxValue = Collections.max(allValues);
            Double minValue = Collections.min(allValues);
            maxRoundedUp = (int) Math.ceil(maxValue);
            minRoundedDown = (int) Math.floor(minValue);
        } else {
            processedTable = tableData.stream()
                    .map(row -> {
                        String unit2Value = String.format("%.2f", realtimeData.getOrDefault(row.getUnit2(), 0D));
                        return new TableRow(row.getName(), "-", unit2Value);
                    })
                    .collect(Collectors.toList());
            List<Double> allValues = new ArrayList<>();
            for (List<Double> row : combinedData) {
                allValues.addAll(row);
            }
            // 获取所有值
            Double maxValue = Collections.max(allValues);
            Double minValue = Collections.min(allValues);
            maxRoundedUp = (int) Math.ceil(maxValue);
            minRoundedDown = (int) Math.floor(minValue);
        }


        return new CalculateDataVO(processedTable, combinedData, maxRoundedUp, minRoundedDown);
    }

    public List<DataRow> getRealtimeData() {
        List<DataRow> dataList = new ArrayList<>();
        // 添加数据
        dataList.add(new DataRow("理想循泵配伍序号(-)", "LXXBPWXH", "#1机发电机功率(MW)", "P_1"));
        dataList.add(new DataRow("理想最大收益（相对利润）(万元/h)", "LXZDSY", "#2机发电机功率(MW)", "P_2"));
        dataList.add(new DataRow("理想最大毛利润(万元/h)", "LXZDMLR", "当前机组运行数量(-)", "DQJZSL"));
        dataList.add(new DataRow("实际循泵配伍序号(-)", "SJXBPWXH", "循环水泵A状态(-)", "XHSBAZT"));
        dataList.add(new DataRow("实际收益（相对利润）(万元/h)", "SJSY", "循环水泵B状态(-)", "XHSBBZT"));
        dataList.add(new DataRow("节支收益（相对利润）(万元/h)", "JZSY", "循环水泵C状态(-)", "XHSBCZT"));
        dataList.add(new DataRow("实时收益百分比(-)", "SSSYBFB", "循环水泵D状态(-)", "XHSBDZT"));
        dataList.add(new DataRow("24h累计实际收益（相对利润）(万元)", "LJSJSY", "A循泵电机电流(A)", "AXBDJDL"));
        dataList.add(new DataRow("24h累计理想最大收益（相对利润）(万元)", "LJLXZDSY", "B循泵电机电流(A)", "BXBDJDL"));
        dataList.add(new DataRow("24h累计节支收益（相对利润）(万元)", "LJJZSY", "C循泵电机电流(A)", "CXBDJDL"));
        dataList.add(new DataRow("24h累计收益百分比(-)", "PJSYBFB", "D循泵电机电流(A)", "DXBDJDL"));
        dataList.add(new DataRow("配伍6全厂凝汽器总热负荷(kW)", "QCNQQZRFH_6", "配伍6燃煤成本(万元/h)", "RMCB_6"));
        dataList.add(new DataRow("配伍6售电收益(万元/h)", "SDSY_6", "配伍6毛利润(万元/h)", "MLR_6"));
        dataList.add(new DataRow("配伍6总燃煤量(吨/h)", "ZRML_6", "配伍6相对利润(万元/h)", "XDLR_6"));

        dataList.add(new DataRow("配伍5全厂凝汽器总热负荷(kW)", "QCNQQZRFH_5", "配伍5燃煤成本(万元/h)", "RMCB_5"));
        dataList.add(new DataRow("配伍5售电收益(万元/h)", "SDSY_5", "配伍5毛利润(万元/h)", "MLR_5"));
        dataList.add(new DataRow("配伍5总燃煤量(吨/h)", "ZRML_5", "配伍5相对利润(万元/h)", "XDLR_5"));

        dataList.add(new DataRow("配伍4全厂凝汽器总热负荷(kW)", "QCNQQZRFH_4", "配伍4燃煤成本(万元/h)", "RMCB_4"));
        dataList.add(new DataRow("配伍4售电收益(万元/h)", "SDSY_4", "配伍4毛利润(万元/h)", "MLR_4"));
        dataList.add(new DataRow("配伍4总燃煤量(吨/h)", "ZRML_4", "配伍4相对利润(万元/h)", "XDLR_4"));

        dataList.add(new DataRow("配伍3全厂凝汽器总热负荷(kW)", "QCNQQZRFH_3", "配伍3燃煤成本(万元/h)", "RMCB_3"));
        dataList.add(new DataRow("配伍3售电收益(万元/h)", "SDSY_3", "配伍3毛利润(万元/h)", "MLR_3"));
        dataList.add(new DataRow("配伍3总燃煤量(吨/h)", "ZRML_3", "配伍3相对利润(万元/h)", "XDLR_3"));

        dataList.add(new DataRow("配伍2全厂凝汽器总热负荷(kW)", "QCNQQZRFH_2", "配伍2燃煤成本(万元/h)", "RMCB_2"));
        dataList.add(new DataRow("配伍2售电收益(万元/h)", "SDSY_2", "配伍2毛利润(万元/h)", "MLR_2"));
        dataList.add(new DataRow("配伍2总燃煤量(吨/h)", "ZRML_2", "配伍2相对利润(万元/h)", "XDLR_2"));

        dataList.add(new DataRow("配伍1全厂凝汽器总热负荷(kW)", "QCNQQZRFH_1", "配伍1燃煤成本(万元/h)", "RMCB_1"));
        dataList.add(new DataRow("配伍1售电收益(万元/h)", "SDSY_1", "配伍1毛利润(万元/h)", "MLR_1"));
        dataList.add(new DataRow("配伍1总燃煤量(吨/h)", "ZRML_1", "配伍1相对利润(万元/h)", "XDLR_1"));

        dataList.add(new DataRow("配伍9全厂凝汽器总热负荷(kW)", "QCNQQZRFH_9", "配伍9燃煤成本(万元/h)", "RMCB_9"));
        dataList.add(new DataRow("配伍9售电收益(万元/h)", "SDSY_9", "配伍9毛利润(万元/h)", "MLR_9"));
        dataList.add(new DataRow("配伍9总燃煤量(吨/h)", "ZRML_9", "配伍9相对利润(万元/h)", "XDLR_9"));

        dataList.add(new DataRow("配伍8全厂凝汽器总热负荷(kW)", "QCNQQZRFH_8", "配伍8燃煤成本(万元/h)", "RMCB_8"));
        dataList.add(new DataRow("配伍8售电收益(万元/h)", "SDSY_8", "配伍8毛利润(万元/h)", "MLR_8"));
        dataList.add(new DataRow("配伍8总燃煤量(吨/h)", "ZRML_8", "配伍8相对利润(万元/h)", "XDLR_8"));

        dataList.add(new DataRow("配伍7全厂凝汽器总热负荷(kW)", "QCNQQZRFH_7", "配伍7燃煤成本(万元/h)", "RMCB_7"));
        dataList.add(new DataRow("配伍7售电收益(万元/h)", "SDSY_7", "配伍7毛利润(万元/h)", "MLR_7"));
        dataList.add(new DataRow("配伍7总燃煤量(吨/h)", "ZRML_7", "配伍7相对利润(万元/h)", "XDLR_7"));

        Map<String, Double> hjbSsyh = influxService.readGroupNow("HJB_SSYH", points);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        List<DataRow> updatedTableData = new ArrayList<>();

        for (DataRow row : dataList) {
            String updatedValue1 = hjbSsyh.containsKey(row.getValue1())
                    ? decimalFormat.format(hjbSsyh.get(row.getValue1()))
                    : row.getValue1(); // 如果点号不存在，则保留原值

            String updatedValue2 = hjbSsyh.containsKey(row.getValue2())
                    ? decimalFormat.format(hjbSsyh.get(row.getValue2()))
                    : row.getValue2(); // 如果点号不存在，则保留原值

            updatedTableData.add(new DataRow(row.getName1(), updatedValue1, row.getName2(), updatedValue2));
        }
        return updatedTableData;
    }

    public static class DataRow {
        private String name1;
        private String value1;
        private String name2;
        private String value2;

        public DataRow(String name1, String value1, String name2, String value2) {
            this.name1 = name1;
            this.value1 = value1;
            this.name2 = name2;
            this.value2 = value2;
        }

        public String getName1() {
            return name1;
        }

        public String getValue1() {
            return value1;
        }

        public String getName2() {
            return name2;
        }

        public String getValue2() {
            return value2;
        }

        @Override
        public String toString() {
            return "DataRow{" +
                    "name1='" + name1 + '\'' +
                    ", value1='" + value1 + '\'' +
                    ", name2='" + name2 + '\'' +
                    ", value2='" + value2 + '\'' +
                    '}';
        }
    }

    public List<Overview> getOverview() {

        String sjxbpwxh = "", lxxbpwxh = "";
        List<Overview> overviews = new ArrayList<>();
        List<String> points = ListUtil.of("SJSY", "LXZDSY", "JZSY", "LJLXZDSY", "LJSJSY", "LJJZSY", "DQXBPWXH_QC", "LXXBPWXH");
        Map<String, Double> hjbSsyh = influxService.readGroupNow("HJB_SSYH", points);
        List<Xbpw> xbpws = xbpwService.list();
        for (Xbpw xbpw : xbpws) {
            if (Objects.equals(xbpw.getPw().intValue(), hjbSsyh.getOrDefault("DQXBPWXH_QC", 0D).intValue())) {
                sjxbpwxh = xbpw.getDes();
            }
            if (Objects.equals(xbpw.getPw().intValue(), hjbSsyh.getOrDefault("LXXBPWXH", 0D).intValue())) {
                lxxbpwxh = xbpw.getDes();
            }
        }
        overviews.add(new Overview("循泵配伍方式", sjxbpwxh, lxxbpwxh, "/"));
        overviews.add(new Overview("相对利润（收益），万元/h", hjbSsyh.get("SJSY") + "", hjbSsyh.get("LXZDSY") + "", hjbSsyh.get("JZSY") + ""));
        overviews.add(new Overview("24h累计相对利润（收益），万元", hjbSsyh.get("LJSJSY") + "", hjbSsyh.get("LJLXZDSY") + "", hjbSsyh.get("LJJZSY") + ""));
        return overviews;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Overview {
        private String name;
        private String now;
        private String optimize;
        private String diff;

    }

}
