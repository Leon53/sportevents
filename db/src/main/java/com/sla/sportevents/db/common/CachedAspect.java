package com.sla.sportevents.db.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
@EnableAspectJAutoProxy
public class CachedAspect {

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;


    @Value("${sportevents.cache.ttl.minutes}")
    //@Value("#{new Long('${sportevents.cache.ttl.minutes}')}")
    Long cacheTTLinMinutes;

    public CachedAspect(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(com.sla.sportevents.db.common.Cached)")
    public Object aroundCachedMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long cacheTTL = cacheTTLinMinutes * 60;

        String methodName = joinPoint.getSignature().getName();
        String cacheKey = getCacheKey(joinPoint);
        if (!ObjectUtils.isEmpty(cacheKey)) {

            if (isEvict(joinPoint)) {
                remove(cacheKey);
                return joinPoint.proceed();
            }

            String cachedValue = (String)this.read(cacheKey);

            if (!ObjectUtils.isEmpty(cachedValue)) {
                write(cacheKey, cachedValue, cacheTTL);
                return this.parseCachedValue(joinPoint, cachedValue);
            }

            log.debug("Cache miss for {}/{}", methodName, cacheKey);
            Object result = joinPoint.proceed();
            this.write(cacheKey, this.objectMapper.writeValueAsString(result), cacheTTL);
            return result;
        }

        return joinPoint.proceed();

    }

    private void write(String key, Object value, long redisCacheTTL) {
        log.debug("Writing to cache, key : {}, TTL : {}", key, redisCacheTTL);
        redisTemplate.opsForValue().set(key, (String) value, Duration.ofSeconds(redisCacheTTL));
    }

    public Object read(String key) {
        log.debug("Reading from cache, key : {}", key);
        return redisTemplate.opsForValue().get(key);
    }

    public void remove(String key) {
        log.debug("Removing from cache, key : {}", key);
        redisTemplate.delete(key);
    }

    private String getCacheKey(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String cacheKey = null;
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] methodArguments = joinPoint.getArgs();
        Cached cachedAnnotation = method.getAnnotation(Cached.class);
        if (cachedAnnotation != null) {
            cacheKey = cachedAnnotation.key();
        }
        if (ObjectUtils.isEmpty(cacheKey)) {
            cacheKey = methodName;
        }

        return "%s_%s".formatted(cacheKey, String.join("_", Arrays.stream(methodArguments)
                .map(this::getObjectKey).toList()));

    }

    private String getObjectKey(Object obj) {
        if (obj instanceof CacheId cacheId) {
            return String.valueOf(cacheId.getId());
        }
        return String.valueOf(obj);
    }

    private boolean isEvict(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Cached cachedAnnotation = method.getAnnotation(Cached.class);
        if (cachedAnnotation != null ) {
            return cachedAnnotation.evict();
        }

        return false;
    }

    private Object parseCachedValue(ProceedingJoinPoint joinPoint, String cachedValue) throws JsonProcessingException {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Class<?> returnType = methodSignature.getReturnType();

        return this.objectMapper.readValue(cachedValue, returnType);
    }

}
