package me.lhy.pandaid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.lhy.pandaid.domain.dto.PandaDto;
import me.lhy.pandaid.domain.po.Panda;
import me.lhy.pandaid.mapper.PandaMapper;
import me.lhy.pandaid.service.PandaService;
import me.lhy.pandaid.util.Constants;
import me.lhy.pandaid.util.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("pandaService")
@Slf4j
public class PandaServiceImpl implements PandaService {

    private final PandaMapper mapper;

    public PandaServiceImpl(PandaMapper mapper) {
        this.mapper = mapper;
    }


    /**
     * 获取所有熊猫信息
     *
     * @return 所有熊猫信息
     */
    @Override
    public List<PandaDto> getAllWithPage(Integer pageNum, Integer pageSize) {
        Page<Panda> page = new Page<>(pageNum, pageSize);
        List<Panda> pandas = mapper.selectPage(page, null).getRecords();
        log.info("第 {} 页，{} 只熊猫", pageNum, pandas.size());
        return pandas.stream().map(Converter.INSTANCE::toPandaDto).toList();
    }

    /**
     * 根据id获取单个熊猫信息
     *
     * @param id 熊猫id
     * @return 单个熊猫信息
     */
    @Override
    public PandaDto getOneById(Integer id) {
        Panda panda = mapper.selectById(id);
        log.info("获取id为 {} 的大熊猫", id);
        return Converter.INSTANCE.toPandaDto(panda);
    }

    /**
     * 根据名字获取单个熊猫信息
     *
     * @param name 熊猫名
     * @return 单个熊猫信息
     */
    @Override
    public PandaDto getOneByName(String name) {
        log.info("获取名字为 {} 的大熊猫", name);
        Panda panda = mapper.selectOne(new LambdaQueryWrapper<Panda>().eq(Panda::getPandaName, name));
        return Converter.INSTANCE.toPandaDto(panda);
    }

    /**
     * 根据年龄获取所有熊猫信息
     *
     * @param age 年龄
     * @return 所有熊猫信息
     */
    @Override
    public List<PandaDto> getAllByAge(Integer age) {
        List<PandaDto> list = mapper
                .selectList(new LambdaQueryWrapper<Panda>().eq(Panda::getPandaAge, age))
                .stream()
                .map(Converter.INSTANCE::toPandaDto)
                .toList();
        log.info("获取年龄为 {} 的大熊猫，共 {} 只", age, list.size());
        return list;
    }

    /**
     * 根据性别获取所有熊猫信息
     *
     * @param sex 性别
     * @return 所有熊猫信息
     */
    @Override
    public List<PandaDto> getAllBySex(Character sex) {
        List<PandaDto> list = mapper.selectList(new LambdaQueryWrapper<Panda>().eq(Panda::getPandaSex, sex))
                                    .stream().map(Converter.INSTANCE::toPandaDto)
                                    .toList();
        log.info("获取性别为 {} 的大熊猫，共 {} 只", sex,list.size());
        return list;
    }

    /**
     * 获取熊猫总数
     *
     * @return 熊猫总数
     */
    @Override
    public Long getCount() {
        Long count = mapper.selectCount(null);
        log.info("获取熊猫总数为 {}",count);
        return count;
    }

    /**
     * 获取已删除的熊猫信息
     *
     * @return 已删除的熊猫信息
     */
    @Override
    public List<PandaDto> getDeletedWithPage(int pageNum, int pageSize) {
        Page<Panda> page = new Page<>(pageNum, pageSize);
        List<Panda> pandas = mapper.selectPage(page, new LambdaQueryWrapper<Panda>().eq(Panda::getDeleted, true))
                                   .getRecords();
        log.info("第 {} 页，{} 个已删除熊猫", pageNum, pandas.size());
        return pandas.stream().map(Converter.INSTANCE::toPandaDto).toList();
    }

    /**
     * 添加一个熊猫信息
     *
     * @param pandaDto 单个熊猫信息
     */
    @Override
    @Transactional
    public void addOne(PandaDto pandaDto) {
        Panda panda = Converter.INSTANCE.toPanda(pandaDto);
        mapper.insert(panda);
        log.info("添加一个熊猫，id 为 {}", panda.getPandaId());
    }

    /**
     * 添加多个熊猫信息
     *
     * @param pandaDtos 多个熊猫信息
     */
    @Transactional
    @Override
    public void addMany(List<PandaDto> pandaDtos) {
        if (pandaDtos.isEmpty()) {
            return;
        }
        List<Panda> pandas = pandaDtos.stream().map(Converter.INSTANCE::toPanda).toList();
        var inserted = mapper.insert(pandas, Constants.INSERT_BATCH_SIZE).size();
        log.info("批量添加 {} 只熊猫，成功 {} 条数据",pandaDtos.size(), inserted);
    }


    /**
     * 更新一个熊猫信息
     *
     * @param pandaDto 单个熊猫信息
     */
    @Transactional
    @Override
    public void updateOne(PandaDto pandaDto) {
        Panda panda = Converter.INSTANCE.toPanda(pandaDto);
        mapper.updateById(panda);
        log.info("更新熊猫 {}", pandaDto.getPandaName());
    }

    /**
     * 根据ID删除一个熊猫信息
     *
     * @param id 熊猫id
     */
    @Override
    public void deleteOneById(Integer id) {
        mapper.deleteById(id);
        log.info("删除熊猫 {}", id);
    }

    /**
     * 根据名字删除一个熊猫信息
     *
     * @param name 熊猫名
     */
    @Override
    public void deleteOneByName(String name) {
        mapper.delete(new LambdaQueryWrapper<Panda>()
                .eq(Panda::getPandaName, name));
        log.info("删除熊猫 {}", name);
    }

    /**
     * 根据ID删除多个熊猫信息
     *
     * @param ids 熊猫id
     */
    @Override
    public void deleteMany(List<Integer> ids) {
        int deleted = mapper.deleteByIds(ids);
        log.info("批量删除 {} 只熊猫，成功删除 {} 条数据", ids.size(), deleted);
    }
}
