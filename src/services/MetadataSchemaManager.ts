/**
 * Metadata Schema Manager
 * Manages field definitions and schemas for extensible business fields
 */

import { MetadataSchema, FieldDefinition } from '../types/metadata';

/**
 * Manages metadata schemas and field definitions
 */
export class MetadataSchemaManager {
  private schemas: Map<string, MetadataSchema> = new Map();

  /**
   * Register a new metadata schema
   */
  registerSchema(schema: Omit<MetadataSchema, 'createdAt' | 'updatedAt'>): MetadataSchema {
    const fullSchema: MetadataSchema = {
      ...schema,
      createdAt: new Date(),
      updatedAt: new Date()
    };

    this.schemas.set(schema.id, fullSchema);
    return fullSchema;
  }

  /**
   * Get a schema by ID
   */
  getSchema(id: string): MetadataSchema | undefined {
    return this.schemas.get(id);
  }

  /**
   * Get all schemas
   */
  getAllSchemas(): MetadataSchema[] {
    return Array.from(this.schemas.values());
  }

  /**
   * Update a schema
   */
  updateSchema(id: string, updates: Partial<Omit<MetadataSchema, 'id' | 'createdAt'>>): MetadataSchema | undefined {
    const existing = this.schemas.get(id);
    if (!existing) {
      return undefined;
    }

    const updated: MetadataSchema = {
      ...existing,
      ...updates,
      id: existing.id,
      createdAt: existing.createdAt,
      updatedAt: new Date()
    };

    this.schemas.set(id, updated);
    return updated;
  }

  /**
   * Delete a schema
   */
  deleteSchema(id: string): boolean {
    return this.schemas.delete(id);
  }

  /**
   * Get field definitions for a schema
   */
  getFieldDefinitions(schemaId: string): FieldDefinition[] {
    const schema = this.schemas.get(schemaId);
    return schema ? schema.fields : [];
  }

  /**
   * Add a field definition to a schema
   */
  addFieldDefinition(schemaId: string, field: FieldDefinition): boolean {
    const schema = this.schemas.get(schemaId);
    if (!schema) {
      return false;
    }

    // Check if field already exists
    const existingIndex = schema.fields.findIndex(f => f.name === field.name);
    if (existingIndex !== -1) {
      return false;
    }

    schema.fields.push(field);
    schema.updatedAt = new Date();
    return true;
  }

  /**
   * Update a field definition in a schema
   */
  updateFieldDefinition(schemaId: string, fieldName: string, updates: Partial<FieldDefinition>): boolean {
    const schema = this.schemas.get(schemaId);
    if (!schema) {
      return false;
    }

    const fieldIndex = schema.fields.findIndex(f => f.name === fieldName);
    if (fieldIndex === -1) {
      return false;
    }

    schema.fields[fieldIndex] = {
      ...schema.fields[fieldIndex],
      ...updates,
      name: schema.fields[fieldIndex].name // Preserve original name
    };
    schema.updatedAt = new Date();
    return true;
  }

  /**
   * Remove a field definition from a schema
   */
  removeFieldDefinition(schemaId: string, fieldName: string): boolean {
    const schema = this.schemas.get(schemaId);
    if (!schema) {
      return false;
    }

    const initialLength = schema.fields.length;
    schema.fields = schema.fields.filter(f => f.name !== fieldName);
    
    if (schema.fields.length < initialLength) {
      schema.updatedAt = new Date();
      return true;
    }
    
    return false;
  }
}
