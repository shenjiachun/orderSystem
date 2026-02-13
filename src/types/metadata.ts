/**
 * Metadata capabilities for the order system
 */

/**
 * Supported field types for extensible business fields
 */
export enum FieldType {
  STRING = 'string',
  NUMBER = 'number',
  BOOLEAN = 'boolean',
  DATE = 'date',
  ARRAY = 'array',
  OBJECT = 'object'
}

/**
 * Validation rules for fields
 */
export interface FieldValidation {
  required?: boolean;
  min?: number;
  max?: number;
  pattern?: string;
  enum?: any[];
  custom?: (value: any) => boolean | string;
}

/**
 * Field definition for extensible business fields
 */
export interface FieldDefinition {
  name: string;
  type: FieldType;
  label: string;
  description?: string;
  validation?: FieldValidation;
  defaultValue?: any;
}

/**
 * Metadata schema that can be attached to orders
 */
export interface MetadataSchema {
  id: string;
  name: string;
  description?: string;
  version: string;
  fields: FieldDefinition[];
  createdAt: Date;
  updatedAt: Date;
}

/**
 * Generic metadata that can be attached to any entity
 */
export interface Metadata {
  [key: string]: any;
}

/**
 * Validation result
 */
export interface ValidationResult {
  valid: boolean;
  errors: ValidationError[];
}

/**
 * Validation error details
 */
export interface ValidationError {
  field: string;
  message: string;
  value?: any;
}
