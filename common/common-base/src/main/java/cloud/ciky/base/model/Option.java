package cloud.ciky.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 下拉选项对象
 * </p>
 *
 * @author ciky
 * @since 2025/12/9 12:03
 */
@Schema(description ="下拉选项对象")
@Data
@NoArgsConstructor
public class Option<T> {

    public Option(T value, String label) {
        this.value = value;
        this.label = label;
    }

    public Option(T value, String label, List<Option<T>> children) {
        this.value = value;
        this.label = label;
        this.children= children;
    }

    public Option(T value, String label, String tag) {
        this.value = value;
        this.label = label;
        this.tag= tag;
    }


    @Schema(description="选项的值")
    private T value;

    @Schema(description="选项的标签")
    private String label;

    @Schema(description="是否项目详情目录")
    private Boolean isProjectDetail = false;

    @Schema(description = "标签类型")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String tag;

    @Schema(description="子选项列表")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<Option<T>> children;

    public static Integer getValueByLabel(List<Option<String>> options, String targetLabel) {
        Optional<Option<String>> result = options.stream()
            .filter(option -> targetLabel.equals(option.getLabel())) // 匹配 label
            .findFirst(); // 找到第一个匹配项

        // 如果找到匹配项，返回其 value；否则返回 null 或默认值
        return result.map(o->Integer.parseInt(o.getValue())).orElse(null);
    }

    public static Integer getLabelByValue(List<Option<String>> options, String targetLabel) {
        Optional<Option<String>> result = options.stream()
            .filter(option -> targetLabel.equals(option.getLabel())) // 匹配 label
            .findFirst(); // 找到第一个匹配项

        // 如果找到匹配项，返回其 value；否则返回 null 或默认值
        return result.map(o->Integer.parseInt(o.getValue())).orElse(null);
    }

    public static String getLabelByValueStr(List<Option<String>> options, String targetValue) {
        String value = String.valueOf(targetValue);
        Optional<Option<String>> result = options.stream()
            .filter(option -> value.equals(option.getValue())) // 匹配 value
            .findFirst(); // 找到第一个匹配项

        // 如果找到匹配项，返回其 label；否则返回 defaultValue
        return result.map(Option::getLabel).orElse(null);
    }

    public static List<String> getValuesByLabelStr(List<Option<String>> options, String targetLabel) {
        return options.stream()
            .filter(option -> targetLabel.equals(option.getLabel()))
            .map(Option::getValue)
            .collect(Collectors.toList());   // 所有匹配值
    }

    public static String getLabelByValue(List<Option<String>> options, Integer targetValue) {
        String value = String.valueOf(targetValue);
        Optional<Option<String>> result = options.stream()
            .filter(option -> value.equals(option.getValue())) // 匹配 value
            .findFirst(); // 找到第一个匹配项

        // 如果找到匹配项，返回其 label；否则返回 defaultValue
        return result.map(Option::getLabel).orElse(null);
    }

}