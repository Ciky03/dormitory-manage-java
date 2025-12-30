package cloud.ciky.datascope.handler;


import cloud.ciky.base.model.DataScope;
import cloud.ciky.core.annotation.DataPermission;
import cloud.ciky.datascope.provider.IDataScopeProvider;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ParenthesedExpressionList;
import net.sf.jsqlparser.schema.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * <p>
 * 数据权限控制器
 * </p>
 *
 * @author ciky
 * @since 2025/12/9 15:05
 */
@Slf4j
@Component
public class MyDataPermissionHandler implements DataPermissionHandler {

    @Autowired
    @Lazy
    private IDataScopeProvider dataScopeProvider;

    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        Optional<String> opt = resolveDataPermission(mappedStatementId);
        if (opt.isEmpty()) {
            return where;
        }

        String mainAlias = opt.get();

        String userId = SecurityUtils.getUserId();
        if (CharSequenceUtil.isBlank(userId)) {
            // 未登录：全部禁止
            return and(where, denyAll());
        }

        DataScope ds = dataScopeProvider.getCurrentUserDataScope();

        // 1) 构造范围条件（building/room/student）
        Expression scopeExpr = buildScopeExpression(ds, mainAlias);

        // 2) 对“公告/社区”等表，允许 PUBLIC
        // 这里先用 mappedStatementId 简单判断：你可以改成注解方式（更优雅）
//        if (needIncludePublic(mappedStatementId)) {
//            scopeExpr = or(scopeExpr, publicExpr());
//        }

        return and(where, scopeExpr);
    }

    private Expression buildScopeExpression(DataScope ds, String mainAlias) {
        if (ds == null || ds.getType() == null) return denyAll();

        //TODO 通过原sql获取主表别名, 设置column为xxx.column
        return switch (ds.getType()) {
            case ALL -> null; // 不加条件
            case NONE -> denyAll();
            case BUILDING -> in(mainAlias, "building_id", ds.getBuildingIds());
            case ROOM -> in(mainAlias, "room_id", ds.getRoomIds());
            case STUDENT -> in(mainAlias, "student_id", ds.getStudentIds());
        };
    }

    private boolean needIncludePublic(String mappedStatementId) {
        // 例：NoticeMapper / CommunityPostMapper 的查询方法
        // 你也可以判断 SQL 表名，但 mappedStatementId 更常见
        String id = mappedStatementId.toLowerCase();
        return id.contains("notice") || id.contains("community");
    }


    private Expression publicExpr() {
        EqualsTo eq = new EqualsTo();
        eq.setLeftExpression(new Column("scope_type"));
        eq.setRightExpression(new StringValue("PUBLIC"));
        return eq;
    }

    private Expression denyAll() {
        // 1=0
        return new EqualsTo(new StringValue("1"), new StringValue("0"));
    }

    private Expression in(String mainAlias, String column, List<String> values) {
        if (values == null || values.isEmpty()) return denyAll();

        List<StringValue> items = values.stream().map(StringValue::new).collect(Collectors.toList());

        ParenthesedExpressionList<StringValue> list = new ParenthesedExpressionList<>(items);

        column = CharSequenceUtil.isNotBlank(mainAlias) ? mainAlias + "." + column : column;
        return new InExpression(new Column(column), list);
    }

    private Expression and(Expression a, Expression b) {
        if (a == null) return b;
        if (b == null) return a;
        return new AndExpression(a, b);
    }

    private Expression or(Expression a, Expression b) {
        if (a == null) return b;
        if (b == null) return a;
        return new OrExpression(a, b);
    }

    private Optional<String> resolveDataPermission(String mappedStatementId) {
        try {
            int lastDot = mappedStatementId.lastIndexOf(".");
            if (lastDot < 0) return Optional.empty();

            String className = mappedStatementId.substring(0, lastDot);
            String methodName = mappedStatementId.substring(lastDot + 1);

            Class<?> mapperClass = Class.forName(className);


            // 1. 方法上有注解
            for (Method method : mapperClass.getMethods()) {
                if (method.getName().equals(methodName)) {
                    DataPermission annotation = method.getAnnotation(DataPermission.class);
                    if (annotation != null) {
                        String alias = annotation.mainAlias();
                        return Optional.of(alias);
                    }
                }
            }

            // 2. 类上有注解
            DataPermission annotation = mapperClass.getAnnotation(DataPermission.class);
            if (annotation != null) {
                String alias = annotation.mainAlias();
                return Optional.of(alias);
            }

            return Optional.empty();
        } catch (ClassNotFoundException e) {
            log.info("无法解析类信息:mapperStatementId={},{}", mappedStatementId, e.getMessage());
            return Optional.empty();
        }
    }


}

