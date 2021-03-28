package xyz.erupt.core.config;


import com.google.gson.*;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import xyz.erupt.core.util.DateUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;


/**
 * @author YuePeng
 * date 10/31/18.
 */
@Configuration
public class MvcConfig {

    @Bean
    public HttpMessageConverters customConverters() {
        Gson gson = new GsonBuilder().setDateFormat(DateUtil.DATE_TIME)
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context)
                        -> new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(DateUtil.DATE_TIME))))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context)
                        -> new JsonPrimitive(src.format(DateTimeFormatter.ofPattern(DateUtil.DATE))))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext)
                        -> LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern(DateUtil.DATE_TIME)))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext)
                        -> LocalDate.parse(json.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ofPattern(DateUtil.DATE)))
                .setLongSerializationPolicy(LongSerializationPolicy.STRING).setExclusionStrategies(new GsonExclusionStrategies())
                .serializeNulls().create();
        Collection<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new GsonHttpMessageConverter(gson) {
            // 兼容magic-api，由于gson不走setter，所以配置magic-api包下的类不走gson转换，走默认的jackson。
            @Override
            protected boolean supports(Class<?> clazz) {
                return !clazz.getName().startsWith("org.ssssssss.") && super.supports(clazz);
            }
        });
        return new HttpMessageConverters(true, messageConverters);
    }

}
