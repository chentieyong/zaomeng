package com.kingpivot.base.config;

public class RedisKey {

    public static enum Key {
        SITE_KEY("site_"), MEMBER_KEY("member_"), MEMBERLOG_KEY("memberLog_");

        public String key;

        // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
        Key(String key) {
            this.key = key;
        }
    }
}
