package com.kafka.utils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonTypeResolver;

import java.io.IOException;

public class CustomJsonTypeResolver extends TypeIdResolverBase implements JsonTypeResolver {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final TypeIdResolver delegate;

    public CustomJsonTypeResolver(TypeIdResolver delegate) {
        this.delegate = delegate;
    }

    @Override
    public void init(JavaType baseType) {
        delegate.init(baseType);
    }

    @Override
    public String idFromValue(Object value) {
        return delegate.idFromValue(value);
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return delegate.idFromValueAndType(value, suggestedType);
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        return delegate.typeFromId(context, id);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return delegate.getMechanism();
    }

    @Override
    public JavaType resolveType(String s, byte[] bytes, Headers headers) {
        try {
            Class<?> clazz = Class.forName(s);
            return OBJECT_MAPPER.getTypeFactory().constructType(clazz);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot find class for id: " + s, e);
        }
    }
}
