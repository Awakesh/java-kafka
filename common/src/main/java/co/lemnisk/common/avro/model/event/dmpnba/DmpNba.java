/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package co.lemnisk.common.avro.model.event.dmpnba;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class DmpNba extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -7022082878166985555L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"DmpNba\",\"namespace\":\"co.lemnisk.common.avro.model.event.dmpnba\",\"fields\":[{\"name\":\"userId\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"context\",\"type\":{\"type\":\"record\",\"name\":\"Context\",\"fields\":[{\"name\":\"srcId\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"writeKey\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"accountId\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"server_ts\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"otherIds\",\"type\":{\"type\":\"map\",\"values\":[\"null\",\"int\",\"string\",\"boolean\"],\"default\":{}}}]}},{\"name\":\"event\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"lemEvent\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"messageId\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"properties\",\"type\":{\"type\":\"map\",\"values\":[\"null\",\"int\",\"string\",\"boolean\",{\"type\":\"array\",\"items\":\"string\"},{\"type\":\"map\",\"values\":[\"null\",\"int\",\"string\",\"boolean\",{\"type\":\"array\",\"items\":\"string\"},{\"type\":\"map\",\"values\":[\"null\",\"int\",\"string\",\"boolean\",{\"type\":\"array\",\"items\":\"string\"},{\"type\":\"map\",\"values\":[\"null\",\"int\",\"string\",\"boolean\",{\"type\":\"array\",\"items\":\"string\"}]}]}]}],\"default\":{}}},{\"name\":\"destinationInstanceId\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"tracerId\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"spanId\",\"type\":\"string\",\"doc\":\"This is used for Tracing\",\"default\":\"\"},{\"name\":\"type\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"isStandardEvent\",\"type\":[\"null\",\"boolean\"],\"default\":null}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<DmpNba> ENCODER =
      new BinaryMessageEncoder<DmpNba>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<DmpNba> DECODER =
      new BinaryMessageDecoder<DmpNba>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<DmpNba> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<DmpNba> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<DmpNba>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this DmpNba to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a DmpNba from a ByteBuffer. */
  public static DmpNba fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.CharSequence userId;
  @Deprecated public co.lemnisk.common.avro.model.event.dmpnba.Context context;
  @Deprecated public java.lang.CharSequence event;
  @Deprecated public java.lang.CharSequence lemEvent;
  @Deprecated public java.lang.CharSequence messageId;
  @Deprecated public java.util.Map<java.lang.CharSequence,java.lang.Object> properties;
  @Deprecated public java.lang.CharSequence destinationInstanceId;
  @Deprecated public java.lang.CharSequence tracerId;
  /** This is used for Tracing */
  @Deprecated public java.lang.CharSequence spanId;
  @Deprecated public java.lang.CharSequence type;
  @Deprecated public java.lang.Boolean isStandardEvent;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public DmpNba() {}

  /**
   * All-args constructor.
   * @param userId The new value for userId
   * @param context The new value for context
   * @param event The new value for event
   * @param lemEvent The new value for lemEvent
   * @param messageId The new value for messageId
   * @param properties The new value for properties
   * @param destinationInstanceId The new value for destinationInstanceId
   * @param tracerId The new value for tracerId
   * @param spanId This is used for Tracing
   * @param type The new value for type
   * @param isStandardEvent The new value for isStandardEvent
   */
  public DmpNba(java.lang.CharSequence userId, co.lemnisk.common.avro.model.event.dmpnba.Context context, java.lang.CharSequence event, java.lang.CharSequence lemEvent, java.lang.CharSequence messageId, java.util.Map<java.lang.CharSequence,java.lang.Object> properties, java.lang.CharSequence destinationInstanceId, java.lang.CharSequence tracerId, java.lang.CharSequence spanId, java.lang.CharSequence type, java.lang.Boolean isStandardEvent) {
    this.userId = userId;
    this.context = context;
    this.event = event;
    this.lemEvent = lemEvent;
    this.messageId = messageId;
    this.properties = properties;
    this.destinationInstanceId = destinationInstanceId;
    this.tracerId = tracerId;
    this.spanId = spanId;
    this.type = type;
    this.isStandardEvent = isStandardEvent;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return userId;
    case 1: return context;
    case 2: return event;
    case 3: return lemEvent;
    case 4: return messageId;
    case 5: return properties;
    case 6: return destinationInstanceId;
    case 7: return tracerId;
    case 8: return spanId;
    case 9: return type;
    case 10: return isStandardEvent;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: userId = (java.lang.CharSequence)value$; break;
    case 1: context = (co.lemnisk.common.avro.model.event.dmpnba.Context)value$; break;
    case 2: event = (java.lang.CharSequence)value$; break;
    case 3: lemEvent = (java.lang.CharSequence)value$; break;
    case 4: messageId = (java.lang.CharSequence)value$; break;
    case 5: properties = (java.util.Map<java.lang.CharSequence,java.lang.Object>)value$; break;
    case 6: destinationInstanceId = (java.lang.CharSequence)value$; break;
    case 7: tracerId = (java.lang.CharSequence)value$; break;
    case 8: spanId = (java.lang.CharSequence)value$; break;
    case 9: type = (java.lang.CharSequence)value$; break;
    case 10: isStandardEvent = (java.lang.Boolean)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'userId' field.
   * @return The value of the 'userId' field.
   */
  public java.lang.CharSequence getUserId() {
    return userId;
  }

  /**
   * Sets the value of the 'userId' field.
   * @param value the value to set.
   */
  public void setUserId(java.lang.CharSequence value) {
    this.userId = value;
  }

  /**
   * Gets the value of the 'context' field.
   * @return The value of the 'context' field.
   */
  public co.lemnisk.common.avro.model.event.dmpnba.Context getContext() {
    return context;
  }

  /**
   * Sets the value of the 'context' field.
   * @param value the value to set.
   */
  public void setContext(co.lemnisk.common.avro.model.event.dmpnba.Context value) {
    this.context = value;
  }

  /**
   * Gets the value of the 'event' field.
   * @return The value of the 'event' field.
   */
  public java.lang.CharSequence getEvent() {
    return event;
  }

  /**
   * Sets the value of the 'event' field.
   * @param value the value to set.
   */
  public void setEvent(java.lang.CharSequence value) {
    this.event = value;
  }

  /**
   * Gets the value of the 'lemEvent' field.
   * @return The value of the 'lemEvent' field.
   */
  public java.lang.CharSequence getLemEvent() {
    return lemEvent;
  }

  /**
   * Sets the value of the 'lemEvent' field.
   * @param value the value to set.
   */
  public void setLemEvent(java.lang.CharSequence value) {
    this.lemEvent = value;
  }

  /**
   * Gets the value of the 'messageId' field.
   * @return The value of the 'messageId' field.
   */
  public java.lang.CharSequence getMessageId() {
    return messageId;
  }

  /**
   * Sets the value of the 'messageId' field.
   * @param value the value to set.
   */
  public void setMessageId(java.lang.CharSequence value) {
    this.messageId = value;
  }

  /**
   * Gets the value of the 'properties' field.
   * @return The value of the 'properties' field.
   */
  public java.util.Map<java.lang.CharSequence,java.lang.Object> getProperties() {
    return properties;
  }

  /**
   * Sets the value of the 'properties' field.
   * @param value the value to set.
   */
  public void setProperties(java.util.Map<java.lang.CharSequence,java.lang.Object> value) {
    this.properties = value;
  }

  /**
   * Gets the value of the 'destinationInstanceId' field.
   * @return The value of the 'destinationInstanceId' field.
   */
  public java.lang.CharSequence getDestinationInstanceId() {
    return destinationInstanceId;
  }

  /**
   * Sets the value of the 'destinationInstanceId' field.
   * @param value the value to set.
   */
  public void setDestinationInstanceId(java.lang.CharSequence value) {
    this.destinationInstanceId = value;
  }

  /**
   * Gets the value of the 'tracerId' field.
   * @return The value of the 'tracerId' field.
   */
  public java.lang.CharSequence getTracerId() {
    return tracerId;
  }

  /**
   * Sets the value of the 'tracerId' field.
   * @param value the value to set.
   */
  public void setTracerId(java.lang.CharSequence value) {
    this.tracerId = value;
  }

  /**
   * Gets the value of the 'spanId' field.
   * @return This is used for Tracing
   */
  public java.lang.CharSequence getSpanId() {
    return spanId;
  }

  /**
   * Sets the value of the 'spanId' field.
   * This is used for Tracing
   * @param value the value to set.
   */
  public void setSpanId(java.lang.CharSequence value) {
    this.spanId = value;
  }

  /**
   * Gets the value of the 'type' field.
   * @return The value of the 'type' field.
   */
  public java.lang.CharSequence getType() {
    return type;
  }

  /**
   * Sets the value of the 'type' field.
   * @param value the value to set.
   */
  public void setType(java.lang.CharSequence value) {
    this.type = value;
  }

  /**
   * Gets the value of the 'isStandardEvent' field.
   * @return The value of the 'isStandardEvent' field.
   */
  public java.lang.Boolean getIsStandardEvent() {
    return isStandardEvent;
  }

  /**
   * Sets the value of the 'isStandardEvent' field.
   * @param value the value to set.
   */
  public void setIsStandardEvent(java.lang.Boolean value) {
    this.isStandardEvent = value;
  }

  /**
   * Creates a new DmpNba RecordBuilder.
   * @return A new DmpNba RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder newBuilder() {
    return new co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder();
  }

  /**
   * Creates a new DmpNba RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new DmpNba RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder newBuilder(co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder other) {
    return new co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder(other);
  }

  /**
   * Creates a new DmpNba RecordBuilder by copying an existing DmpNba instance.
   * @param other The existing instance to copy.
   * @return A new DmpNba RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder newBuilder(co.lemnisk.common.avro.model.event.dmpnba.DmpNba other) {
    return new co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder(other);
  }

  /**
   * RecordBuilder for DmpNba instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<DmpNba>
    implements org.apache.avro.data.RecordBuilder<DmpNba> {

    private java.lang.CharSequence userId;
    private co.lemnisk.common.avro.model.event.dmpnba.Context context;
    private co.lemnisk.common.avro.model.event.dmpnba.Context.Builder contextBuilder;
    private java.lang.CharSequence event;
    private java.lang.CharSequence lemEvent;
    private java.lang.CharSequence messageId;
    private java.util.Map<java.lang.CharSequence,java.lang.Object> properties;
    private java.lang.CharSequence destinationInstanceId;
    private java.lang.CharSequence tracerId;
    /** This is used for Tracing */
    private java.lang.CharSequence spanId;
    private java.lang.CharSequence type;
    private java.lang.Boolean isStandardEvent;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.userId)) {
        this.userId = data().deepCopy(fields()[0].schema(), other.userId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.context)) {
        this.context = data().deepCopy(fields()[1].schema(), other.context);
        fieldSetFlags()[1] = true;
      }
      if (other.hasContextBuilder()) {
        this.contextBuilder = co.lemnisk.common.avro.model.event.dmpnba.Context.newBuilder(other.getContextBuilder());
      }
      if (isValidValue(fields()[2], other.event)) {
        this.event = data().deepCopy(fields()[2].schema(), other.event);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.lemEvent)) {
        this.lemEvent = data().deepCopy(fields()[3].schema(), other.lemEvent);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.messageId)) {
        this.messageId = data().deepCopy(fields()[4].schema(), other.messageId);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.properties)) {
        this.properties = data().deepCopy(fields()[5].schema(), other.properties);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.destinationInstanceId)) {
        this.destinationInstanceId = data().deepCopy(fields()[6].schema(), other.destinationInstanceId);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.tracerId)) {
        this.tracerId = data().deepCopy(fields()[7].schema(), other.tracerId);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.spanId)) {
        this.spanId = data().deepCopy(fields()[8].schema(), other.spanId);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.type)) {
        this.type = data().deepCopy(fields()[9].schema(), other.type);
        fieldSetFlags()[9] = true;
      }
      if (isValidValue(fields()[10], other.isStandardEvent)) {
        this.isStandardEvent = data().deepCopy(fields()[10].schema(), other.isStandardEvent);
        fieldSetFlags()[10] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing DmpNba instance
     * @param other The existing instance to copy.
     */
    private Builder(co.lemnisk.common.avro.model.event.dmpnba.DmpNba other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.userId)) {
        this.userId = data().deepCopy(fields()[0].schema(), other.userId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.context)) {
        this.context = data().deepCopy(fields()[1].schema(), other.context);
        fieldSetFlags()[1] = true;
      }
      this.contextBuilder = null;
      if (isValidValue(fields()[2], other.event)) {
        this.event = data().deepCopy(fields()[2].schema(), other.event);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.lemEvent)) {
        this.lemEvent = data().deepCopy(fields()[3].schema(), other.lemEvent);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.messageId)) {
        this.messageId = data().deepCopy(fields()[4].schema(), other.messageId);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.properties)) {
        this.properties = data().deepCopy(fields()[5].schema(), other.properties);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.destinationInstanceId)) {
        this.destinationInstanceId = data().deepCopy(fields()[6].schema(), other.destinationInstanceId);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.tracerId)) {
        this.tracerId = data().deepCopy(fields()[7].schema(), other.tracerId);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.spanId)) {
        this.spanId = data().deepCopy(fields()[8].schema(), other.spanId);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.type)) {
        this.type = data().deepCopy(fields()[9].schema(), other.type);
        fieldSetFlags()[9] = true;
      }
      if (isValidValue(fields()[10], other.isStandardEvent)) {
        this.isStandardEvent = data().deepCopy(fields()[10].schema(), other.isStandardEvent);
        fieldSetFlags()[10] = true;
      }
    }

    /**
      * Gets the value of the 'userId' field.
      * @return The value.
      */
    public java.lang.CharSequence getUserId() {
      return userId;
    }

    /**
      * Sets the value of the 'userId' field.
      * @param value The value of 'userId'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setUserId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.userId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'userId' field has been set.
      * @return True if the 'userId' field has been set, false otherwise.
      */
    public boolean hasUserId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'userId' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder clearUserId() {
      userId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'context' field.
      * @return The value.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.Context getContext() {
      return context;
    }

    /**
      * Sets the value of the 'context' field.
      * @param value The value of 'context'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setContext(co.lemnisk.common.avro.model.event.dmpnba.Context value) {
      validate(fields()[1], value);
      this.contextBuilder = null;
      this.context = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'context' field has been set.
      * @return True if the 'context' field has been set, false otherwise.
      */
    public boolean hasContext() {
      return fieldSetFlags()[1];
    }

    /**
     * Gets the Builder instance for the 'context' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.dmpnba.Context.Builder getContextBuilder() {
      if (contextBuilder == null) {
        if (hasContext()) {
          setContextBuilder(co.lemnisk.common.avro.model.event.dmpnba.Context.newBuilder(context));
        } else {
          setContextBuilder(co.lemnisk.common.avro.model.event.dmpnba.Context.newBuilder());
        }
      }
      return contextBuilder;
    }

    /**
     * Sets the Builder instance for the 'context' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setContextBuilder(co.lemnisk.common.avro.model.event.dmpnba.Context.Builder value) {
      clearContext();
      contextBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'context' field has an active Builder instance
     * @return True if the 'context' field has an active Builder instance
     */
    public boolean hasContextBuilder() {
      return contextBuilder != null;
    }

    /**
      * Clears the value of the 'context' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder clearContext() {
      context = null;
      contextBuilder = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'event' field.
      * @return The value.
      */
    public java.lang.CharSequence getEvent() {
      return event;
    }

    /**
      * Sets the value of the 'event' field.
      * @param value The value of 'event'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setEvent(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.event = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'event' field has been set.
      * @return True if the 'event' field has been set, false otherwise.
      */
    public boolean hasEvent() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'event' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder clearEvent() {
      event = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'lemEvent' field.
      * @return The value.
      */
    public java.lang.CharSequence getLemEvent() {
      return lemEvent;
    }

    /**
      * Sets the value of the 'lemEvent' field.
      * @param value The value of 'lemEvent'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setLemEvent(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.lemEvent = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'lemEvent' field has been set.
      * @return True if the 'lemEvent' field has been set, false otherwise.
      */
    public boolean hasLemEvent() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'lemEvent' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder clearLemEvent() {
      lemEvent = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'messageId' field.
      * @return The value.
      */
    public java.lang.CharSequence getMessageId() {
      return messageId;
    }

    /**
      * Sets the value of the 'messageId' field.
      * @param value The value of 'messageId'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setMessageId(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.messageId = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'messageId' field has been set.
      * @return True if the 'messageId' field has been set, false otherwise.
      */
    public boolean hasMessageId() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'messageId' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder clearMessageId() {
      messageId = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'properties' field.
      * @return The value.
      */
    public java.util.Map<java.lang.CharSequence,java.lang.Object> getProperties() {
      return properties;
    }

    /**
      * Sets the value of the 'properties' field.
      * @param value The value of 'properties'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setProperties(java.util.Map<java.lang.CharSequence,java.lang.Object> value) {
      validate(fields()[5], value);
      this.properties = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'properties' field has been set.
      * @return True if the 'properties' field has been set, false otherwise.
      */
    public boolean hasProperties() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'properties' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder clearProperties() {
      properties = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'destinationInstanceId' field.
      * @return The value.
      */
    public java.lang.CharSequence getDestinationInstanceId() {
      return destinationInstanceId;
    }

    /**
      * Sets the value of the 'destinationInstanceId' field.
      * @param value The value of 'destinationInstanceId'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setDestinationInstanceId(java.lang.CharSequence value) {
      validate(fields()[6], value);
      this.destinationInstanceId = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
      * Checks whether the 'destinationInstanceId' field has been set.
      * @return True if the 'destinationInstanceId' field has been set, false otherwise.
      */
    public boolean hasDestinationInstanceId() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'destinationInstanceId' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder clearDestinationInstanceId() {
      destinationInstanceId = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /**
      * Gets the value of the 'tracerId' field.
      * @return The value.
      */
    public java.lang.CharSequence getTracerId() {
      return tracerId;
    }

    /**
      * Sets the value of the 'tracerId' field.
      * @param value The value of 'tracerId'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setTracerId(java.lang.CharSequence value) {
      validate(fields()[7], value);
      this.tracerId = value;
      fieldSetFlags()[7] = true;
      return this;
    }

    /**
      * Checks whether the 'tracerId' field has been set.
      * @return True if the 'tracerId' field has been set, false otherwise.
      */
    public boolean hasTracerId() {
      return fieldSetFlags()[7];
    }


    /**
      * Clears the value of the 'tracerId' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder clearTracerId() {
      tracerId = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    /**
      * Gets the value of the 'spanId' field.
      * This is used for Tracing
      * @return The value.
      */
    public java.lang.CharSequence getSpanId() {
      return spanId;
    }

    /**
      * Sets the value of the 'spanId' field.
      * This is used for Tracing
      * @param value The value of 'spanId'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setSpanId(java.lang.CharSequence value) {
      validate(fields()[8], value);
      this.spanId = value;
      fieldSetFlags()[8] = true;
      return this;
    }

    /**
      * Checks whether the 'spanId' field has been set.
      * This is used for Tracing
      * @return True if the 'spanId' field has been set, false otherwise.
      */
    public boolean hasSpanId() {
      return fieldSetFlags()[8];
    }


    /**
      * Clears the value of the 'spanId' field.
      * This is used for Tracing
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder clearSpanId() {
      spanId = null;
      fieldSetFlags()[8] = false;
      return this;
    }

    /**
      * Gets the value of the 'type' field.
      * @return The value.
      */
    public java.lang.CharSequence getType() {
      return type;
    }

    /**
      * Sets the value of the 'type' field.
      * @param value The value of 'type'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setType(java.lang.CharSequence value) {
      validate(fields()[9], value);
      this.type = value;
      fieldSetFlags()[9] = true;
      return this;
    }

    /**
      * Checks whether the 'type' field has been set.
      * @return True if the 'type' field has been set, false otherwise.
      */
    public boolean hasType() {
      return fieldSetFlags()[9];
    }


    /**
      * Clears the value of the 'type' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder clearType() {
      type = null;
      fieldSetFlags()[9] = false;
      return this;
    }

    /**
      * Gets the value of the 'isStandardEvent' field.
      * @return The value.
      */
    public java.lang.Boolean getIsStandardEvent() {
      return isStandardEvent;
    }

    /**
      * Sets the value of the 'isStandardEvent' field.
      * @param value The value of 'isStandardEvent'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder setIsStandardEvent(java.lang.Boolean value) {
      validate(fields()[10], value);
      this.isStandardEvent = value;
      fieldSetFlags()[10] = true;
      return this;
    }

    /**
      * Checks whether the 'isStandardEvent' field has been set.
      * @return True if the 'isStandardEvent' field has been set, false otherwise.
      */
    public boolean hasIsStandardEvent() {
      return fieldSetFlags()[10];
    }


    /**
      * Clears the value of the 'isStandardEvent' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.dmpnba.DmpNba.Builder clearIsStandardEvent() {
      isStandardEvent = null;
      fieldSetFlags()[10] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DmpNba build() {
      try {
        DmpNba record = new DmpNba();
        record.userId = fieldSetFlags()[0] ? this.userId : (java.lang.CharSequence) defaultValue(fields()[0]);
        if (contextBuilder != null) {
          record.context = this.contextBuilder.build();
        } else {
          record.context = fieldSetFlags()[1] ? this.context : (co.lemnisk.common.avro.model.event.dmpnba.Context) defaultValue(fields()[1]);
        }
        record.event = fieldSetFlags()[2] ? this.event : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.lemEvent = fieldSetFlags()[3] ? this.lemEvent : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.messageId = fieldSetFlags()[4] ? this.messageId : (java.lang.CharSequence) defaultValue(fields()[4]);
        record.properties = fieldSetFlags()[5] ? this.properties : (java.util.Map<java.lang.CharSequence,java.lang.Object>) defaultValue(fields()[5]);
        record.destinationInstanceId = fieldSetFlags()[6] ? this.destinationInstanceId : (java.lang.CharSequence) defaultValue(fields()[6]);
        record.tracerId = fieldSetFlags()[7] ? this.tracerId : (java.lang.CharSequence) defaultValue(fields()[7]);
        record.spanId = fieldSetFlags()[8] ? this.spanId : (java.lang.CharSequence) defaultValue(fields()[8]);
        record.type = fieldSetFlags()[9] ? this.type : (java.lang.CharSequence) defaultValue(fields()[9]);
        record.isStandardEvent = fieldSetFlags()[10] ? this.isStandardEvent : (java.lang.Boolean) defaultValue(fields()[10]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<DmpNba>
    WRITER$ = (org.apache.avro.io.DatumWriter<DmpNba>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<DmpNba>
    READER$ = (org.apache.avro.io.DatumReader<DmpNba>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
