package com.seu.sis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CalculateDataVO {
    private List<TableRow> tableData;
    private List<List<Double>> chartData;
    private int max;
    private int min;


    @Data
    @AllArgsConstructor
    public static class TableRow {
        private String name;
        private String unit1;
        private String unit2;
    }
}
