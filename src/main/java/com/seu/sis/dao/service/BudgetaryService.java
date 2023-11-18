package com.seu.sis.dao.service;

import com.seu.sis.model.vo.BudgetaryParam;

import java.util.List;
import java.util.Map;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-15 21:51
 */
public interface BudgetaryService {
    /**
     * 获取节支概算图数据
     *
     * @return 结果
     */
    List<List<Double>> getBudgetaryData();

    /**
     * 获取参数
     *
     * @return 返回参数
     */
    Map<String, Double> getParam();

    boolean submit(BudgetaryParam param);

    List<List<Object>> getData();
}
