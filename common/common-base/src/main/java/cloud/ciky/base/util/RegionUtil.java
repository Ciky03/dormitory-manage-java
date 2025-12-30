package cloud.ciky.base.util;

import cloud.ciky.base.model.KeyValue;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 地区工具类
 * </p>
 *
 * @author ciky
 * @since 2025/12/9 12:12
 */
@Component
@Slf4j
public class RegionUtil {


    /**
     * 直辖市编号(北京,天津,上海,重庆)
     */
    private static final Set<String> MUNICIPALITIES = Set.of("110000", "120000", "310000", "500000");

    /**
     * 判断给定的省份是否是直辖市
     *
     * @param province 省份编号
     */
    public static boolean isMunicipality(String province) {
        return CharSequenceUtil.isNotBlank(province) && MUNICIPALITIES.contains(province);
    }

    public static String getArea(List<KeyValue> finalRegionList, String state, String city) {

        if (CollUtil.isEmpty(finalRegionList)) {
            return "未知";
        }
        StringBuilder area = new StringBuilder();
        if ("990100".equals(state)) {
            finalRegionList.stream().filter(f -> f.getKey().equals(state)).findFirst().ifPresent(
                    f -> area.append(f.getValue()));
        } else if (!RegionUtil.isMunicipality(state)) {
            finalRegionList.stream().filter(f -> f.getKey().equals(state)).findFirst().ifPresent(
                    f -> area.append(f.getValue()));
            finalRegionList.stream().filter(f -> f.getKey().equals(city)).findFirst().ifPresent(
                    f -> area.append(f.getValue()));
        } else {
            finalRegionList.stream().filter(f -> f.getKey().equals(city)).findFirst().ifPresent(
                    f -> area.append(f.getValue()));
        }
        return area.toString();
    }

    public static String getFullArea(List<KeyValue> finalRegionList, String state, String city, String district) {

        if (CollUtil.isEmpty(finalRegionList)) {
            return "未知";
        }
        StringBuilder area = new StringBuilder();
        if ("990100".equals(state)) {
            finalRegionList.stream().filter(f -> f.getKey().equals(state)).findFirst().ifPresent(
                    f -> area.append(f.getValue()));
        } else if (!RegionUtil.isMunicipality(state)) {
            finalRegionList.stream().filter(f -> f.getKey().equals(state)).findFirst().ifPresent(
                    f -> area.append(f.getValue()));
            finalRegionList.stream().filter(f -> f.getKey().equals(city)).findFirst().ifPresent(
                    f -> area.append(f.getValue()));
            finalRegionList.stream().filter(f -> f.getKey().equals(district)).findFirst().ifPresent(
                    f -> area.append(f.getValue()));
        } else {
            finalRegionList.stream().filter(f -> f.getKey().equals(city)).findFirst().ifPresent(
                    f -> area.append(f.getValue()));
            finalRegionList.stream().filter(f -> f.getKey().equals(district)).findFirst().ifPresent(
                    f -> area.append(f.getValue()));
        }
        return area.toString();
    }

}
