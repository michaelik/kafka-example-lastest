package com.kafka.utils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

public class CustomTypeIdResolver extends TypeIdResolverBase {

    @Override
    public void init(JavaType baseType) {
        super.init(baseType);
    }

    @Override
    public String idFromValue(Object value) {
        return value.getClass().getName();
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) {
        try {
            return context.constructType(Class.forName(id));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find class for id: " + id, e);
        }
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CLASS;
    }
}