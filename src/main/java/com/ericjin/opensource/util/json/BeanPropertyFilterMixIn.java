package com.ericjin.opensource.util.json;

/**
 * A marker interface to use Jackson bean property filter without adding annotation to POJO.
 */
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("propertyFilter")
public interface BeanPropertyFilterMixIn {

}
