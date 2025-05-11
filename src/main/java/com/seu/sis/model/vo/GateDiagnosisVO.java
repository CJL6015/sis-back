package com.seu.sis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GateDiagnosisVO {
    private List<String> faults;
    private List<List<Integer>> chartValue;
}
