package Glue.context;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    public ScenarioContext() {}


    private final Map<String, Object> context = new HashMap<>();

    // 存储数据
    public void setContext(ContextKey key, Object value) {
        context.put(key.toString(), value);
    }

    // 获取数据
    @SuppressWarnings("unchecked")
    public <T> T getContext(ContextKey key) {
        return (T) context.get(key.toString());
    }

    // 检查是否包含特定数据
    public boolean containsKey(ContextKey key) {
        return context.containsKey(key.toString());
    }

    // 清理上下文
    public void clear() {
        context.clear();
    }

    // 使用枚举定义上下文键，提高类型安全性
    public enum ContextKey {
        Agent,
        USER_TOKEN,
        CURRENT_PRODUCT,
        CART_ITEMS
    }



}
