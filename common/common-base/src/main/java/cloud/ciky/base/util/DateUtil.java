package cloud.ciky.base.util;


import cloud.ciky.base.BaseQuery;
import cloud.ciky.base.constant.DateFormatConstants;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * 日期转LocalDate...(针对三方包使用Date转换用)
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
public final class DateUtil {

    private DateUtil() {
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate toLocalDate(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Long toTimestamp(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public static void setTimePeriod(Integer timePeriod, BaseQuery query){
        if(timePeriod != null){
            LocalDate now = LocalDate.now();
            String format = now.format(DateTimeFormatter.ofPattern(DateFormatConstants.FORMAT8));
            if(timePeriod==1){
                query.setStartDate(format);
                query.setEndDate(format);
            }else if (timePeriod == 2){
                query.setEndDate(format);
                query.setStartDate(now.minusWeeks(1).format(DateTimeFormatter.ofPattern(DateFormatConstants.FORMAT8)));
            }else if (timePeriod ==3){
                query.setStartDate(now.minusWeeks(2).format(DateTimeFormatter.ofPattern(DateFormatConstants.FORMAT8)));
                query.setEndDate(now.minusWeeks(1).format(DateTimeFormatter.ofPattern(DateFormatConstants.FORMAT8)));
            }
        }
    }
    public static String getTimeDifference(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);

        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        StringBuilder result = new StringBuilder();

        if (days > 0) {
            result.append(days).append("天");
        }
        if (hours > 0) {
            result.append(hours).append("时");
        }
        if (minutes > 0) {
            result.append(minutes).append("分");
        }
        if (seconds > 0 || result.length() == 0) {
            result.append(seconds).append("秒");
        }

        return result.toString();
    }
}
