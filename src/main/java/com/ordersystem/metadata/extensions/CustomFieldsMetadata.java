package com.ordersystem.metadata.extensions;

import com.ordersystem.metadata.BaseMetadata;

/**
 * Custom fields metadata extension.
 * Allows arbitrary key-value pairs for maximum extensibility.
 */
public class CustomFieldsMetadata extends BaseMetadata {
    
    public static final String TYPE = "CUSTOM_FIELDS";
    
    public CustomFieldsMetadata() {
        super(TYPE);
    }
    
    /**
     * Add a custom field
     * @param fieldName field name
     * @param value field value
     */
    public void addField(String fieldName, Object value) {
        setAttribute(fieldName, value);
    }
    
    /**
     * Get a custom field value
     * @param fieldName field name
     * @return field value or null if not found
     */
    public Object getField(String fieldName) {
        return getAttribute(fieldName);
    }
    
    /**
     * Remove a custom field
     * @param fieldName field name
     */
    public void removeField(String fieldName) {
        removeAttribute(fieldName);
    }
}
