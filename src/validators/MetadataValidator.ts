/**
 * Metadata and field validator
 */

import {
  FieldDefinition,
  FieldType,
  Metadata,
  ValidationResult,
  ValidationError
} from '../types/metadata';

/**
 * Validates metadata against field definitions
 */
export class MetadataValidator {
  /**
   * Validate metadata against field definitions
   */
  validate(metadata: Metadata, fieldDefinitions: FieldDefinition[]): ValidationResult {
    const errors: ValidationError[] = [];

    // Check required fields
    for (const fieldDef of fieldDefinitions) {
      const value = metadata[fieldDef.name];

      // Check required
      if (fieldDef.validation?.required && (value === undefined || value === null)) {
        errors.push({
          field: fieldDef.name,
          message: `Field '${fieldDef.label}' is required`,
          value
        });
        continue;
      }

      // Skip validation if value is not provided and not required
      if (value === undefined || value === null) {
        continue;
      }

      // Type validation
      const typeError = this.validateType(fieldDef.name, value, fieldDef.type, fieldDef.label);
      if (typeError) {
        errors.push(typeError);
        continue;
      }

      // Additional validation rules
      if (fieldDef.validation) {
        const validationErrors = this.validateRules(fieldDef.name, value, fieldDef.validation, fieldDef.label);
        errors.push(...validationErrors);
      }
    }

    // Check for unknown fields
    const definedFieldNames = new Set(fieldDefinitions.map(f => f.name));
    for (const key of Object.keys(metadata)) {
      if (!definedFieldNames.has(key)) {
        errors.push({
          field: key,
          message: `Unknown field '${key}'`,
          value: metadata[key]
        });
      }
    }

    return {
      valid: errors.length === 0,
      errors
    };
  }

  /**
   * Validate type
   */
  private validateType(
    fieldName: string,
    value: any,
    expectedType: FieldType,
    label: string
  ): ValidationError | null {
    switch (expectedType) {
      case FieldType.STRING:
        if (typeof value !== 'string') {
          return {
            field: fieldName,
            message: `Field '${label}' must be a string`,
            value
          };
        }
        break;

      case FieldType.NUMBER:
        if (typeof value !== 'number' || isNaN(value)) {
          return {
            field: fieldName,
            message: `Field '${label}' must be a number`,
            value
          };
        }
        break;

      case FieldType.BOOLEAN:
        if (typeof value !== 'boolean') {
          return {
            field: fieldName,
            message: `Field '${label}' must be a boolean`,
            value
          };
        }
        break;

      case FieldType.DATE:
        if (!(value instanceof Date) && isNaN(Date.parse(value))) {
          return {
            field: fieldName,
            message: `Field '${label}' must be a valid date`,
            value
          };
        }
        break;

      case FieldType.ARRAY:
        if (!Array.isArray(value)) {
          return {
            field: fieldName,
            message: `Field '${label}' must be an array`,
            value
          };
        }
        break;

      case FieldType.OBJECT:
        if (typeof value !== 'object' || Array.isArray(value) || value === null) {
          return {
            field: fieldName,
            message: `Field '${label}' must be an object`,
            value
          };
        }
        break;
    }

    return null;
  }

  /**
   * Validate additional rules
   */
  private validateRules(
    fieldName: string,
    value: any,
    validation: any,
    label: string
  ): ValidationError[] {
    const errors: ValidationError[] = [];

    // Min validation (for numbers and strings)
    if (validation.min !== undefined) {
      if (typeof value === 'number' && value < validation.min) {
        errors.push({
          field: fieldName,
          message: `Field '${label}' must be at least ${validation.min}`,
          value
        });
      } else if (typeof value === 'string' && value.length < validation.min) {
        errors.push({
          field: fieldName,
          message: `Field '${label}' must be at least ${validation.min} characters`,
          value
        });
      }
    }

    // Max validation (for numbers and strings)
    if (validation.max !== undefined) {
      if (typeof value === 'number' && value > validation.max) {
        errors.push({
          field: fieldName,
          message: `Field '${label}' must be at most ${validation.max}`,
          value
        });
      } else if (typeof value === 'string' && value.length > validation.max) {
        errors.push({
          field: fieldName,
          message: `Field '${label}' must be at most ${validation.max} characters`,
          value
        });
      }
    }

    // Pattern validation (for strings)
    if (validation.pattern && typeof value === 'string') {
      const regex = new RegExp(validation.pattern);
      if (!regex.test(value)) {
        errors.push({
          field: fieldName,
          message: `Field '${label}' does not match the required pattern`,
          value
        });
      }
    }

    // Enum validation
    if (validation.enum && !validation.enum.includes(value)) {
      errors.push({
        field: fieldName,
        message: `Field '${label}' must be one of: ${validation.enum.join(', ')}`,
        value
      });
    }

    // Custom validation
    if (validation.custom) {
      const result = validation.custom(value);
      if (result !== true) {
        errors.push({
          field: fieldName,
          message: typeof result === 'string' ? result : `Field '${label}' failed custom validation`,
          value
        });
      }
    }

    return errors;
  }
}
