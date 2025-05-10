package com.seu.sis.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisScatterParam {
    private String start;
    private String end;
    private String period;
}
