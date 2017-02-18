package com.ericjin.opensource.util.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.io.IOException;

/**
 * A simple JSON utility class build on top fo Jackson Parser.
 *
 * Created by eric jin.
 */
public class SimpleJsonUtil {

    private static final ObjectMapper om;
    private static final String SERIALIZE_ERROR_MSG = "An error happened when converting java object to json ";
    private static final String DESERIALIZE_ERROR_MSG = "An error happened when converting json to java object ";

    static {
        om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    /**
     * Serialize a Java object to a JSON string.
     *
     * @param obj
     * @return JSON String
     * @throws JsonConversionException
     */
    public static String convertToJSON(Object obj) throws JsonConversionException {

        try {
            return om.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new JsonConversionException(SERIALIZE_ERROR_MSG + obj.getClass().getName(), ex);
        }
    }

    /**
     * Convert a JSON string to a Java object.
     *
     * @param <T>
     *            Class type
     * @param json
     *            a JSON string to be deserialized
     * @param type
     *            the Java object type
     * @return a Java object
     * @throws JsonConversionException
     */
    public static <T> T parseJSONString(String json, Class<T> type) throws JsonConversionException {

        try {
            return om.readValue(json, type);
        } catch (IOException ex) {
            throw new JsonConversionException(DESERIALIZE_ERROR_MSG + type.getName(), ex);
        }
    }

    /**
     * Convert a java object to JSON string only including certain properties.
     * This is sallow serialization. For nested object and collection, you also
     * need to include properties of child object.
     *
     * @param obj
     * @param strings
     * @return JSON String
     * @throws JsonConversionException
     */
    public static String convertToJsonInclude(Object obj, String... strings) throws JsonConversionException {

        try {
            ObjectMapper om = new ObjectMapper();
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            om.addMixInAnnotations(Object.class, BeanPropertyFilterMixIn.class);
            FilterProvider filters = new SimpleFilterProvider().addFilter(
                    "propertyFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(strings));
            return om.writer(filters).writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new JsonConversionException(SERIALIZE_ERROR_MSG + obj.getClass().getName(), ex);
        }
    }

    /**
     * Convert a Java object to JSON string excluding certain properties.
     *
     * @param obj
     * @param strings
     * @return JSON String
     * @throws JsonConversionException
     */
    public static String convertToJsonExclude(Object obj, String... strings) throws JsonConversionException {

        try {
            ObjectMapper om = new ObjectMapper();
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            om.addMixInAnnotations(Object.class, BeanPropertyFilterMixIn.class);
            FilterProvider filters = new SimpleFilterProvider().addFilter(
                    "propertyFilter",
                    SimpleBeanPropertyFilter.serializeAllExcept(strings));
            return om.writer(filters).writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new JsonConversionException(SERIALIZE_ERROR_MSG + obj.getClass().getName(), ex);
        }
    }

    /**
     * This method transforms JSON property names using Mix-In interface without
     * adding Jackson annotations to POJO. The Mix-In interface only provides
     * getter method and Jackson annotation for bean properties to be
     * serialized.
     *
     * @param obj
     * @param mixinClass
     * @return JSON String
     */
    public static String convertToJsonWithMixIn(Object obj, Class<?> mixinClass) {

        try {
            ObjectMapper om = new ObjectMapper();
            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            om.addMixInAnnotations(Object.class, mixinClass);

            return om.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new JsonConversionException(SERIALIZE_ERROR_MSG + obj.getClass().getName(), ex);
        }
    }
}
