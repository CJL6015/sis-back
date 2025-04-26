package com.seu.sis.dao.service;

import com.seu.sis.dao.domain.C4Output;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 陈小黑
* @description 针对表【C4_Output】的数据库操作Service
* @createDate 2024-05-12 12:58:45
*/
public interface C4OutputService extends IService<C4Output> {

    List<List<Object>> getData();

}
