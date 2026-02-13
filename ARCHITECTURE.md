# Order Metadata System - Architecture Documentation

## Overview

The Order Metadata System is an extensible framework built on Java that allows orders to be enriched with various types of metadata without modifying the core Order entity. This design follows SOLID principles and common design patterns to ensure maintainability and extensibility.

## Architecture Components

### 1. Core Interfaces and Abstract Classes

#### `Metadata` Interface
The central contract for all metadata types. Defines the following operations:
- `getType()` - Returns the unique type identifier
- `getAttributes()` - Returns all key-value attributes
- `getAttribute(key)` - Gets a specific attribute
- `setAttribute(key, value)` - Sets a specific attribute
- `validate()` - Validates the metadata state

#### `BaseMetadata` Abstract Class
Provides default implementation of the `Metadata` interface:
- Manages a protected `attributes` map
- Implements common attribute operations
- Provides a `removeAttribute()` method for encapsulation
- Default `validate()` returns true (can be overridden)

#### `MetadataFactory` Interface
Factory pattern for creating metadata instances:
- `getType()` - Returns the metadata type this factory creates
- `create()` - Creates a new instance of the metadata

### 2. Registry System

#### `MetadataRegistry` (Singleton)
Central registry for managing metadata factories:
- **Singleton Pattern**: Ensures only one registry instance exists
- **Thread-Safe**: Uses `ConcurrentHashMap` for factory storage
- **Operations**:
  - `registerFactory(factory)` - Register a new metadata type
  - `unregisterFactory(type)` - Remove a metadata type
  - `createMetadata(type)` - Create metadata instance by type
  - `isRegistered(type)` - Check if a type is registered
  - `clear()` - Clear all registrations (for testing)

### 3. Domain Model

#### `Order` Entity
The main business entity that can hold multiple metadata instances:
- **Core Properties**: orderId, customerId, createTime, totalAmount, status
- **Metadata Management**:
  - `addMetadata(metadata)` - Attach metadata to order
  - `getMetadataList()` - Get all attached metadata
  - `getMetadataByType(type)` - Retrieve specific metadata by type
  - `removeMetadataByType(type)` - Remove metadata by type
  - `validateMetadata()` - Validate all attached metadata

### 4. Metadata Extensions

The system includes three built-in metadata types as examples:

#### `ShippingMetadata`
Handles shipping-related information:
- Address (required for validation)
- Carrier
- Tracking Number
- Estimated Delivery

#### `PaymentMetadata`
Manages payment information:
- Payment Method (required for validation)
- Transaction ID
- Payment Status
- Payment Time

#### `CustomFieldsMetadata`
Provides flexible key-value storage:
- `addField(name, value)` - Add arbitrary fields
- `getField(name)` - Retrieve field value
- `removeField(name)` - Remove a field
- No validation constraints (always valid)

## Design Patterns Used

### 1. Factory Pattern
Each metadata type has a corresponding factory class implementing `MetadataFactory`. This decouples object creation from usage.

```java
ShippingMetadataFactory factory = new ShippingMetadataFactory();
Metadata shipping = factory.create();
```

### 2. Registry Pattern
`MetadataRegistry` acts as a central point for registering and retrieving factories, enabling dynamic discovery of metadata types.

```java
MetadataRegistry registry = MetadataRegistry.getInstance();
registry.registerFactory(new ShippingMetadataFactory());
Metadata shipping = registry.createMetadata("SHIPPING");
```

### 3. Template Method Pattern
`BaseMetadata` provides a template implementation that subclasses can extend, customizing only what they need (e.g., validation logic).

### 4. Strategy Pattern
Each metadata type can define its own validation strategy by overriding the `validate()` method.

## Extensibility Guide

### Adding a New Metadata Type

1. **Create the Metadata Class**
   ```java
   public class InventoryMetadata extends BaseMetadata {
       public static final String TYPE = "INVENTORY";
       
       public InventoryMetadata() {
           super(TYPE);
       }
       
       // Add specific getters/setters
       public void setWarehouseId(String id) {
           setAttribute("warehouseId", id);
       }
       
       public String getWarehouseId() {
           return (String) getAttribute("warehouseId");
       }
       
       @Override
       public boolean validate() {
           return getWarehouseId() != null;
       }
   }
   ```

2. **Create the Factory**
   ```java
   public class InventoryMetadataFactory implements MetadataFactory {
       @Override
       public String getType() {
           return InventoryMetadata.TYPE;
       }
       
       @Override
       public Metadata create() {
           return new InventoryMetadata();
       }
   }
   ```

3. **Register and Use**
   ```java
   MetadataRegistry.getInstance()
       .registerFactory(new InventoryMetadataFactory());
   
   InventoryMetadata inventory = (InventoryMetadata) 
       registry.createMetadata("INVENTORY");
   inventory.setWarehouseId("WH-001");
   order.addMetadata(inventory);
   ```

## Benefits

1. **Open/Closed Principle**: The system is open for extension (add new metadata types) but closed for modification (no need to change Order or core classes).

2. **Single Responsibility**: Each metadata type handles its own domain logic and validation.

3. **Dependency Inversion**: Order depends on the `Metadata` abstraction, not concrete implementations.

4. **Flexibility**: New metadata types can be added at runtime without recompiling existing code.

5. **Type Safety**: While using a generic attributes map internally, the API exposes type-safe methods.

6. **Testability**: Each component can be tested in isolation. The registry can be cleared between tests.

## Thread Safety

- `MetadataRegistry` uses `ConcurrentHashMap` for thread-safe factory management
- Individual metadata instances are NOT thread-safe by design (they're meant to be used within a single thread context)
- If shared across threads, external synchronization is required

## Testing Strategy

The system includes comprehensive tests:

1. **Unit Tests**: Test individual metadata types and their validation
2. **Registry Tests**: Test factory registration and creation
3. **Order Tests**: Test metadata management in orders
4. **Integration Tests**: Test complete workflows combining multiple metadata types

All tests use JUnit 4 and follow the AAA (Arrange-Act-Assert) pattern.

## Future Enhancements

Potential areas for extension:

1. **Metadata Serialization**: Add JSON/XML serialization support
2. **Metadata Events**: Add listeners for metadata changes
3. **Metadata Validation Groups**: Support complex validation scenarios
4. **Metadata Versioning**: Track changes to metadata over time
5. **Metadata Query DSL**: Build queries to find orders by metadata criteria
6. **Metadata Inheritance**: Allow metadata types to inherit from other metadata types

## Conclusion

This architecture provides a solid foundation for managing extensible order metadata in a Java application. By following established design patterns and SOLID principles, the system remains maintainable, testable, and easy to extend as business requirements evolve.
