package com.seu.sis.model.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisVO {
    List<List<Double[]>> idealPressureDrop;
    List<List<Double[]>> idealPressureDrop1;
    List<List<Double[]>> thermalEfficiency;
    List<List<Double[]>> thermalEfficiency1;

}
