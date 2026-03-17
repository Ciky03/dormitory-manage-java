package cloud.ciky.business.service.impl;

import cloud.ciky.base.IBaseEnum;
import cloud.ciky.base.constant.SystemConstants;
import cloud.ciky.base.enums.DelflagEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.enums.EduUnitTypeEnum;
import cloud.ciky.business.model.entity.ClassStudent;
import cloud.ciky.business.model.entity.ClassTeacher;
import cloud.ciky.business.model.entity.EduUnit;
import cloud.ciky.business.mapper.EduUnitMapper;
import cloud.ciky.business.model.form.EduUnitForm;
import cloud.ciky.business.model.query.ClassPageQuery;
import cloud.ciky.business.model.query.UnitQuery;
import cloud.ciky.business.model.vo.ClassPageVO;
import cloud.ciky.business.model.vo.EduUnitVO;
import cloud.ciky.business.service.ClassStudentService;
import cloud.ciky.business.service.ClassTeacherService;
import cloud.ciky.business.service.EduUnitService;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ibm.icu.text.UFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 学院/专业/班级表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-02-05 17:03:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EduUnitServiceImpl extends ServiceImpl<EduUnitMapper, EduUnit> implements EduUnitService {

    private final ClassStudentService classStudentService;
    private final ClassTeacherService classTeacherService;

    @Override
    public List<EduUnitVO> listUnitTree(UnitQuery query) {
        Boolean queryAll = query.getQueryAll();
        String studentId = query.getStudentId();
        String teacherId = query.getTeacherId();

        List<EduUnit> units = this.list(new LambdaQueryWrapper<EduUnit>()
                .and(queryAll == null || !queryAll,
                        wrapper -> wrapper.eq(EduUnit::getType, EduUnitTypeEnum.COLLEGE.getValue())
                                .or()
                                .eq(EduUnit::getType, EduUnitTypeEnum.MAJOR.getValue()))
                .eq(EduUnit::getDelflag, DelflagEnum.USABLE.getValue())
                .orderByAsc(EduUnit::getGradeYear, EduUnit::getName, EduUnit::getCreateTime));

        // 查询已经被选择的班级
        String selectedClassId = null;
        if (CharSequenceUtil.isNotBlank(studentId)) {
            selectedClassId = classStudentService.getSelectedClassId(studentId);
        }
        if (CharSequenceUtil.isNotBlank(teacherId)) {
            selectedClassId = classTeacherService.getSelectedClassId(teacherId);
        }


        Set<String> parentIds = units.stream()
                .map(EduUnit::getParentId)
                .collect(Collectors.toSet());

        Set<String> unitIds = units.stream()
                .map(EduUnit::getId)
                .collect(Collectors.toSet());

        // 获取根节点id
        List<String> rootIds = parentIds.stream()
                .filter(id -> !unitIds.contains(id))
                .toList();

        String finalSelectedClassId = selectedClassId;
        return rootIds.stream()
                .flatMap(rootId -> buildUnitTree(rootId, units, finalSelectedClassId).stream())
                .toList();
    }

    /**
     * <p>
     * 递归生成学院/专业列表
     * </p>
     *
     * @param parentId 父id
     * @param unitList 学院/专业列表
     * @param selectedClassId 选中的班级id
     * @author ciky
     * @since 2026/2/5 17:33
     */
    private List<EduUnitVO> buildUnitTree(String parentId, List<EduUnit> unitList, String selectedClassId) {
        return CollUtil.emptyIfNull(unitList)
                .stream()
                .filter(unit -> unit.getParentId().equals(parentId))
                .map(entity -> {
                    Integer type = entity.getType();
                    String name = entity.getName();

                    EduUnitVO vo = new EduUnitVO();
                    vo.setId(entity.getId());
                    vo.setParentId(entity.getParentId());
                    vo.setTreePath(entity.getTreePath());
                    vo.setName(type.equals(EduUnitTypeEnum.CLASS.getValue()) ? entity.getGradeYear() + name : name);
                    vo.setType(type);
                    vo.setSelected(Objects.equals(entity.getId(), selectedClassId));
                    List<EduUnitVO> children = buildUnitTree(entity.getId(), unitList, selectedClassId);
                    vo.setChildren(children);
                    return vo;
                }).toList();
    }

    @Override
    public Page<ClassPageVO> listClass(ClassPageQuery query) {
        return this.baseMapper.listClass(new Page<>(query.getPageNum(), query.getPageSize()), query);
    }

    @Override
    public boolean saveUnit(EduUnitForm form) {
        String id = form.getId();
        String parentId = form.getParentId();
        String name = form.getName();
        Integer eduType = form.getEduType();
        Integer gradeYear = form.getGradeYear();
        String userId = SecurityUtils.getUserId();

        // 新增班级, 年份/班主任判空
        if (eduType.equals(EduUnitTypeEnum.CLASS.getValue())) {
            if (ObjUtil.isNull(gradeYear) || CharSequenceUtil.isBlank(form.getHeadTeacherId())) {
                throw new BusinessException("年份/班主任不能为空!");
            }
        }

        long count = this.count(new LambdaQueryWrapper<EduUnit>()
                .ne(CharSequenceUtil.isNotBlank(id), EduUnit::getId, id)
                .and(wrapper -> wrapper.eq(EduUnit::getParentId, parentId)
                        .eq(eduType.equals(EduUnitTypeEnum.CLASS.getValue()), EduUnit::getGradeYear, gradeYear)
                        .eq(EduUnit::getName, name)
                        .eq(EduUnit::getDelflag, DelflagEnum.USABLE.getValue())));

        if (count > 0) {
            throw new BusinessException(IBaseEnum.getLabelByValue(eduType, EduUnitTypeEnum.class) + "名称已存在, 请修改后重试!");
        }

        EduUnit entity = new EduUnit();
        if (CharSequenceUtil.isBlank(id)) {
            entity.setCreateBy(userId);
        } else {
            entity.setId(id);
            entity.setUpdateBy(userId);
        }
        entity.setParentId(parentId);
        entity.setType(eduType);
        entity.setName(name);
        entity.setGradeYear(gradeYear);
        String treePath = generateUnitTreePath(parentId);
        entity.setTreePath(treePath);

        // TODO 保存教师-班级关系

        return this.saveOrUpdate(entity);
    }


    /**
     * 路径生成
     *
     * @param parentId 父ID
     * @return 父节点路径以英文逗号(, )分割，eg: 1,2,3
     */
    private String generateUnitTreePath(String parentId) {
        if (SystemConstants.ROOT_NODE_ID.equals(parentId)) {
            return parentId;
        } else {
            EduUnit parent = this.getById(parentId);
            return parent != null ? parent.getTreePath() + "," + parent.getId() : null;
        }
    }


    @Override
    public EduUnitForm getUnitForm(String id) {
        // TODO 回显班主任信息
        return this.baseMapper.selectUnitForm(id);
    }

    @Override
    public boolean deleteUnit(String id) {
        EduUnit entity = this.getById(id);
        if (entity == null) {
            return true;
        }

        // 学院/专业 判断级联
        Integer type = entity.getType();
        if (type.equals(EduUnitTypeEnum.COLLEGE.getValue()) || type.equals(EduUnitTypeEnum.MAJOR.getValue())) {
            long sonCount = this.count(new LambdaQueryWrapper<EduUnit>()
                    .eq(EduUnit::getParentId, id)
                    .eq(EduUnit::getDelflag, DelflagEnum.USABLE.getValue()));
            if (sonCount > 0) {
                throw new BusinessException(type.equals(EduUnitTypeEnum.COLLEGE.getValue()) ? "该学院存在专业,无法删除!" : "该专业存在班级,无法删除!");
            }
        }

        // TODO 判断班级-学生绑定关系
        String userId = SecurityUtils.getUserId();
        return this.update(new LambdaUpdateWrapper<EduUnit>().set(EduUnit::getDelflag, DelflagEnum.REMOVED.getValue())
                .set(EduUnit::getUpdateBy, userId)
                .eq(EduUnit::getId, id));
    }

}
