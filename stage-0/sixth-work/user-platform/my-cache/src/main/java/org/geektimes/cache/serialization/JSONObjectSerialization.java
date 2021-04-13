package org.geektimes.cache.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JSONObjectSerialization implements Serialization<JSONObject> {

    @Override
    public JSONObject deserialize(byte[] target) {
        return JSON.parseObject(target, JSONObject.class);
    }

    @Override
    public byte[] serialize(JSONObject source) {
        return JSON.toJSONBytes(source);
    }

    @Override
    public void close() {

    }
}
