package com.seu.sis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResultVO {
    private List<String> tableData;

    private Map<String, List<Object[]>> chartValue;

    private String status;
}
