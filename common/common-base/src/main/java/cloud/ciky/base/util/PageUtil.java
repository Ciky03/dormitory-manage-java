package cloud.ciky.base.util;

import cloud.ciky.base.BaseQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 分页结果构建(针对三方包不兼容mybatis时用到)
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
public final class PageUtil {

    private PageUtil() {
    }

    /**
     * 通用分页结果构建
     * @param query 查询条件（需包含分页参数）
     * @param records 当前页数据
     * @param total 总记录数
     * @param <Q> 查询对象类型（继承 BaseQuery）
     * @param <T> VO 类型
     * @return Page<T>
     */
    public static <Q extends BaseQuery, T> Page<T> buildPage(Q query, List<T> records, long total) {
        Page<T> page = new Page<>();
        page.setRecords(records);
        page.setTotal(total);
        page.setSize(query.getPageSize());
        page.setCurrent(query.getPageNum());
        if(total == 0){
            page.setPages(total);
        }else{
            page.setPages((total + query.getPageSize() - 1) / query.getPageSize());
        }
        return page;
    }

}
