/**
 * Tests for MetadataValidator
 */

import { MetadataValidator } from '../validators/MetadataValidator';
import { FieldType, FieldDefinition } from '../types/metadata';

describe('MetadataValidator', () => {
  let validator: MetadataValidator;

  beforeEach(() => {
    validator = new MetadataValidator();
  });

  describe('Type validation', () => {
    it('should validate string type', () => {
      const fields: FieldDefinition[] = [{
        name: 'name',
        type: FieldType.STRING,
        label: 'Name'
      }];

      const result = validator.validate({ name: 'test' }, fields);
      expect(result.valid).toBe(true);
      expect(result.errors).toHaveLength(0);
    });

    it('should reject invalid string type', () => {
      const fields: FieldDefinition[] = [{
        name: 'name',
        type: FieldType.STRING,
        label: 'Name'
      }];

      const result = validator.validate({ name: 123 }, fields);
      expect(result.valid).toBe(false);
      expect(result.errors).toHaveLength(1);
      expect(result.errors[0].field).toBe('name');
    });

    it('should validate number type', () => {
      const fields: FieldDefinition[] = [{
        name: 'age',
        type: FieldType.NUMBER,
        label: 'Age'
      }];

      const result = validator.validate({ age: 25 }, fields);
      expect(result.valid).toBe(true);
    });

    it('should validate boolean type', () => {
      const fields: FieldDefinition[] = [{
        name: 'active',
        type: FieldType.BOOLEAN,
        label: 'Active'
      }];

      const result = validator.validate({ active: true }, fields);
      expect(result.valid).toBe(true);
    });

    it('should validate array type', () => {
      const fields: FieldDefinition[] = [{
        name: 'tags',
        type: FieldType.ARRAY,
        label: 'Tags'
      }];

      const result = validator.validate({ tags: ['a', 'b'] }, fields);
      expect(result.valid).toBe(true);
    });

    it('should validate object type', () => {
      const fields: FieldDefinition[] = [{
        name: 'config',
        type: FieldType.OBJECT,
        label: 'Config'
      }];

      const result = validator.validate({ config: { key: 'value' } }, fields);
      expect(result.valid).toBe(true);
    });
  });

  describe('Required validation', () => {
    it('should pass when required field is provided', () => {
      const fields: FieldDefinition[] = [{
        name: 'email',
        type: FieldType.STRING,
        label: 'Email',
        validation: { required: true }
      }];

      const result = validator.validate({ email: 'test@example.com' }, fields);
      expect(result.valid).toBe(true);
    });

    it('should fail when required field is missing', () => {
      const fields: FieldDefinition[] = [{
        name: 'email',
        type: FieldType.STRING,
        label: 'Email',
        validation: { required: true }
      }];

      const result = validator.validate({}, fields);
      expect(result.valid).toBe(false);
      expect(result.errors[0].message).toContain('required');
    });
  });

  describe('Min/Max validation', () => {
    it('should validate min for numbers', () => {
      const fields: FieldDefinition[] = [{
        name: 'age',
        type: FieldType.NUMBER,
        label: 'Age',
        validation: { min: 18 }
      }];

      expect(validator.validate({ age: 20 }, fields).valid).toBe(true);
      expect(validator.validate({ age: 15 }, fields).valid).toBe(false);
    });

    it('should validate max for numbers', () => {
      const fields: FieldDefinition[] = [{
        name: 'age',
        type: FieldType.NUMBER,
        label: 'Age',
        validation: { max: 100 }
      }];

      expect(validator.validate({ age: 50 }, fields).valid).toBe(true);
      expect(validator.validate({ age: 150 }, fields).valid).toBe(false);
    });

    it('should validate min for strings', () => {
      const fields: FieldDefinition[] = [{
        name: 'name',
        type: FieldType.STRING,
        label: 'Name',
        validation: { min: 3 }
      }];

      expect(validator.validate({ name: 'John' }, fields).valid).toBe(true);
      expect(validator.validate({ name: 'Jo' }, fields).valid).toBe(false);
    });

    it('should validate max for strings', () => {
      const fields: FieldDefinition[] = [{
        name: 'name',
        type: FieldType.STRING,
        label: 'Name',
        validation: { max: 10 }
      }];

      expect(validator.validate({ name: 'John' }, fields).valid).toBe(true);
      expect(validator.validate({ name: 'VeryLongName' }, fields).valid).toBe(false);
    });
  });

  describe('Pattern validation', () => {
    it('should validate pattern for strings', () => {
      const fields: FieldDefinition[] = [{
        name: 'email',
        type: FieldType.STRING,
        label: 'Email',
        validation: { pattern: '^[a-z]+@[a-z]+\\.[a-z]+$' }
      }];

      expect(validator.validate({ email: 'test@example.com' }, fields).valid).toBe(true);
      expect(validator.validate({ email: 'invalid-email' }, fields).valid).toBe(false);
    });
  });

  describe('Enum validation', () => {
    it('should validate enum values', () => {
      const fields: FieldDefinition[] = [{
        name: 'status',
        type: FieldType.STRING,
        label: 'Status',
        validation: { enum: ['active', 'inactive', 'pending'] }
      }];

      expect(validator.validate({ status: 'active' }, fields).valid).toBe(true);
      expect(validator.validate({ status: 'invalid' }, fields).valid).toBe(false);
    });
  });

  describe('Unknown fields', () => {
    it('should detect unknown fields', () => {
      const fields: FieldDefinition[] = [{
        name: 'name',
        type: FieldType.STRING,
        label: 'Name'
      }];

      const result = validator.validate({ name: 'test', unknown: 'value' }, fields);
      expect(result.valid).toBe(false);
      expect(result.errors.some(e => e.field === 'unknown')).toBe(true);
    });
  });

  describe('Optional fields', () => {
    it('should allow optional fields to be omitted', () => {
      const fields: FieldDefinition[] = [{
        name: 'optional',
        type: FieldType.STRING,
        label: 'Optional Field'
      }];

      const result = validator.validate({}, fields);
      expect(result.valid).toBe(true);
    });
  });
});
