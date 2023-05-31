package co.lemnisk.transform.analyzepost.builder.v3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public abstract class AbstractV3DataBuilder<T> {

    protected String rawData;
    protected T data;
    T targetClass;

    public T setRawData(String rawData) {
        this.rawData = rawData;
        return (T) this;
    }

    protected void constructData() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        JavaType type = objectMapper.getTypeFactory().
                constructCollectionType(
                        ArrayList.class,
                        targetClass.getClass());
        this.data = objectMapper.readValue(rawData, type);
    }
}
