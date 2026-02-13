package com.ordersystem.metadata.extensions;

import com.ordersystem.metadata.Metadata;
import com.ordersystem.metadata.MetadataFactory;

/**
 * Factory for creating PaymentMetadata instances
 */
public class PaymentMetadataFactory implements MetadataFactory {
    
    @Override
    public String getType() {
        return PaymentMetadata.TYPE;
    }
    
    @Override
    public Metadata create() {
        return new PaymentMetadata();
    }
}
