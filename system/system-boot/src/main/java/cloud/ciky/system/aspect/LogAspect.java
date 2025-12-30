package cloud.ciky.system.aspect;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.util.IPUtils;
import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.controller.LogController;
import cloud.ciky.system.model.form.LogForm;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * 日志切面
 * </p>
 *
 * @author ciky
 * @since 2025/12/15 15:49
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final LogController logService;
    private final HttpServletRequest request;

    /**
     * 切点
     */
    @Pointcut("@annotation(cloud.ciky.core.annotation.Log)")
    public void logPointcut() {
    }


    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @Around("logPointcut() && @annotation(logAnnotation)")
    public Object doAround(ProceedingJoinPoint joinPoint, cloud.ciky.core.annotation.Log logAnnotation) throws Throwable {
        TimeInterval timer = DateUtil.timer();
        Object result = null;
        Exception exception = null;

        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(" 日志切面发生错误");
            throw e;
        } finally {
            long executionTime = timer.interval(); // 执行时长
            this.saveLog(joinPoint, exception, result, logAnnotation, executionTime);
        }
        return result;
    }


    /**
     * 保存日志
     *
     * @param joinPoint     切点
     * @param e             异常
     * @param jsonResult    响应结果
     * @param logAnnotation 日志注解
     */
    private void saveLog(final JoinPoint joinPoint, final Exception e, Object jsonResult, cloud.ciky.core.annotation.Log logAnnotation, long executionTime) {
        String requestUri = request.getRequestURI();
        // 创建日志记录
        LogForm log = new LogForm();
        log.setExecutionTime(executionTime);
        if (logAnnotation == null && e != null) {
            log.setModule(LogModuleEnum.EXCEPTION);
            log.setContent("系统发生异常");
            this.setRequestParameters(joinPoint, log);
            log.setResponseContent(JSONUtil.toJsonStr(e.getStackTrace()));
        } else {
            log.setModule(logAnnotation.module());
            log.setContent(logAnnotation.value());
            // 请求参数
            if (logAnnotation.params()) {
                this.setRequestParameters(joinPoint, log);
            }
            // 响应结果
            if (logAnnotation.result() && jsonResult != null) {
                log.setResponseContent(JSONUtil.toJsonStr(jsonResult));
            }
        }
        log.setRequestUri(requestUri);
        String userId = SecurityUtils.getUserId();
        log.setCreateBy(userId);
        String ipAddr = IPUtils.getIpAddr(request);
        if (CharSequenceUtil.isNotBlank(ipAddr)) {
            log.setIp(ipAddr);
            if (!"127.0.0.1".equals(ipAddr) && !"0:0:0:0:0:0:0:1".equals(ipAddr)) {
                String region = IPUtils.getRegion(ipAddr);
                // 中国|0|四川省|成都市|电信 解析省和市
                if (CharSequenceUtil.isNotBlank(region)) {
                    String[] regionArray = region.split("\\|");
                    if (regionArray.length > 2) {
                        log.setProvince(regionArray[2]);
                        log.setCity(regionArray[3]);
                    }
                }
            }
        }

        // 获取浏览器和终端系统信息
        String userAgentString = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        // 系统信息
        //log.setOs(userAgent.getOs().getName());
        log.setOs(userAgent.getOperatingSystem().name());
        // 浏览器信息
        Browser browser = userAgent.getBrowser();
        log.setBrowser(browser.getName());
        Version version = browser.getVersion(userAgentString);
        if (browser.getVersion(userAgentString) != null) {
            log.setBrowserVersion(version.getVersion());
        }
        // 保存日志到数据库
        logService.saveLog(log);
    }

    /**
     * 设置请求参数到日志对象中
     *
     * @param joinPoint 切点
     * @param log       操作日志
     */
    private void setRequestParameters(JoinPoint joinPoint, LogForm log) {
        String requestMethod = request.getMethod();
        log.setRequestMethod(requestMethod);
        if (HttpMethod.GET.name().equalsIgnoreCase(requestMethod) || HttpMethod.PUT.name().equalsIgnoreCase(requestMethod) || HttpMethod.POST.name().equalsIgnoreCase(requestMethod)) {
            String params = convertArgumentsToString(joinPoint.getArgs());
            log.setRequestParams(CharSequenceUtil.sub(params, 0, 65535));
        } else {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                Map<?, ?> paramsMap = (Map<?, ?>) attributes.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                log.setRequestParams(CharSequenceUtil.sub(paramsMap.toString(), 0, 65535));
            } else {
                log.setRequestParams("");
            }
        }
    }

    /**
     * 将参数数组转换为字符串
     *
     * @param paramsArray 参数数组
     * @return 参数字符串
     */
    private String convertArgumentsToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object param : paramsArray) {
                if (!shouldFilterObject(param)) {
                    params.append(JSONUtil.toJsonStr(param)).append(" ");
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param obj 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    private boolean shouldFilterObject(Object obj) {
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            return MultipartFile.class.isAssignableFrom(clazz.getComponentType());
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection<?> collection = (Collection<?>) obj;
            return collection.stream().anyMatch(MultipartFile.class::isInstance);
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return map.values().stream().anyMatch(MultipartFile.class::isInstance);
        }
        return obj instanceof MultipartFile || obj instanceof HttpServletRequest || obj instanceof HttpServletResponse;
    }


    /**
     * 解析UserAgent
     *
     * @param userAgentString UserAgent字符串
     * @return UserAgent
     */
   /* public UserAgent resolveUserAgent(String userAgentString) {
        if (CharSequenceUtil.isBlank(userAgentString)) {
            return null;
        }
        try {
            // 给userAgentString MD5加密一次防止过长
            String userAgentStringMd5 = DigestUtil.md5Hex(userAgentString);
            //判断是否命中缓存
            UserAgent userAgent = Objects.requireNonNull(cacheManager.getCache("userAgent")).get(userAgentStringMd5, UserAgent.class);
            if (userAgent != null) {
                return userAgent;
            }
            userAgent = UserAgentUtil.parse(userAgentString);
            Objects.requireNonNull(cacheManager.getCache("userAgent")).put(userAgentStringMd5, userAgent);
            return userAgent;
        } catch (Exception e) {
            log.error("解析UserAgent失败", e);
            return null;
        }
    }*/

}
