package com.ordersystem.metadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for metadata factories.
 * Provides extensibility by allowing registration of custom metadata types.
 */
public class MetadataRegistry {
    
    private static final MetadataRegistry INSTANCE = new MetadataRegistry();
    
    private final Map<String, MetadataFactory> factories;
    
    private MetadataRegistry() {
        this.factories = new ConcurrentHashMap<>();
    }
    
    public static MetadataRegistry getInstance() {
        return INSTANCE;
    }
    
    /**
     * Register a metadata factory
     * @param factory metadata factory to register
     */
    public void registerFactory(MetadataFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }
        factories.put(factory.getType(), factory);
    }
    
    /**
     * Unregister a metadata factory
     * @param type metadata type to unregister
     */
    public void unregisterFactory(String type) {
        factories.remove(type);
    }
    
    /**
     * Create metadata of the specified type
     * @param type metadata type
     * @return new metadata instance
     * @throws IllegalArgumentException if type is not registered
     */
    public Metadata createMetadata(String type) {
        MetadataFactory factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("No factory registered for type: " + type);
        }
        return factory.create();
    }
    
    /**
     * Check if a metadata type is registered
     * @param type metadata type
     * @return true if registered, false otherwise
     */
    public boolean isRegistered(String type) {
        return factories.containsKey(type);
    }
    
    /**
     * Clear all registered factories (useful for testing)
     */
    public void clear() {
        factories.clear();
    }
}
