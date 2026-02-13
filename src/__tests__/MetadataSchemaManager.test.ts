/**
 * Tests for MetadataSchemaManager
 */

import { MetadataSchemaManager } from '../services/MetadataSchemaManager';
import { FieldType } from '../types/metadata';

describe('MetadataSchemaManager', () => {
  let manager: MetadataSchemaManager;

  beforeEach(() => {
    manager = new MetadataSchemaManager();
  });

  describe('registerSchema', () => {
    it('should register a new schema', () => {
      const schema = manager.registerSchema({
        id: 'test-schema',
        name: 'Test Schema',
        version: '1.0.0',
        fields: []
      });

      expect(schema.id).toBe('test-schema');
      expect(schema.name).toBe('Test Schema');
      expect(schema.createdAt).toBeInstanceOf(Date);
      expect(schema.updatedAt).toBeInstanceOf(Date);
    });

    it('should register schema with fields', () => {
      const schema = manager.registerSchema({
        id: 'test-schema',
        name: 'Test Schema',
        version: '1.0.0',
        fields: [
          {
            name: 'field1',
            type: FieldType.STRING,
            label: 'Field 1'
          }
        ]
      });

      expect(schema.fields).toHaveLength(1);
      expect(schema.fields[0].name).toBe('field1');
    });
  });

  describe('getSchema', () => {
    it('should get a registered schema', () => {
      manager.registerSchema({
        id: 'test-schema',
        name: 'Test Schema',
        version: '1.0.0',
        fields: []
      });

      const schema = manager.getSchema('test-schema');
      expect(schema).toBeDefined();
      expect(schema?.id).toBe('test-schema');
    });

    it('should return undefined for non-existent schema', () => {
      const schema = manager.getSchema('non-existent');
      expect(schema).toBeUndefined();
    });
  });

  describe('getAllSchemas', () => {
    it('should return all registered schemas', () => {
      manager.registerSchema({
        id: 'schema1',
        name: 'Schema 1',
        version: '1.0.0',
        fields: []
      });

      manager.registerSchema({
        id: 'schema2',
        name: 'Schema 2',
        version: '1.0.0',
        fields: []
      });

      const schemas = manager.getAllSchemas();
      expect(schemas).toHaveLength(2);
    });
  });

  describe('updateSchema', () => {
    it('should update an existing schema', () => {
      manager.registerSchema({
        id: 'test-schema',
        name: 'Test Schema',
        version: '1.0.0',
        fields: []
      });

      const updated = manager.updateSchema('test-schema', {
        name: 'Updated Schema',
        version: '2.0.0'
      });

      expect(updated?.name).toBe('Updated Schema');
      expect(updated?.version).toBe('2.0.0');
    });

    it('should return undefined for non-existent schema', () => {
      const updated = manager.updateSchema('non-existent', { name: 'New Name' });
      expect(updated).toBeUndefined();
    });
  });

  describe('deleteSchema', () => {
    it('should delete an existing schema', () => {
      manager.registerSchema({
        id: 'test-schema',
        name: 'Test Schema',
        version: '1.0.0',
        fields: []
      });

      const deleted = manager.deleteSchema('test-schema');
      expect(deleted).toBe(true);
      expect(manager.getSchema('test-schema')).toBeUndefined();
    });

    it('should return false for non-existent schema', () => {
      const deleted = manager.deleteSchema('non-existent');
      expect(deleted).toBe(false);
    });
  });

  describe('Field operations', () => {
    beforeEach(() => {
      manager.registerSchema({
        id: 'test-schema',
        name: 'Test Schema',
        version: '1.0.0',
        fields: []
      });
    });

    it('should add a field definition', () => {
      const added = manager.addFieldDefinition('test-schema', {
        name: 'newField',
        type: FieldType.STRING,
        label: 'New Field'
      });

      expect(added).toBe(true);
      const fields = manager.getFieldDefinitions('test-schema');
      expect(fields).toHaveLength(1);
      expect(fields[0].name).toBe('newField');
    });

    it('should not add duplicate field', () => {
      manager.addFieldDefinition('test-schema', {
        name: 'field1',
        type: FieldType.STRING,
        label: 'Field 1'
      });

      const added = manager.addFieldDefinition('test-schema', {
        name: 'field1',
        type: FieldType.NUMBER,
        label: 'Field 1 Duplicate'
      });

      expect(added).toBe(false);
      const fields = manager.getFieldDefinitions('test-schema');
      expect(fields).toHaveLength(1);
    });

    it('should update a field definition', () => {
      manager.addFieldDefinition('test-schema', {
        name: 'field1',
        type: FieldType.STRING,
        label: 'Field 1'
      });

      const updated = manager.updateFieldDefinition('test-schema', 'field1', {
        label: 'Updated Field 1'
      });

      expect(updated).toBe(true);
      const fields = manager.getFieldDefinitions('test-schema');
      expect(fields[0].label).toBe('Updated Field 1');
      expect(fields[0].name).toBe('field1'); // Name should not change
    });

    it('should remove a field definition', () => {
      manager.addFieldDefinition('test-schema', {
        name: 'field1',
        type: FieldType.STRING,
        label: 'Field 1'
      });

      const removed = manager.removeFieldDefinition('test-schema', 'field1');
      expect(removed).toBe(true);
      
      const fields = manager.getFieldDefinitions('test-schema');
      expect(fields).toHaveLength(0);
    });

    it('should return false when removing non-existent field', () => {
      const removed = manager.removeFieldDefinition('test-schema', 'non-existent');
      expect(removed).toBe(false);
    });
  });
});
