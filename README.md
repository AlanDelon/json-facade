# json-facade
## 一、概述
Fastjson已经连续几次爆出高危漏洞，由于目前使用范围较广，每次几乎所有的JAVA后台系统均需升级。
为避免此安全问题，开发此SDK快速替换Fastjson，提供统一的JSON API，定义方式参照Fastjson，目前内核为Jackson.

## 二、默认特性
* 对象中未赋值的属性不进行序列化
* json字符串中存在类中未定义的属性时则忽略不报错
* json字符串反序列化时大小写不敏感
* 支持"单引号"、"双引号"、"无引号"三种json定义格式
* 允许以0开头的整数，例：0000100
* 允许json字符串中有回车换行符
* 浮点类型数据解析为BigDecimal


## 三、快速替换

### 3.1 依赖包处理
移除fastjson包依赖，并引用json-facade包依赖
```
<dependency>
  <groupId>com.jframe</groupId>
  <artifactId>json-facade</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 3.2 全局替换类引用
```
com.alibaba.fastjson.JSON 替换为 com.jframe.json.JSON

com.alibaba.fastjson.JSONArray 替换为 com.jframe.json.JSONArray

com.alibaba.fastjson.JSONObject 替换为 com.jframe.json.JSONObject

com.alibaba.fastjson.TypeReference 替换为 com.fasterxml.jackson.core.type.TypeReference
```
Idea 全局替换快捷键 Command + Shift + R

### 3.3 测试&监控
1. 替换完成后务必进行测试，尤其关注冷门方法的测试覆盖。
2. 验证过程和上线后，关注系统日志中是否出现JsonConvertException异常。


    