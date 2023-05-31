/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package co.lemnisk.common.avro.model.event.track.app;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class ContextDevice extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -5898221079567973747L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ContextDevice\",\"namespace\":\"co.lemnisk.common.avro.model.event.track.app\",\"fields\":[{\"name\":\"id\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"advertisingId\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"adTrackingEnabled\",\"type\":\"boolean\"},{\"name\":\"manufacturer\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"model\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"type\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"token\",\"type\":\"string\",\"default\":\"\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<ContextDevice> ENCODER =
      new BinaryMessageEncoder<ContextDevice>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<ContextDevice> DECODER =
      new BinaryMessageDecoder<ContextDevice>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<ContextDevice> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<ContextDevice> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<ContextDevice>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this ContextDevice to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a ContextDevice from a ByteBuffer. */
  public static ContextDevice fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.CharSequence id;
  @Deprecated public java.lang.CharSequence advertisingId;
  @Deprecated public boolean adTrackingEnabled;
  @Deprecated public java.lang.CharSequence manufacturer;
  @Deprecated public java.lang.CharSequence model;
  @Deprecated public java.lang.CharSequence type;
  @Deprecated public java.lang.CharSequence token;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ContextDevice() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param advertisingId The new value for advertisingId
   * @param adTrackingEnabled The new value for adTrackingEnabled
   * @param manufacturer The new value for manufacturer
   * @param model The new value for model
   * @param type The new value for type
   * @param token The new value for token
   */
  public ContextDevice(java.lang.CharSequence id, java.lang.CharSequence advertisingId, java.lang.Boolean adTrackingEnabled, java.lang.CharSequence manufacturer, java.lang.CharSequence model, java.lang.CharSequence type, java.lang.CharSequence token) {
    this.id = id;
    this.advertisingId = advertisingId;
    this.adTrackingEnabled = adTrackingEnabled;
    this.manufacturer = manufacturer;
    this.model = model;
    this.type = type;
    this.token = token;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return advertisingId;
    case 2: return adTrackingEnabled;
    case 3: return manufacturer;
    case 4: return model;
    case 5: return type;
    case 6: return token;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.CharSequence)value$; break;
    case 1: advertisingId = (java.lang.CharSequence)value$; break;
    case 2: adTrackingEnabled = (java.lang.Boolean)value$; break;
    case 3: manufacturer = (java.lang.CharSequence)value$; break;
    case 4: model = (java.lang.CharSequence)value$; break;
    case 5: type = (java.lang.CharSequence)value$; break;
    case 6: token = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public java.lang.CharSequence getId() {
    return id;
  }

  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.CharSequence value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'advertisingId' field.
   * @return The value of the 'advertisingId' field.
   */
  public java.lang.CharSequence getAdvertisingId() {
    return advertisingId;
  }

  /**
   * Sets the value of the 'advertisingId' field.
   * @param value the value to set.
   */
  public void setAdvertisingId(java.lang.CharSequence value) {
    this.advertisingId = value;
  }

  /**
   * Gets the value of the 'adTrackingEnabled' field.
   * @return The value of the 'adTrackingEnabled' field.
   */
  public java.lang.Boolean getAdTrackingEnabled() {
    return adTrackingEnabled;
  }

  /**
   * Sets the value of the 'adTrackingEnabled' field.
   * @param value the value to set.
   */
  public void setAdTrackingEnabled(java.lang.Boolean value) {
    this.adTrackingEnabled = value;
  }

  /**
   * Gets the value of the 'manufacturer' field.
   * @return The value of the 'manufacturer' field.
   */
  public java.lang.CharSequence getManufacturer() {
    return manufacturer;
  }

  /**
   * Sets the value of the 'manufacturer' field.
   * @param value the value to set.
   */
  public void setManufacturer(java.lang.CharSequence value) {
    this.manufacturer = value;
  }

  /**
   * Gets the value of the 'model' field.
   * @return The value of the 'model' field.
   */
  public java.lang.CharSequence getModel() {
    return model;
  }

  /**
   * Sets the value of the 'model' field.
   * @param value the value to set.
   */
  public void setModel(java.lang.CharSequence value) {
    this.model = value;
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
   * Gets the value of the 'token' field.
   * @return The value of the 'token' field.
   */
  public java.lang.CharSequence getToken() {
    return token;
  }

  /**
   * Sets the value of the 'token' field.
   * @param value the value to set.
   */
  public void setToken(java.lang.CharSequence value) {
    this.token = value;
  }

  /**
   * Creates a new ContextDevice RecordBuilder.
   * @return A new ContextDevice RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder newBuilder() {
    return new co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder();
  }

  /**
   * Creates a new ContextDevice RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ContextDevice RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder newBuilder(co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder other) {
    return new co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder(other);
  }

  /**
   * Creates a new ContextDevice RecordBuilder by copying an existing ContextDevice instance.
   * @param other The existing instance to copy.
   * @return A new ContextDevice RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder newBuilder(co.lemnisk.common.avro.model.event.track.app.ContextDevice other) {
    return new co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder(other);
  }

  /**
   * RecordBuilder for ContextDevice instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ContextDevice>
    implements org.apache.avro.data.RecordBuilder<ContextDevice> {

    private java.lang.CharSequence id;
    private java.lang.CharSequence advertisingId;
    private boolean adTrackingEnabled;
    private java.lang.CharSequence manufacturer;
    private java.lang.CharSequence model;
    private java.lang.CharSequence type;
    private java.lang.CharSequence token;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.advertisingId)) {
        this.advertisingId = data().deepCopy(fields()[1].schema(), other.advertisingId);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.adTrackingEnabled)) {
        this.adTrackingEnabled = data().deepCopy(fields()[2].schema(), other.adTrackingEnabled);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.manufacturer)) {
        this.manufacturer = data().deepCopy(fields()[3].schema(), other.manufacturer);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.model)) {
        this.model = data().deepCopy(fields()[4].schema(), other.model);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.type)) {
        this.type = data().deepCopy(fields()[5].schema(), other.type);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.token)) {
        this.token = data().deepCopy(fields()[6].schema(), other.token);
        fieldSetFlags()[6] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing ContextDevice instance
     * @param other The existing instance to copy.
     */
    private Builder(co.lemnisk.common.avro.model.event.track.app.ContextDevice other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.advertisingId)) {
        this.advertisingId = data().deepCopy(fields()[1].schema(), other.advertisingId);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.adTrackingEnabled)) {
        this.adTrackingEnabled = data().deepCopy(fields()[2].schema(), other.adTrackingEnabled);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.manufacturer)) {
        this.manufacturer = data().deepCopy(fields()[3].schema(), other.manufacturer);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.model)) {
        this.model = data().deepCopy(fields()[4].schema(), other.model);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.type)) {
        this.type = data().deepCopy(fields()[5].schema(), other.type);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.token)) {
        this.token = data().deepCopy(fields()[6].schema(), other.token);
        fieldSetFlags()[6] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public java.lang.CharSequence getId() {
      return id;
    }

    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder setId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'advertisingId' field.
      * @return The value.
      */
    public java.lang.CharSequence getAdvertisingId() {
      return advertisingId;
    }

    /**
      * Sets the value of the 'advertisingId' field.
      * @param value The value of 'advertisingId'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder setAdvertisingId(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.advertisingId = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'advertisingId' field has been set.
      * @return True if the 'advertisingId' field has been set, false otherwise.
      */
    public boolean hasAdvertisingId() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'advertisingId' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder clearAdvertisingId() {
      advertisingId = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'adTrackingEnabled' field.
      * @return The value.
      */
    public java.lang.Boolean getAdTrackingEnabled() {
      return adTrackingEnabled;
    }

    /**
      * Sets the value of the 'adTrackingEnabled' field.
      * @param value The value of 'adTrackingEnabled'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder setAdTrackingEnabled(boolean value) {
      validate(fields()[2], value);
      this.adTrackingEnabled = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'adTrackingEnabled' field has been set.
      * @return True if the 'adTrackingEnabled' field has been set, false otherwise.
      */
    public boolean hasAdTrackingEnabled() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'adTrackingEnabled' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder clearAdTrackingEnabled() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'manufacturer' field.
      * @return The value.
      */
    public java.lang.CharSequence getManufacturer() {
      return manufacturer;
    }

    /**
      * Sets the value of the 'manufacturer' field.
      * @param value The value of 'manufacturer'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder setManufacturer(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.manufacturer = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'manufacturer' field has been set.
      * @return True if the 'manufacturer' field has been set, false otherwise.
      */
    public boolean hasManufacturer() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'manufacturer' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder clearManufacturer() {
      manufacturer = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'model' field.
      * @return The value.
      */
    public java.lang.CharSequence getModel() {
      return model;
    }

    /**
      * Sets the value of the 'model' field.
      * @param value The value of 'model'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder setModel(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.model = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'model' field has been set.
      * @return True if the 'model' field has been set, false otherwise.
      */
    public boolean hasModel() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'model' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder clearModel() {
      model = null;
      fieldSetFlags()[4] = false;
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
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder setType(java.lang.CharSequence value) {
      validate(fields()[5], value);
      this.type = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'type' field has been set.
      * @return True if the 'type' field has been set, false otherwise.
      */
    public boolean hasType() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'type' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder clearType() {
      type = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'token' field.
      * @return The value.
      */
    public java.lang.CharSequence getToken() {
      return token;
    }

    /**
      * Sets the value of the 'token' field.
      * @param value The value of 'token'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder setToken(java.lang.CharSequence value) {
      validate(fields()[6], value);
      this.token = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
      * Checks whether the 'token' field has been set.
      * @return True if the 'token' field has been set, false otherwise.
      */
    public boolean hasToken() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'token' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.app.ContextDevice.Builder clearToken() {
      token = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ContextDevice build() {
      try {
        ContextDevice record = new ContextDevice();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.advertisingId = fieldSetFlags()[1] ? this.advertisingId : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.adTrackingEnabled = fieldSetFlags()[2] ? this.adTrackingEnabled : (java.lang.Boolean) defaultValue(fields()[2]);
        record.manufacturer = fieldSetFlags()[3] ? this.manufacturer : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.model = fieldSetFlags()[4] ? this.model : (java.lang.CharSequence) defaultValue(fields()[4]);
        record.type = fieldSetFlags()[5] ? this.type : (java.lang.CharSequence) defaultValue(fields()[5]);
        record.token = fieldSetFlags()[6] ? this.token : (java.lang.CharSequence) defaultValue(fields()[6]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<ContextDevice>
    WRITER$ = (org.apache.avro.io.DatumWriter<ContextDevice>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<ContextDevice>
    READER$ = (org.apache.avro.io.DatumReader<ContextDevice>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
