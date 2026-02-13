package com.ordersystem.metadata;

import java.util.Map;

/**
 * Core interface for extensible metadata.
 * Implementations can define custom metadata attributes and behaviors.
 */
public interface Metadata {
    
    /**
     * Get the type identifier of this metadata
     * @return metadata type
     */
    String getType();
    
    /**
     * Get all attributes of this metadata
     * @return map of attribute key-value pairs
     */
    Map<String, Object> getAttributes();
    
    /**
     * Get a specific attribute value
     * @param key attribute key
     * @return attribute value or null if not found
     */
    Object getAttribute(String key);
    
    /**
     * Set a specific attribute value
     * @param key attribute key
     * @param value attribute value
     */
    void setAttribute(String key, Object value);
    
    /**
     * Validate the metadata
     * @return true if valid, false otherwise
     */
    boolean validate();
}
