package com.ordersystem.metadata.extensions;

import com.ordersystem.metadata.Metadata;
import com.ordersystem.metadata.MetadataFactory;

/**
 * Factory for creating ShippingMetadata instances
 */
public class ShippingMetadataFactory implements MetadataFactory {
    
    @Override
    public String getType() {
        return ShippingMetadata.TYPE;
    }
    
    @Override
    public Metadata create() {
        return new ShippingMetadata();
    }
}
