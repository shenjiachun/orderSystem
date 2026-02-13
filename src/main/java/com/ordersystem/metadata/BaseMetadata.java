package com.ordersystem.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation of Metadata interface providing common functionality
 */
public abstract class BaseMetadata implements Metadata {
    
    protected final String type;
    protected final Map<String, Object> attributes;
    
    protected BaseMetadata(String type) {
        this.type = type;
        this.attributes = new HashMap<>();
    }
    
    @Override
    public String getType() {
        return type;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return new HashMap<>(attributes);
    }
    
    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
    
    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
    
    /**
     * Remove a specific attribute
     * @param key attribute key
     */
    protected void removeAttribute(String key) {
        attributes.remove(key);
    }
    
    @Override
    public boolean validate() {
        return true;
    }
}
