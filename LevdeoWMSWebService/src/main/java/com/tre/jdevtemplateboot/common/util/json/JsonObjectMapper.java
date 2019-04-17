package com.tre.jdevtemplateboot.common.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @description: JackJSON 序列化封装
 * @author: JDev
 * @create: 2018-11-14 09:53
 **/
public class JsonObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    public JsonObjectMapper(){
        super();
        // format the null data to ""
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jg, SerializerProvider sp)
                    throws IOException, JsonProcessingException {
                jg.writeString("");
            }
        });
    }
}
