package me.lhy.pandaid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.lhy.pandaid.annotation.LogOperation;
import me.lhy.pandaid.domain.dto.PandaDTO;
import me.lhy.pandaid.domain.po.Panda;
import me.lhy.pandaid.mapper.PandaMapper;
import me.lhy.pandaid.service.PandaService;
import me.lhy.pandaid.util.Constants;
import me.lhy.pandaid.util.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PandaServiceImpl implements PandaService {

    private final PandaMapper mapper;

    public PandaServiceImpl(PandaMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 分页获取所有熊猫信息
     *
     * @return 对应页的熊猫列表
     */
    @LogOperation(value = "获取所有熊猫信息")
    @Override
    public List<PandaDTO> getAllWithPage(Integer pageNum, Integer pageSize) {
        Page<Panda> page = new Page<>(pageNum, pageSize);
        List<Panda> pandas = mapper.selectPage(page, null).getRecords();
        return pandas.stream().map(Converter.INSTANCE::toPandaDto).toList();
    }

    /**
     * 根据id获取单个熊猫信息
     *
     * @param id 熊猫表主键
     * @return 单个熊猫信息
     */
    @LogOperation(value = "根据id获取单个熊猫信息")
    @Override
    public PandaDTO getOneById(Long id) {
        Panda panda = mapper.selectById(id);
        return Converter.INSTANCE.toPandaDto(panda);
    }

    /**
     * 根据panda_id获取熊猫信息
     *
     * @param panda_id 大熊猫编号
     * @return 单个熊猫信息
     */
    @Override
    public PandaDTO getOneByPandaId(Integer panda_id) {
        Panda panda = mapper.selectOne(new LambdaQueryWrapper<Panda>()
                .eq(Panda::getPandaId, panda_id));
        return Converter.INSTANCE.toPandaDto(panda);
    }

    /**
     * 根据名字获取单个熊猫信息
     *
     * @param name 熊猫名
     * @return 单个熊猫信息
     */
    @LogOperation(value = "根据名字获取单个熊猫信息")
    @Override
    public PandaDTO getOneByName(String name) {
        Panda panda = mapper.selectOne(new LambdaQueryWrapper<Panda>().eq(Panda::getPandaName, name));
        return Converter.INSTANCE.toPandaDto(panda);
    }

    /**
     * 根据年龄获取所有熊猫信息
     *
     * @param age 年龄
     * @return 所有熊猫信息
     */
    @LogOperation(value = "根据年龄获取所有熊猫信息")
    @Override
    public List<PandaDTO> getAllByAge(Integer age) {
        return mapper.selectList(new LambdaQueryWrapper<Panda>()
                             .eq(Panda::getPandaAge, age))
                     .stream()
                     .map(Converter.INSTANCE::toPandaDto)
                     .toList();
    }

    /**
     * 根据性别获取所有熊猫信息
     *
     * @param sex 性别
     * @return 所有熊猫信息
     */
    @LogOperation(value = "根据性别获取所有熊猫信息")
    @Override
    public List<PandaDTO> getAllBySex(Character sex) {
        return mapper.selectList(new LambdaQueryWrapper<Panda>()
                             .eq(Panda::getPandaSex, sex))
                     .stream()
                     .map(Converter.INSTANCE::toPandaDto)
                     .toList();
    }

    /**
     * 获取熊猫总数
     *
     * @return 熊猫总数
     */
    @LogOperation(value = "获取熊猫总数")
    @Override
    public Long getCount() {
        return mapper.selectCount(null);
    }

    /**
     * 获取已删除的熊猫信息
     *
     * @return 已删除的熊猫信息
     */
    @LogOperation(value = "获取已删除的熊猫信息")
    @Override
    public List<PandaDTO> getDeletedWithPage(int pageNum, int pageSize) {
        Page<Panda> page = new Page<>(pageNum, pageSize);
        List<Panda> pandas = mapper.selectPage(page, new LambdaQueryWrapper<Panda>()
                .eq(Panda::getDeleted, true)).getRecords();
        return pandas.stream()
                     .map(Converter.INSTANCE::toPandaDto)
                     .toList();
    }

    /**
     * 添加一个熊猫信息
     *
     * @param pandaDto 单个熊猫信息
     */
    @LogOperation(value = "添加一个熊猫信息")
    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public void addOne(PandaDTO pandaDto) {
        Panda panda = Converter.INSTANCE.toPanda(pandaDto);
        mapper.insert(panda);
    }

    /**
     * 添加多个熊猫信息
     *
     * @param pandaDTOS 多个熊猫信息
     */
    @LogOperation(value = "添加多个熊猫信息")
    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public void addMany(List<PandaDTO> pandaDTOS) {
        if (pandaDTOS.isEmpty()) {
            return;
        }
        List<Panda> pandas = pandaDTOS.stream()
                                      .map(Converter.INSTANCE::toPanda)
                                      .toList();
        mapper.insert(pandas, Constants.INSERT_BATCH_SIZE);
    }


    /**
     * 更新一个熊猫信息
     *
     * @param pandaDto 单个熊猫信息
     */
    @LogOperation(value = "更新一个熊猫信息")
    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public void updateOne(PandaDTO pandaDto) {
        Panda panda = Converter.INSTANCE.toPanda(pandaDto);
        mapper.updateById(panda);
    }

    /**
     * 根据ID删除一个熊猫信息
     *
     * @param id 熊猫id
     */
    @LogOperation(value = "根据ID删除一个熊猫信息")
    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public void deleteOneById(Integer id) {
        mapper.deleteById(id);
    }

    /**
     * 根据名字删除一个熊猫信息
     *
     * @param name 熊猫名
     */
    @LogOperation(value = "根据名字删除一个熊猫信息")
    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public void deleteOneByName(String name) {
        mapper.delete(new LambdaQueryWrapper<Panda>()
                .eq(Panda::getPandaName, name));
    }

    /**
     * 根据ID删除多个熊猫信息
     *
     * @param ids 熊猫id
     */
    @LogOperation(value = "根据ID删除多个熊猫信息")
    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public void deleteMany(List<Integer> ids) {
        mapper.deleteByIds(ids);
    }

    /**
     * 物理删除已deleted=true的大熊猫
     */
    @LogOperation(value = "物理删除已逻辑删除的大熊猫")
    @Transactional(rollbackFor = {RuntimeException.class})
    @Override
    public void physicalDelete() {
        mapper.delete(new LambdaQueryWrapper<Panda>()
                .eq(Panda::getDeleted, true));
    }
}
