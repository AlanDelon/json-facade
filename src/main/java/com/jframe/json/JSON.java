package com.jframe.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * json 工具类
 * @author jiangjian45
 * Created at 2021/9/8 12:55
 */
public class JSON {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 默认时间格式
     */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    static {
        // LocalDateTime序列化依赖 jackson-datatype-jsr310 包
        MAPPER.findAndRegisterModules();
        // 为null的字段默认不序列化
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 设置未知属性不报错
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // MAPPER.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        MAPPER.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        // 是否允许没有引号的字段
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 浮点类型数据转为BigDecimal
        MAPPER.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        // 设置反序列化时大小写不敏感
        MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        MAPPER.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_PATTERN));
    }

    public <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return MAPPER.convertValue(fromValue, toValueTypeRef);
    }

    public static <T> T parseObject(String jsonString, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(jsonString, typeReference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject parseObject(String jsonString) {
        try {
            final Map map = MAPPER.readValue(jsonString, Map.class);
            return mapToJsonObject(map);
        } catch (Exception e) {
            throw new JsonConvertException(e);
        }

    }

    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        try {
            return MAPPER.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new JsonConvertException(e);
        }
    }

    public static JSONArray parseArray(String jsonString) {
        try {
            final List list = MAPPER.readValue(jsonString, List.class);
            return listConvertToJsonArray(list);
        } catch (Exception e) {
            throw new JsonConvertException(e);
        }
    }


    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        try {
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
            return MAPPER.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw new JsonConvertException(e);
        }
    }

    public static Object parse(String jsonString) {
        if (isJsonObj(jsonString)) {
            return parseObject(jsonString);
        }
        if (isJsonArray(jsonString)) {
            return parseArray(jsonString);
        }
        try {
            return MAPPER.readValue(jsonString, JsonNode.class);
        } catch (Exception e) {
            throw new JsonConvertException(e);
        }
    }

    public static String toJSONString(Object o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (Exception e) {
            throw new JsonConvertException(e);
        }
    }

    /**
     * 是否为JSON字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     */
    public static boolean isJson(String str) {
        return isJsonObj(str) || isJsonArray(str);
    }

    /**
     * 是否为JSONObject字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     */
    public static boolean isJsonObj(String str) {

        if (isBlank(str)) {
            return false;
        }
        return isWrap(str.trim(), '{', '}');
    }

    /**
     * 是否为JSONObject字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     */
    public static boolean isJsonArray(String str) {
        if (isBlank(str)) {
            return false;
        }
        return isWrap(str.trim(), '[', ']');
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    private static boolean isWrap(String str, char start, char end) {
        if (isBlank(str)) {
            return false;
        }
        return str.charAt(0) == start && str.charAt(str.length() - 1) == end;
    }

    private static JSONArray listConvertToJsonArray(List list) {
        List<Object> jsonObjects = new ArrayList<>(list.size());
        for (Object obj : list) {
            if (obj instanceof Map) {
                jsonObjects.add(mapToJsonObject((Map<String, Object>) obj));
            } else {
                jsonObjects.add(obj);
            }
        }
        return new JSONArray(jsonObjects);
    }

    /**
     * jackson parse出来的是map和list,所以把map和list转换为jsonObject和jsonArray
     *
     * @param map
     * @return
     */
    private static JSONObject mapToJsonObject(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            final Object value = entry.getValue();
            if (value instanceof Map) {
                jsonObject.put(entry.getKey(), mapToJsonObject((Map<String, Object>) value));
            } else if (value instanceof List) {
                final List listVal = (List) value;
                JSONArray objects = new JSONArray(listVal.size());
                for (Object o : listVal) {
                    if (o instanceof Map) {
                        objects.add(mapToJsonObject((Map<String, Object>) o));
                    } else if (o instanceof List) {
                        objects.add(listConvertToJsonArray((List) o));
                    } else {
                        objects.add(o);
                    }
                }
                jsonObject.put(entry.getKey(), objects);
            } else {
                jsonObject.put(entry.getKey(), value);
            }
        }
        return jsonObject;
    }
}
