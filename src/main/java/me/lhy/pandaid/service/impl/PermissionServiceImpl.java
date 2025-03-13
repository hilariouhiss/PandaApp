package me.lhy.pandaid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.lhy.pandaid.annotation.LogOperation;
import me.lhy.pandaid.domain.po.Permission;
import me.lhy.pandaid.mapper.PermissionMapper;
import me.lhy.pandaid.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper mapper;

    public PermissionServiceImpl(PermissionMapper mapper) {this.mapper = mapper;}


    /**
     * 分页查询所有权限
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 对应页的权限
     */
    @LogOperation("分页查询所有权限")
    @Override
    public List<Permission> getAllWithPage(Integer pageNum, Integer pageSize) {
        Page<Permission> page = new Page<>(pageNum, pageSize);
        return mapper.selectPage(page, null)
                     .getRecords();
    }

    /**
     * 根据ID查询权限
     *
     * @param id 权限主键
     * @return 单个权限
     */
    @Override
    public Permission getOneById(Long id) {
        return mapper.selectById(id);
    }

    /**
     * 根据URL查询权限
     *
     * @param url 权限URL
     * @return 单个权限
     */
    @Override
    public Permission getOneByURL(String url) {
        var wrapper = new LambdaQueryWrapper<Permission>()
                .eq(Permission::getUrl, url);
        return mapper.selectOne(wrapper);
    }

    /**
     * 根据name查询权限
     *
     * @param name 权限名称
     * @return 单个权限
     */
    @Override
    public Permission getOneByName(String name) {
        var wrapper = new LambdaQueryWrapper<Permission>()
                .eq(Permission::getName, name);
        return mapper.selectOne(wrapper);
    }

    /**
     * 查询权限总数
     *
     * @return 权限总数
     */
    @Override
    public Long getCount() {
        return mapper.selectCount(null);
    }

    /**
     * 分页获取逻辑删除的权限
     *
     * @param pageNum  页码
     * @param pageSize 页容
     * @return 对应页权限列表
     */
    @Override
    public List<Permission> getDeletedWithPage(int pageNum, int pageSize) {
        var page = new Page<Permission>(pageNum, pageSize);
        return mapper.selectPage(page, null).getRecords();

    }

    /**
     * 添加新的权限
     * @param permission 权限
     */
    @LogOperation("添加新的权限")
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void addOne(Permission permission) {
        mapper.insert(permission);
    }

    /**
     * 批量添加新的权限
     * @param permissions 权限列表
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void addMany(List<Permission> permissions) {
        mapper.insert(permissions);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateOne(Permission permission) {
        if (permission.getId() == null) {
            // todo 自定义异常类
            throw new RuntimeException("id 不可为空");
        }
        mapper.updateById(permission);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void deleteOneById(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public void deleteOneByName(String name) {
        var wrapper = new LambdaQueryWrapper<Permission>()
                .eq(Permission::getName, name);
        mapper.deleteById(wrapper);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        mapper.deleteByIds(ids);
    }

    @Override
    public void physicalDelete() {
        var wrapper = new LambdaQueryWrapper<Permission>()
                .eq(Permission::getDeleted, true);
        mapper.delete(wrapper);
    }
}
