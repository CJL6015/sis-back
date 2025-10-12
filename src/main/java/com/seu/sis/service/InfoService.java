package com.seu.sis.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import com.seu.sis.dao.domain.Jhyh;
import com.seu.sis.dao.domain.Svld;
import com.seu.sis.dao.domain.Svtm;
import com.seu.sis.dao.domain.Xbpw;
import com.seu.sis.dao.service.JhyhService;
import com.seu.sis.dao.service.SvldService;
import com.seu.sis.dao.service.SvtmService;
import com.seu.sis.dao.service.XbpwService;
import com.seu.sis.influx.InfluxService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-05-12 18:42
 */
@Service
@RequiredArgsConstructor
public class InfoService {
    private static final List<String> points1 = ListUtil.of("DQJZSL_QC",
            "LQSZLL_QC",
            "XBZGH_QC",
            "XHSBACZT_1",
            "XHSBBDZT_1",
            "XHSBACZT_2",
            "XHSBBDZT_2",
            "U1P_QC",
            "NQQRFH_1",
            "GBY_1",
            "DBY_1",
            "U2P_QC",
            "NQQRFH_2",
            "GBY_2",
            "DBY_2",
            "DQXBPWXH_QC", "LQTFG_1", "CTSW_1", "JTSQWD_1", "LQTFG_2", "CTSW_2", "JTSQWD_2");

    private static final List<String> points2 = ListUtil.of("LXZDSY",
            "LXZDMLR",
            "SJSY",
            "SSSYBFB",
            "PJSYBFB",
            "LXXBPWXH");

    private static final List<String> key1 = ListUtil.of("#1机组负荷（MW）",
            "#1机组凝汽器热负荷（MW）",
            "#1机组凝汽器压力（kPa）",
            "#2机组负荷（MW）",
            "#2机组凝汽器热负荷（MW）",
            "#2机组凝汽器压力（kPa）",
            "#3机组负荷（MW）",
            "#3机组凝汽器热负荷（MW）",
            "#3机组凝汽器压力（kPa）",
            "#4机组负荷（MW）",
            "#4机组凝汽器热负荷（MW）",
            "#4机组凝汽器压力（kPa）");

    private static final List<String> value1 = ListUtil.of("P_MW_1",
            "NQQRFH_MW_1",
            "DCSNQQYL_1",
            "P_MW_2",
            "NQQRFH_MW_2",
            "DCSNQQYL_2",
            "P_MW_3",
            "NQQRFH_MW_3",
            "DCSNQQYL_3",
            "P_MW_4",
            "NQQRFH_MW_4",
            "DCSNQQYL_4");

    private static final List<String> key2 = ListUtil.of("#1循环水泵运行状态",
            "#2循环水泵运行状态",
            "#3循环水泵运行状态",
            "#4循环水泵运行状态",
            "#5循环水泵运行状态",
            "#6循环水泵运行状态",
            "#7循环水泵运行状态",
            "#8循环水泵运行状态",
            "冷却水总流量（t/h）",
            "冷却水入口温度（℃）",
            "全厂凝汽器总热负荷（MW）");
    private static final List<String> value2 = ListUtil.of("XB1_YXZT",
            "XB2_YXZT",
            "XB3_YXZT",
            "XB4_YXZT",
            "XB5_YXZT",
            "XB6_YXZT",
            "XB7_YXZT",
            "XB8_YXZT",
            "LQSLL_QC",
            "PJJSWD_QC",
            "NQQRFH_QC");
    private static final List<String> key3 = ListUtil.of("全厂总负荷（MW）",
            "当前机组运行数量",
            "当前循泵数量",
            "优化循泵数量",
            "电价（元/kWh）",
            "标煤单价（元/吨）",
            "理想最大相对利润（万元/h）",
            "理想最大毛利润（万元/h）",
            "实际运行相对利润（万元/h）",
            "实时收益百分比（%）",
            "24h累计收益百分比（%）");
    private static final List<String> value3 = ListUtil.of("P_QC",
            "DQJZSL_QC",
            "DQXBSL_QC",
            "LXBS_C2",
            "C3_SWDJ",
            "C3_MJ",
            "LXZDXDLR_C2",
            "LXZDMLR_C2",
            "SJYXXDLR_C2",
            "SSSYBFB_C2",
            "PJSYBFB_C2");

    public static final List<Map<String, String>> INFO = Collections.unmodifiableList(Arrays.asList(
            createGroup("#1机发电功率(MW)", "U1P_QC", "当前机组运行数量", "DQJZSL_QC", "电价(元/kWh)", "SWDJ"),
            createGroup("#1机凝汽器热负荷(kW)", "NQQRFH_1", "冷却水总流量(t/h)", "LQSZLL_QC", "标煤单价(元/吨)", "MJ"),
            createGroup("#1机高背压均值(kPa)", "GBY_1", "循泵总功耗(kW)", "XBZGH_QC", "理想循泵配伍", "LXXBPWXH"),
            createGroup("#1机低背压均值(kPa)", "DBY_1", "循环水泵A状态", "XHSBACZT_1", "理想最大收益(万元/h)", "LXZDSY"),
            createGroup("#2机发电功率(MW)", "U2P_QC", "循环水泵B状态", "XHSBBDZT_1", "理想最大毛利润(万元/h)", "LXZDMLR"),
            createGroup("#2机凝汽器热负荷(kW)", "NQQRFH_2", "循环水泵C状态", "XHSBACZT_2", "实际收益(万元/h)", "SJSY"),
            createGroup("#2机高背压均值(kPa)", "GBY_2", "循环水泵D状态", "XHSBBDZT_2", "实时收益百分比(-)", "SSSYBFB"),
            createGroup("#2机低背压均值(kPa)", "DBY_2", "实际循泵配伍", "DQXBPWXH_QC", "24h累计收益百分比(-)", "PJSYBFB"),
            createGroup("#1机冷却塔幅高值(℃)", "LQTFG_1", "#1机冷却塔出塔水温(℃)", "CTSW_1", "#1机进塔湿球温度(℃)", "JTSQWD_1"),
            createGroup("#2机冷却塔幅高值(℃)", "LQTFG_2", "#2机冷却塔出塔水温(℃)", "CTSW_2", "#2机进塔湿球温度(℃)", "JTSQWD_2")
    ));

    private static Map<String, String> createGroup(String k1, String v1, String k2, String v2, String k3, String v3) {
        Map<String, String> group = new HashMap<>();
        group.put("k1", k1);
        group.put("v1", v1);
        group.put("k2", k2);
        group.put("v2", v2);
        group.put("k3", k3);
        group.put("v3", v3);
        return group;
    }

    private static List<String> intFormat = ListUtil.of("DQJZSL_QC", "XHSBACZT_1", "XHSBBDZT_1", "XHSBACZT_2", "XHSBBDZT_2");

    private static List<String> FOUR_DIGITS = ListUtil.of("SWDJ", "LXZDSY", "LXZDMLR", "SJSY");

    private final InfluxService influxService;

    private final JhyhService jhyhService;

    private final XbpwService xbpwService;

    private final SvldService svldService;

    private final SvtmService svtmService;

    public List<Map<String, String>> getInfo() {

        DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormat df4 = new DecimalFormat("0.0000");
        Map<String, String> data = influxService.readGroupNow("HJB_XBSS", points1).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            if (intFormat.contains(e.getKey())) {
                                return String.valueOf(e.getValue().intValue());
                            } else if (FOUR_DIGITS.contains(e.getKey())) {
                                return df4.format(e.getValue().floatValue());
                            } else {
                                return df.format(e.getValue());
                            }
                        }
                ));
        Map<String, String> groupNow2 = influxService.readGroupNow("HJB_SSYH", points2).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            if (intFormat.contains(e.getKey())) {
                                return String.valueOf(e.getValue().intValue());
                            } else if (FOUR_DIGITS.contains(e.getKey())) {
                                return df4.format(e.getValue().floatValue());
                            } else {
                                return df.format(e.getValue());
                            }
                        }
                ));
        data.putAll(groupNow2);
        List<Jhyh> list = jhyhService.list();
        list.forEach(t -> {
            String str;
            if (intFormat.contains(t.getTagname())) {
                str = String.valueOf(t.getValue().intValue());
            } else if (FOUR_DIGITS.contains(t.getTagname())) {
                str = df4.format(t.getValue().floatValue());
            } else {
                str = df.format(t.getValue());
            }
            data.put(t.getTagname(), str);
        });
        List<Xbpw> xbpws = xbpwService.list();
        data.put("DQXBPWXH_QC", xbpws.stream().filter(t -> df.format(t.getPw()).equals(data.get("DQXBPWXH_QC"))).findFirst().get().getDes());
        data.put("LXXBPWXH", xbpws.stream().filter(t -> df.format(t.getPw()).equals(data.get("LXXBPWXH"))).findFirst().get().getDes());

        List<Map<String, String>> result = new ArrayList<>();

        for (Map<String, String> group : INFO) {
            Map<String, String> newGroup = new HashMap<>(group);
            String v1Key = group.get("v1");
            String v2Key = group.get("v2");
            String v3Key = group.get("v3");

            newGroup.put("v1", data.getOrDefault(v1Key, ""));
            newGroup.put("v2", data.getOrDefault(v2Key, ""));
            newGroup.put("v3", data.getOrDefault(v3Key, ""));

            result.add(newGroup);
        }
        return result;
    }

    public PointInfo getPointInfo() {
        String info = "";
        String point = "";
        for (Svld svld : svldService.list()) {
            if (svld.getTagname().equals("Signal")) {
                if (svld.getValue().equals("0")) {
                    return null;
                }
            }
            if (svld.getTagname().equals("Info")) {
                point = svld.getValue();
            } else if (svld.getTagname().equals("TagList")) {
                info = svld.getValue();
            }
        }
        return new PointInfo(info, point);
    }

    public PointInfo getPointInfo1() {
        //svtm
        String info = "";
        String point = "";
        for (Svtm svtm : svtmService.list()) {
            if (svtm.getTagname().equals("Signal")) {
                if (svtm.getValue().equals("0")) {
                    return null;
                }
            }
            if (svtm.getTagname().equals("Info")) {
                point = svtm.getValue();
            } else if (svtm.getTagname().equals("TagList")) {
                info = svtm.getValue();
            }
        }
        return new PointInfo(info, point);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PointInfo {
        private String info;
        private String point;
    }
}
