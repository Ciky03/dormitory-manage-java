package cloud.ciky.system.service;

import cloud.ciky.base.model.Option;
import cloud.ciky.system.model.entity.SysRole;
import cloud.ciky.system.model.form.RoleForm;
import cloud.ciky.system.model.query.RolePageQuery;
import cloud.ciky.system.model.vo.RolePageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 15:56:01
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * <p>
     * 获取角色分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/1/4 17:35
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.system.model.vo.RolePageVO>
     */
    Page<RolePageVO> getRoleListPage(RolePageQuery query);

    /**
     * <p>
     * 获取角色下拉选项列表
     * </p>
     *
     * @author ciky
     * @since 2026/1/4 17:35
     * @return java.util.List<cloud.ciky.base.model.Option<java.lang.String>>
     */
    List<Option<String>> listRoleOptions();

    /**
     * <p>
     * 根据角色编码获取角色id
     * </p>
     *
     * @author ciky
     * @since 2026/3/14 11:19
     * @param roleCode 角色编码
     * @return java.lang.String
     */
    String getRoleIdByCode(String roleCode);

    /**
     * <p>
     * 获取角色表单数据
     * </p>
     *
     * @author ciky
     * @since 2026/1/4 17:36
     * @param roleId 角色id
     * @return cloud.ciky.system.model.form.RoleForm
     */
    RoleForm getRoleForm(String roleId);

    /**
     * <p>
     * 保存角色
     * </p>
     *
     * @author ciky
     * @since 2026/1/4 17:36
     * @param roleForm 表单对象
     * @return boolean
     */
    boolean saveRole(RoleForm roleForm);

    /**
     * <p>
     * 修改角色状态
     * </p>
     *
     * @author ciky
     * @since 2026/1/4 17:36
     * @param roleId 角色id
     * @param status 状态(1:启用;0:禁用)
     * @return boolean
     */
    boolean updateRoleStatus(String roleId, Boolean status);

    /**
     * <p>
     * 删除角色
     * </p>
     *
     * @author ciky
     * @since 2026/1/4 17:36
     * @param ids 角色id
     * @return boolean
     */
    boolean deleteRoles(String ids);

    /**
     * <p>
     * 获取角色最大排序
     * </p>
     *
     * @author ciky
     * @since 2026/1/4 18:06
     * @return java.lang.Long
     */
    Long getSort();

    /**
     * <p>
     * 分配菜单权限给角色
     * </p>
     *
     * @author ciky
     * @since 2026/1/6 14:57
     * @param roleId 角色id
     * @param menuIds 菜单id列表
     * @return boolean
     */
    boolean assignMenusToRole(String roleId, List<String> menuIds);
}
