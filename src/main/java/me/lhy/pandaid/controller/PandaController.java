package me.lhy.pandaid.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.lhy.pandaid.domain.dto.PageDTO;
import me.lhy.pandaid.domain.dto.PandaDTO;
import me.lhy.pandaid.service.PandaService;
import me.lhy.pandaid.util.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/panda")
@Tag(name = "大熊猫管理", description = "大熊猫管理相关接口")
public class PandaController {

    private final PandaService service;

    public PandaController(PandaService service) {
        this.service = service;
    }


    @Operation(summary = "获取所有大熊猫")
    @PostMapping("/getAll")
    public Result<List<PandaDTO>> getAll(@RequestBody PageDTO page) {
        var Pandas = service.getAllWithPage(page.getNum(), page.getSize());
        return Result.success(Pandas);
    }

    @Operation(summary = "根据ID获取单个大熊猫")
    @GetMapping("/getOneById")
    public Result<PandaDTO> getOneById(@RequestParam Long id) {
        var panda = service.getOneById(id);
        return Result.success(panda);
    }

    @Operation(summary = "根据名字获取单个大熊猫")
    @GetMapping("/getOneByName")
    public Result<PandaDTO> getOneByName(@RequestParam String name) {
        var panda = service.getOneByName(name);
        return Result.success(panda);
    }

    @Operation(summary = "根据年龄获取所有大熊猫")
    @GetMapping("/getAllByAge")
    public Result<List<PandaDTO>> getAllByAge(@RequestParam Integer age) {
        var pandas = service.getAllByAge(age);
        return Result.success(pandas);
    }

    @Operation(summary = "根据性别获取所有大熊猫")
    @GetMapping("/getAllBySex")
    public Result<List<PandaDTO>> getAllBySex(@RequestParam Character sex) {
        var pandas = service.getAllBySex(sex);
        return Result.success(pandas);
    }

    @Operation(summary = "获取大熊猫总数")
    @GetMapping("/getCount")
    public Result<Long> getCount() {
        var count = service.getCount();
        return Result.success(count);
    }

    @Operation(summary = "获取已删除的大熊猫")
    @GetMapping("/getDeleted")
    public Result<List<PandaDTO>> getDeleted(@RequestParam int pageNum, @RequestParam int pageSize) {
        var pandas = service.getDeletedWithPage(pageNum, pageSize);
        return Result.success(pandas);
    }

    @Operation(summary = "添加一个大熊猫")
    @PostMapping("/addOne")
    public Result<Void> addOne(@RequestBody PandaDTO pandaDto) {
        service.addOne(pandaDto);
        return Result.success();
    }

    @Operation(summary = "添加多个大熊猫")
    @PostMapping("/addMany")
    public Result<Void> addMany(@RequestBody List<PandaDTO> pandaDTOS) {
        service.addMany(pandaDTOS);
        return Result.success();
    }

    @Operation(summary = "更新一个大熊猫")
    @PutMapping("/updateOne")
    public Result<Void> updateOne(@RequestBody PandaDTO pandaDto) {
        service.updateOne(pandaDto);
        return Result.success();
    }

    @Operation(summary = "根据ID删除一个大熊猫")
    @DeleteMapping("/deleteOneById")
    public Result<Void> deleteOneById(@RequestParam Integer id) {
        service.deleteOneById(id);
        return Result.success();
    }

    @Operation(summary = "根据名字删除一个大熊猫")
    @DeleteMapping("/deleteOneByName")
    public Result<Void> deleteOneByName(@RequestParam String name) {
        service.deleteOneByName(name);
        return Result.success();
    }

    @Operation(summary = "根据ID删除多个大熊猫")
    @DeleteMapping("/deleteMany")
    public Result<Void> deleteMany(@RequestBody List<Integer> ids) {
        service.deleteMany(ids);
        return Result.success();
    }
}
