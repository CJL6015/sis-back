package com.seu.sis.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.sis.dao.domain.C4Input;
import com.seu.sis.dao.domain.Jhyh;
import com.seu.sis.dao.mapper.JhyhMapper;
import com.seu.sis.dao.mapper.SsyhMapper;
import com.seu.sis.dao.service.C4inputService;
import com.seu.sis.dao.service.C4OutputService;
import com.seu.sis.model.entity.Result;
import com.seu.sis.model.vo.BudgetaryParam;
import com.seu.sis.model.vo.FutureVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-05-12 11:01
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/future")
public class FutureController {
    private final C4inputService c4InputService;

    private final C4OutputService c4OutputService;

    private final JhyhMapper jhyhMapper;
    private final SsyhMapper ssyhMapper;

    @PostMapping("/upload")
    public Result<Boolean> upload(@RequestBody List<FutureVO> data) {
        List<C4Input> C4Inputs = data.stream().map(t ->
                        BeanUtil.copyProperties(t, C4Input.class))
                .collect(Collectors.toList());
        c4InputService.updateBatchById(C4Inputs);
        return Result.success(true);
    }


    @PostMapping("/submit")
    public Result<Boolean> submit(@RequestBody BudgetaryParam param) {
        jhyhMapper.updateValue("SWDJ", param.getElectricity());
        jhyhMapper.updateValue("MJ", param.getCoal());
        return Result.success(jhyhMapper.updateValue("C4_SIGNAL", 1D));
    }

    @GetMapping("/status")
    public Result<Boolean> status() {
        LambdaQueryWrapper<Jhyh> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Jhyh::getTagname, "C4_SIGNAL");
        Jhyh one = jhyhMapper.selectOne(queryWrapper);
        return Result.success(Objects.equals(0, one.getValue().intValue()));
    }

    @GetMapping("/data")
    public Result<List<List<Object>>> getData() {
        List<List<Object>> data = c4OutputService.getData();
        return Result.success(data);
    }
}
