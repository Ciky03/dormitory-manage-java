package cloud.ciky.business.service.impl;

import cloud.ciky.base.IBaseEnum;
import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.business.enums.DmTodoPriorityEnum;
import cloud.ciky.business.enums.DmTodoStatusEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DmTodoEnumTest {

    @Test
    void shouldResolveTodoPriorityAndStatusLabels() {
        assertEquals("\u9ad8", IBaseEnum.getLabelByValue(3, DmTodoPriorityEnum.class));
        assertEquals("\u5df2\u53d6\u6d88", IBaseEnum.getLabelByValue(3, DmTodoStatusEnum.class));
    }

    @Test
    void shouldExposeTodoLogModule() {
        assertEquals("\u5bbf\u820d\u5f85\u529e", LogModuleEnum.TODO.getModuleName());
    }
}
