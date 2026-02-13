package com.ordersystem.metadata;

/**
 * Factory interface for creating metadata instances.
 * Implement this interface to register custom metadata types.
 */
public interface MetadataFactory {
    
    /**
     * Get the type identifier this factory creates
     * @return metadata type
     */
    String getType();
    
    /**
     * Create a new metadata instance
     * @return new metadata instance
     */
    Metadata create();
}
