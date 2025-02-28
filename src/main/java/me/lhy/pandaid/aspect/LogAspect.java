package me.lhy.pandaid.aspect;

import lombok.extern.slf4j.Slf4j;
import me.lhy.pandaid.annotation.LogOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Aspect
@Component
@Slf4j
public class LogAspect {

    private final Executor logExecutor = Executors.newFixedThreadPool(4);


    // 环绕通知记录完整操作日志
    @Around("@annotation(logOperation)")
    public Object logOperation(ProceedingJoinPoint joinPoint, LogOperation logOperation) throws Throwable {
        // 修改参数处理调用
        Object[] args = processSensitiveFields(
                joinPoint.getArgs(),
                logOperation.maskFields(),
                logOperation.maxParamSize()
        );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String operationDesc = logOperation.value();

        long startTime = System.currentTimeMillis();
        try {
            logExecutor.execute(() ->
                    log.info("[操作开始] {} | 用户:{} | 类:{} | 方法:{} | 参数:{}",
                            operationDesc, username, className, methodName, args)
            );

            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            logExecutor.execute(() -> {
                String resultLog = logOperation.trackResult() ? result.toString() : "***";
                log.info("[操作成功] {} | 耗时:{}ms | 结果:{}", operationDesc, executionTime, resultLog);
            });
            return result;
        } catch (Exception e) {
            // 记录异常日志
            logExecutor.execute(() ->
                    log.error("[操作异常] {} | 异常类型:{} | 异常信息:{}",
                            operationDesc, e.getClass().getSimpleName(), e.getMessage())
            );
            throw e;
        }
    }

    private Object[] processSensitiveFields(Object[] args, String[] maskFields, int maxSize) {
        return Arrays.stream(args).map(arg -> {
            if (arg == null) return null;

            // 集合类型特殊处理
            if (arg instanceof Collection) {
                return processCollection((Collection<?>) arg, maskFields, maxSize);
            }

            // 数组类型处理
            if (arg.getClass().isArray()) {
                return processArray(arg, maskFields, maxSize);
            }

            // 单个对象处理
            return processSingleObject(arg, maskFields);
        }).toArray();
    }

    private Object processArray(Object arg, String[] maskFields, int maxSize) {
        return null;
    }

    private Object processSingleObject(Object obj, String[] maskFields) {
        Arrays.stream(maskFields).forEach(field ->
                maskSensitiveData(obj, field)
        );
        return obj;
    }

    private Object processCollection(Collection<?> collection, String[] maskFields, int maxSize) {
        // 先进行脱敏处理
        List<Object> processed = collection.stream()
                                           .map(item -> processSingleObject(item, maskFields))
                                           .toList();

        // 参数数量限制
        if (maxSize > 0 && processed.size() > maxSize) {
            List<Object> subList = processed.subList(0, maxSize);
            return String.format("%s (共%d条，显示前%d条)", subList, processed.size(), maxSize);
        }
        return processed;
    }

    private Object maskSensitiveData(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value != null) {
                // 根据字段类型进行脱敏
                if (fieldName.toLowerCase().contains("phone")) {
                    String phone = value.toString();
                    field.set(obj, phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                } else if (fieldName.toLowerCase().contains("password")) {
                    field.set(obj, "******");
                }
            }
            return obj;
        } catch (Exception e) {
            return obj;
        }
    }
}
