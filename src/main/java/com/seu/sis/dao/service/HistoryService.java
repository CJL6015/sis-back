package com.seu.sis.dao.service;

import java.util.List;
import java.util.Map;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-18 12:41
 */
public interface HistoryService {
    Map<String, List<Object[]>> getHistory(Integer unitId, String st, String et);

    List<Double[]> getScatter(Integer unitId,
                                  String x,
                                  String y,
                                  String st,
                                  String et);
}
