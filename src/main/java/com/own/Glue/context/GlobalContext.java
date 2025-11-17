package com.own.Glue.context;

import java.util.HashMap;
import java.util.Map;

public class GlobalContext {
    // 单例实例，全局唯一
    private static GlobalContext instance;
    private final Map<String, Object> data = new HashMap<>();

    // 私有构造，防止外部实例化
    private GlobalContext() {}

    // 获取单例
    public static synchronized GlobalContext getInstance() {
        if (instance == null) {
            instance = new GlobalContext();
        }
        return instance;
    }

    // 存数据
    public void set(String key, Object value) {
        data.put(key, value);
    }

    // 取数据
    public Object get(String key) {
        return data.get(key);
    }

    // 清除数据（可选，如在所有场景执行前/后）
    public void clear() {
        data.clear();
    }
}
