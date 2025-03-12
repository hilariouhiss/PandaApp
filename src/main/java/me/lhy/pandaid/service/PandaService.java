package me.lhy.pandaid.service;

import me.lhy.pandaid.domain.dto.PandaDTO;

import java.util.List;

public interface PandaService {

    // 查询
    // 查询所有大熊猫
    List<PandaDTO> getAllWithPage(Integer pageNum, Integer pageSize);

    // 根据ID查询单个大熊猫
    PandaDTO getOneById(Long id);
    // 根据编号查询
    PandaDTO getOneByPandaId(Integer panda_id);

    // 根据Name查询单个大熊猫
    PandaDTO getOneByName(String name);

    // 根据Age查询所有大熊猫
    List<PandaDTO> getAllByAge(Integer age);

    // 根据Sex查询所有大熊猫
    List<PandaDTO> getAllBySex(Character sex);

    // 查询所有大熊猫的数量
    Long getCount();

    // 查询被删除的大熊猫
    List<PandaDTO> getDeletedWithPage(int pageNum, int pageSize);

    // 添加
    // 添加单个大熊猫
    void addOne(PandaDTO pandaDto);

    // 添加多个大熊猫
    void addMany(List<PandaDTO> pandaDTOS);

    // 修改
    // 修改单个大熊猫信息
    void updateOne(PandaDTO pandaDto);

    // 删除
    // 通过ID删除单个大熊猫
    void deleteOneById(Integer id);

    // 通过Name删除单个大熊猫
    void deleteOneByName(String name);

    // 通过ID删除多个大熊猫
    void deleteMany(List<Integer> ids);
}
