package com.seu.sis.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.sis.dao.domain.Ssyh;

/**
 * @author 陈小黑
 * @description 针对表【SSYH】的数据库操作Mapper
 * @createDate 2023-11-18 09:24:46
 * @Entity com.seu.sis.dao.domain.Ssyh
 */
public interface SsyhMapper extends BaseMapper<Ssyh> {
    boolean updateValue(String tagName, Double value);
}




