package com.seu.sis.dao.mapper;

import com.seu.sis.dao.domain.Jhyh;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 陈小黑
* @description 针对表【JHYH】的数据库操作Mapper
* @createDate 2023-11-18 09:24:55
* @Entity com.seu.sis.dao.domain.Jhyh
*/
public interface JhyhMapper extends BaseMapper<Jhyh> {
    boolean updateValue(String tagName, Double value);
}




