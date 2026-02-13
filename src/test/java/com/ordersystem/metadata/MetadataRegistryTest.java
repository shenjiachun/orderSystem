package com.ordersystem.metadata;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for MetadataRegistry
 */
public class MetadataRegistryTest {
    
    private MetadataRegistry registry;
    
    @Before
    public void setUp() {
        registry = MetadataRegistry.getInstance();
        registry.clear();
    }
    
    @Test
    public void testRegisterFactory() {
        MetadataFactory factory = new TestMetadataFactory();
        registry.registerFactory(factory);
        
        assertTrue(registry.isRegistered("TEST"));
    }
    
    @Test
    public void testCreateMetadata() {
        MetadataFactory factory = new TestMetadataFactory();
        registry.registerFactory(factory);
        
        Metadata metadata = registry.createMetadata("TEST");
        assertNotNull(metadata);
        assertEquals("TEST", metadata.getType());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMetadataUnregistered() {
        registry.createMetadata("UNKNOWN");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNullFactory() {
        registry.registerFactory(null);
    }
    
    @Test
    public void testUnregisterFactory() {
        MetadataFactory factory = new TestMetadataFactory();
        registry.registerFactory(factory);
        assertTrue(registry.isRegistered("TEST"));
        
        registry.unregisterFactory("TEST");
        assertFalse(registry.isRegistered("TEST"));
    }
    
    // Test helper classes
    
    private static class TestMetadata extends BaseMetadata {
        TestMetadata() {
            super("TEST");
        }
    }
    
    private static class TestMetadataFactory implements MetadataFactory {
        @Override
        public String getType() {
            return "TEST";
        }
        
        @Override
        public Metadata create() {
            return new TestMetadata();
        }
    }
}
