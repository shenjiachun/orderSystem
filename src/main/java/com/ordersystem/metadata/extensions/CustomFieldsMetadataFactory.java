package com.ordersystem.metadata.extensions;

import com.ordersystem.metadata.Metadata;
import com.ordersystem.metadata.MetadataFactory;

/**
 * Factory for creating CustomFieldsMetadata instances
 */
public class CustomFieldsMetadataFactory implements MetadataFactory {
    
    @Override
    public String getType() {
        return CustomFieldsMetadata.TYPE;
    }
    
    @Override
    public Metadata create() {
        return new CustomFieldsMetadata();
    }
}
