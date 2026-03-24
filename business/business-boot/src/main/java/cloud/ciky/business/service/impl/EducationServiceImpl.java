package cloud.ciky.business.service.impl;

import cloud.ciky.base.IBaseEnum;
import cloud.ciky.base.constant.SystemConstants;
import cloud.ciky.base.enums.DelflagEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.enums.EducationTypeEnum;
import cloud.ciky.business.model.entity.Education;
import cloud.ciky.business.mapper.EducationMapper;
import cloud.ciky.business.model.form.ClassTeacherForm;
import cloud.ciky.business.model.form.EducationForm;
import cloud.ciky.business.model.query.ClassPageQuery;
import cloud.ciky.business.model.query.EducationQuery;
import cloud.ciky.business.model.vo.ClassPageVO;
import cloud.ciky.business.model.vo.EducationVO;
import cloud.ciky.business.service.ClassStudentService;
import cloud.ciky.business.service.ClassTeacherService;
import cloud.ciky.business.service.EducationService;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
public class EducationServiceImpl extends ServiceImpl<EducationMapper, Education> implements EducationService {

    private final ClassStudentService classStudentService;
    private final ClassTeacherService classTeacherService;

    @Override
    public List<EducationVO> listEducationTree(EducationQuery query) {
        Boolean queryAll = query.getQueryAll();
        String studentId = query.getStudentId();
        String teacherId = query.getTeacherId();

        List<Education> educations = this.list(new LambdaQueryWrapper<Education>()
                .and(queryAll == null || !queryAll,
                        wrapper -> wrapper.eq(Education::getType, EducationTypeEnum.COLLEGE.getValue())
                                .or()
                                .eq(Education::getType, EducationTypeEnum.MAJOR.getValue()))
                .eq(Education::getDelflag, DelflagEnum.USABLE.getValue())
                .orderByAsc(Education::getGradeYear, Education::getName, Education::getCreateTime));

        // 查询已经被选择的班级
        String selectedClassId = null;
        if (CharSequenceUtil.isNotBlank(studentId)) {
            selectedClassId = classStudentService.getSelectedClassId(studentId);
        }
        if (CharSequenceUtil.isNotBlank(teacherId)) {
            selectedClassId = classTeacherService.getSelectedClassId(teacherId);
        }


        Set<String> parentIds = educations.stream()
                .map(Education::getParentId)
                .collect(Collectors.toSet());

        Set<String> educationIds = educations.stream()
                .map(Education::getId)
                .collect(Collectors.toSet());

        // 获取根节点id
        List<String> rootIds = parentIds.stream()
                .filter(id -> !educationIds.contains(id))
                .toList();

        String finalSelectedClassId = selectedClassId;
        return rootIds.stream()
                .flatMap(rootId -> buildEducationTree(rootId, educations, finalSelectedClassId).stream())
                .toList();
    }

    /**
     * <p>
     * 递归生成学院/专业列表
     * </p>
     *
     * @param parentId 父id
     * @param educationList 学院/专业列表
     * @param selectedClassId 选中的班级id
     * @author ciky
     * @since 2026/2/5 17:33
     */
    private List<EducationVO> buildEducationTree(String parentId, List<Education> educationList, String selectedClassId) {
        return CollUtil.emptyIfNull(educationList)
                .stream()
                .filter(education -> education.getParentId().equals(parentId))
                .map(entity -> {
                    Integer type = entity.getType();
                    String name = entity.getName();

                    EducationVO vo = new EducationVO();
                    vo.setId(entity.getId());
                    vo.setParentId(entity.getParentId());
                    vo.setTreePath(entity.getTreePath());
                    vo.setName(type.equals(EducationTypeEnum.CLASS.getValue()) ? entity.getGradeYear() + name : name);
                    vo.setType(type);
                    vo.setSelected(Objects.equals(entity.getId(), selectedClassId));
                    List<EducationVO> children = buildEducationTree(entity.getId(), educationList, selectedClassId);
                    vo.setChildren(children);
                    return vo;
                }).toList();
    }

    @Override
    public Page<ClassPageVO> listClass(ClassPageQuery query) {
        return this.baseMapper.listClass(new Page<>(query.getPageNum(), query.getPageSize()), query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveEducation(EducationForm form) {
        String id = form.getId();
        String parentId = form.getParentId();
        String name = form.getName();
        Integer eduType = form.getEduType();
        Integer gradeYear = form.getGradeYear();
        String userId = SecurityUtils.getUserId();

        // 新增班级, 年级判空
        if (eduType.equals(EducationTypeEnum.CLASS.getValue())) {
            if (ObjUtil.isNull(gradeYear)) {
                throw new BusinessException("年级不能为空!");
            }
        }

        long count = this.count(new LambdaQueryWrapper<Education>()
                .ne(CharSequenceUtil.isNotBlank(id), Education::getId, id)
                .and(wrapper -> wrapper.eq(Education::getParentId, parentId)
                        .eq(eduType.equals(EducationTypeEnum.CLASS.getValue()), Education::getGradeYear, gradeYear)
                        .eq(Education::getName, name)
                        .eq(Education::getDelflag, DelflagEnum.USABLE.getValue())));

        if (count > 0) {
            throw new BusinessException(IBaseEnum.getLabelByValue(eduType, EducationTypeEnum.class) + "名称已存在, 请修改后重试!");
        }

        Education entity = new Education();
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
        String treePath = generateEducationTreePath(parentId);
        entity.setTreePath(treePath);
        boolean saved = this.saveOrUpdate(entity);

        if(saved && CharSequenceUtil.isNotBlank(form.getHeadTeacherId())) {
            // 保存教师-班级关系
            ClassTeacherForm classTeacherForm = new ClassTeacherForm();
            classTeacherForm.setClassId(entity.getId());
            classTeacherForm.setTeacherId(form.getHeadTeacherId());
            saved = classTeacherService.saveClassTeacher(classTeacherForm);
        }
        return saved;
    }


    /**
     * 路径生成
     *
     * @param parentId 父ID
     * @return 父节点路径以英文逗号(, )分割，eg: 1,2,3
     */
    private String generateEducationTreePath(String parentId) {
        if (SystemConstants.ROOT_NODE_ID.equals(parentId)) {
            return parentId;
        } else {
            Education parent = this.getById(parentId);
            return parent != null ? parent.getTreePath() + "," + parent.getId() : null;
        }
    }


    @Override
    public EducationForm getEducationForm(String id) {
        return this.baseMapper.selectEducationForm(id);
    }

    @Override
    public boolean deleteEducation(String id) {
        Education entity = this.getById(id);
        if (entity == null) {
            return true;
        }

        // 学院/专业 判断级联
        Integer type = entity.getType();
        if (type.equals(EducationTypeEnum.COLLEGE.getValue()) || type.equals(EducationTypeEnum.MAJOR.getValue())) {
            long sonCount = this.count(new LambdaQueryWrapper<Education>()
                    .eq(Education::getParentId, id)
                    .eq(Education::getDelflag, DelflagEnum.USABLE.getValue()));
            if (sonCount > 0) {
                throw new BusinessException(type.equals(EducationTypeEnum.COLLEGE.getValue()) ? "该学院存在专业,无法删除!" : "该专业存在班级,无法删除!");
            }
        }

        // TODO 判断班级-学生绑定关系
        String userId = SecurityUtils.getUserId();
        return this.update(new LambdaUpdateWrapper<Education>().set(Education::getDelflag, DelflagEnum.REMOVED.getValue())
                .set(Education::getUpdateBy, userId)
                .eq(Education::getId, id));
    }

}
