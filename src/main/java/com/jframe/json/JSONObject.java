package com.jframe.json;


import com.fasterxml.jackson.core.type.TypeReference;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


/**
 * JSON对象
 * @author jiangjian45
 * @date 2021/9/8 15:42
 */
public class JSONObject extends JSON implements Map<String, Object>, Cloneable, Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;
    /**
     * 默认容量
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * 数据容器
     */
    private final Map<String, Object> map;

    public JSONObject(){
        this(DEFAULT_INITIAL_CAPACITY, false);
    }

    public JSONObject(Map<String, Object> map){
        if (map == null) {
            throw new IllegalArgumentException("map is null.");
        }
        this.map = map;
    }

    public JSONObject(boolean ordered){
        this(DEFAULT_INITIAL_CAPACITY, ordered);
    }

    public JSONObject(int initialCapacity){
        this(initialCapacity, false);
    }

    public JSONObject(int initialCapacity, boolean ordered){
        if (ordered) {
            map = new LinkedHashMap<String, Object>(initialCapacity);
        } else {
            map = new HashMap<String, Object>(initialCapacity);
        }
    }

    public Map<String, Object> getInnerMap() {
        return this.map;
    }
    public JSONObject getJSONObject(String key) {
        Object value = map.get(key);

        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }

        if (value instanceof Map) {
            return new JSONObject((Map) value);
        }

        if (value instanceof String) {
            return JSON.parseObject((String) value);
        }

        return JSON.parseObject(JSON.toJSONString(value));
    }

    public JSONArray getJSONArray(String key) {
        Object value = map.get(key);

        if (value == null) {
            return new JSONArray();
        }

        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }

        if (value instanceof List) {
            return new JSONArray((List) value);
        }

        if (value instanceof String) {
            return JSON.parseArray((String) value);
        }

        return JSON.parseArray(JSON.toJSONString(value));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        Object obj = map.get(key);
        if (clazz == obj.getClass()) {
            return (T) obj;
        }
        if (clazz == String.class) {
            return JSON.parseObject((String) obj, clazz);
        }

        return JSON.parseObject(JSON.toJSONString(obj), clazz);
    }

    public <T> T getObject(String key, TypeReference<T> typeReference) {
        Object obj = map.get(key);
        if (typeReference == null) {
            return (T) obj;
        }
        return convertValue(obj, typeReference);
    }

    public Boolean getBoolean(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return TypeUtils.castToBoolean(value);
    }


    public boolean getBooleanValue(String key) {
        Object value = get(key);

        Boolean booleanVal = TypeUtils.castToBoolean(value);
        if (booleanVal == null) {
            return false;
        }

        return booleanVal.booleanValue();
    }

    public Byte getByte(String key) {
        Object value = get(key);

        return TypeUtils.castToByte(value);
    }

    public byte getByteValue(String key) {
        Object value = get(key);

        Byte byteVal = TypeUtils.castToByte(value);
        if (byteVal == null) {
            return 0;
        }

        return byteVal.byteValue();
    }

    public Short getShort(String key) {
        Object value = get(key);

        return TypeUtils.castToShort(value);
    }

    public short getShortValue(String key) {
        Object value = get(key);

        Short shortVal = TypeUtils.castToShort(value);
        if (shortVal == null) {
            return 0;
        }

        return shortVal.shortValue();
    }

    public Integer getInteger(String key) {
        Object value = get(key);

        return TypeUtils.castToInt(value);
    }

    public int getIntValue(String key) {
        Object value = get(key);

        Integer intVal = TypeUtils.castToInt(value);
        if (intVal == null) {
            return 0;
        }

        return intVal.intValue();
    }

    public Long getLong(String key) {
        Object value = get(key);

        return TypeUtils.castToLong(value);
    }

    public long getLongValue(String key) {
        Object value = get(key);

        Long longVal = TypeUtils.castToLong(value);
        if (longVal == null) {
            return 0L;
        }

        return longVal.longValue();
    }

    public Float getFloat(String key) {
        Object value = get(key);

        return TypeUtils.castToFloat(value);
    }

    public float getFloatValue(String key) {
        Object value = get(key);

        Float floatValue = TypeUtils.castToFloat(value);
        if (floatValue == null) {
            return 0F;
        }

        return floatValue.floatValue();
    }

    public Double getDouble(String key) {
        Object value = get(key);

        return TypeUtils.castToDouble(value);
    }

    public double getDoubleValue(String key) {
        Object value = get(key);

        Double doubleValue = TypeUtils.castToDouble(value);
        if (doubleValue == null) {
            return 0D;
        }

        return doubleValue.doubleValue();
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = get(key);

        return TypeUtils.castToBigDecimal(value);
    }

    public BigInteger getBigInteger(String key) {
        Object value = get(key);

        return TypeUtils.castToBigInteger(value);
    }

    public String getString(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public <T> T toJavaObject(Class<T> clazz) {
        if (clazz == Map.class || clazz == JSONObject.class || clazz == JSONArray.class) {
            return (T) this;
        }
        return parseObject(toJSONString(), clazz);
    }

    public String toJSONString() {
        return JSON.toJSONString(map);
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
