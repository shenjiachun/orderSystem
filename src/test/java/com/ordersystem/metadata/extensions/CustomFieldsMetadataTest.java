package com.ordersystem.metadata.extensions;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for CustomFieldsMetadata
 */
public class CustomFieldsMetadataTest {
    
    @Test
    public void testCreateCustomFieldsMetadata() {
        CustomFieldsMetadata metadata = new CustomFieldsMetadata();
        
        assertEquals("CUSTOM_FIELDS", metadata.getType());
    }
    
    @Test
    public void testAddAndGetField() {
        CustomFieldsMetadata metadata = new CustomFieldsMetadata();
        metadata.addField("customField1", "value1");
        
        assertEquals("value1", metadata.getField("customField1"));
    }
    
    @Test
    public void testAddMultipleFields() {
        CustomFieldsMetadata metadata = new CustomFieldsMetadata();
        metadata.addField("field1", "value1");
        metadata.addField("field2", 123);
        metadata.addField("field3", true);
        
        assertEquals("value1", metadata.getField("field1"));
        assertEquals(123, metadata.getField("field2"));
        assertEquals(true, metadata.getField("field3"));
    }
    
    @Test
    public void testRemoveField() {
        CustomFieldsMetadata metadata = new CustomFieldsMetadata();
        metadata.addField("field1", "value1");
        
        assertEquals("value1", metadata.getField("field1"));
        
        metadata.removeField("field1");
        
        assertNull(metadata.getField("field1"));
    }
    
    @Test
    public void testGetNonExistentField() {
        CustomFieldsMetadata metadata = new CustomFieldsMetadata();
        
        assertNull(metadata.getField("nonexistent"));
    }
}
