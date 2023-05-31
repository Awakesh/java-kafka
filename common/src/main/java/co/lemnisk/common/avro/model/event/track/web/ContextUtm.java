/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package co.lemnisk.common.avro.model.event.track.web;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class ContextUtm extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 2389906919249233112L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"ContextUtm\",\"namespace\":\"co.lemnisk.common.avro.model.event.track.web\",\"fields\":[{\"name\":\"campaign\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"source\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"medium\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"term\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"content\",\"type\":\"string\",\"default\":\"\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<ContextUtm> ENCODER =
      new BinaryMessageEncoder<ContextUtm>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<ContextUtm> DECODER =
      new BinaryMessageDecoder<ContextUtm>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<ContextUtm> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<ContextUtm> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<ContextUtm>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this ContextUtm to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a ContextUtm from a ByteBuffer. */
  public static ContextUtm fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.CharSequence campaign;
  @Deprecated public java.lang.CharSequence source;
  @Deprecated public java.lang.CharSequence medium;
  @Deprecated public java.lang.CharSequence term;
  @Deprecated public java.lang.CharSequence content;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public ContextUtm() {}

  /**
   * All-args constructor.
   * @param campaign The new value for campaign
   * @param source The new value for source
   * @param medium The new value for medium
   * @param term The new value for term
   * @param content The new value for content
   */
  public ContextUtm(java.lang.CharSequence campaign, java.lang.CharSequence source, java.lang.CharSequence medium, java.lang.CharSequence term, java.lang.CharSequence content) {
    this.campaign = campaign;
    this.source = source;
    this.medium = medium;
    this.term = term;
    this.content = content;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return campaign;
    case 1: return source;
    case 2: return medium;
    case 3: return term;
    case 4: return content;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: campaign = (java.lang.CharSequence)value$; break;
    case 1: source = (java.lang.CharSequence)value$; break;
    case 2: medium = (java.lang.CharSequence)value$; break;
    case 3: term = (java.lang.CharSequence)value$; break;
    case 4: content = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'campaign' field.
   * @return The value of the 'campaign' field.
   */
  public java.lang.CharSequence getCampaign() {
    return campaign;
  }

  /**
   * Sets the value of the 'campaign' field.
   * @param value the value to set.
   */
  public void setCampaign(java.lang.CharSequence value) {
    this.campaign = value;
  }

  /**
   * Gets the value of the 'source' field.
   * @return The value of the 'source' field.
   */
  public java.lang.CharSequence getSource() {
    return source;
  }

  /**
   * Sets the value of the 'source' field.
   * @param value the value to set.
   */
  public void setSource(java.lang.CharSequence value) {
    this.source = value;
  }

  /**
   * Gets the value of the 'medium' field.
   * @return The value of the 'medium' field.
   */
  public java.lang.CharSequence getMedium() {
    return medium;
  }

  /**
   * Sets the value of the 'medium' field.
   * @param value the value to set.
   */
  public void setMedium(java.lang.CharSequence value) {
    this.medium = value;
  }

  /**
   * Gets the value of the 'term' field.
   * @return The value of the 'term' field.
   */
  public java.lang.CharSequence getTerm() {
    return term;
  }

  /**
   * Sets the value of the 'term' field.
   * @param value the value to set.
   */
  public void setTerm(java.lang.CharSequence value) {
    this.term = value;
  }

  /**
   * Gets the value of the 'content' field.
   * @return The value of the 'content' field.
   */
  public java.lang.CharSequence getContent() {
    return content;
  }

  /**
   * Sets the value of the 'content' field.
   * @param value the value to set.
   */
  public void setContent(java.lang.CharSequence value) {
    this.content = value;
  }

  /**
   * Creates a new ContextUtm RecordBuilder.
   * @return A new ContextUtm RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder newBuilder() {
    return new co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder();
  }

  /**
   * Creates a new ContextUtm RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new ContextUtm RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder newBuilder(co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder other) {
    return new co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder(other);
  }

  /**
   * Creates a new ContextUtm RecordBuilder by copying an existing ContextUtm instance.
   * @param other The existing instance to copy.
   * @return A new ContextUtm RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder newBuilder(co.lemnisk.common.avro.model.event.track.web.ContextUtm other) {
    return new co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder(other);
  }

  /**
   * RecordBuilder for ContextUtm instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<ContextUtm>
    implements org.apache.avro.data.RecordBuilder<ContextUtm> {

    private java.lang.CharSequence campaign;
    private java.lang.CharSequence source;
    private java.lang.CharSequence medium;
    private java.lang.CharSequence term;
    private java.lang.CharSequence content;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.campaign)) {
        this.campaign = data().deepCopy(fields()[0].schema(), other.campaign);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.source)) {
        this.source = data().deepCopy(fields()[1].schema(), other.source);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.medium)) {
        this.medium = data().deepCopy(fields()[2].schema(), other.medium);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.term)) {
        this.term = data().deepCopy(fields()[3].schema(), other.term);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.content)) {
        this.content = data().deepCopy(fields()[4].schema(), other.content);
        fieldSetFlags()[4] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing ContextUtm instance
     * @param other The existing instance to copy.
     */
    private Builder(co.lemnisk.common.avro.model.event.track.web.ContextUtm other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.campaign)) {
        this.campaign = data().deepCopy(fields()[0].schema(), other.campaign);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.source)) {
        this.source = data().deepCopy(fields()[1].schema(), other.source);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.medium)) {
        this.medium = data().deepCopy(fields()[2].schema(), other.medium);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.term)) {
        this.term = data().deepCopy(fields()[3].schema(), other.term);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.content)) {
        this.content = data().deepCopy(fields()[4].schema(), other.content);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'campaign' field.
      * @return The value.
      */
    public java.lang.CharSequence getCampaign() {
      return campaign;
    }

    /**
      * Sets the value of the 'campaign' field.
      * @param value The value of 'campaign'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder setCampaign(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.campaign = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'campaign' field has been set.
      * @return True if the 'campaign' field has been set, false otherwise.
      */
    public boolean hasCampaign() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'campaign' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder clearCampaign() {
      campaign = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'source' field.
      * @return The value.
      */
    public java.lang.CharSequence getSource() {
      return source;
    }

    /**
      * Sets the value of the 'source' field.
      * @param value The value of 'source'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder setSource(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.source = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'source' field has been set.
      * @return True if the 'source' field has been set, false otherwise.
      */
    public boolean hasSource() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'source' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder clearSource() {
      source = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'medium' field.
      * @return The value.
      */
    public java.lang.CharSequence getMedium() {
      return medium;
    }

    /**
      * Sets the value of the 'medium' field.
      * @param value The value of 'medium'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder setMedium(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.medium = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'medium' field has been set.
      * @return True if the 'medium' field has been set, false otherwise.
      */
    public boolean hasMedium() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'medium' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder clearMedium() {
      medium = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'term' field.
      * @return The value.
      */
    public java.lang.CharSequence getTerm() {
      return term;
    }

    /**
      * Sets the value of the 'term' field.
      * @param value The value of 'term'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder setTerm(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.term = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'term' field has been set.
      * @return True if the 'term' field has been set, false otherwise.
      */
    public boolean hasTerm() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'term' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder clearTerm() {
      term = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'content' field.
      * @return The value.
      */
    public java.lang.CharSequence getContent() {
      return content;
    }

    /**
      * Sets the value of the 'content' field.
      * @param value The value of 'content'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder setContent(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.content = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'content' field has been set.
      * @return True if the 'content' field has been set, false otherwise.
      */
    public boolean hasContent() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'content' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.track.web.ContextUtm.Builder clearContent() {
      content = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ContextUtm build() {
      try {
        ContextUtm record = new ContextUtm();
        record.campaign = fieldSetFlags()[0] ? this.campaign : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.source = fieldSetFlags()[1] ? this.source : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.medium = fieldSetFlags()[2] ? this.medium : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.term = fieldSetFlags()[3] ? this.term : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.content = fieldSetFlags()[4] ? this.content : (java.lang.CharSequence) defaultValue(fields()[4]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<ContextUtm>
    WRITER$ = (org.apache.avro.io.DatumWriter<ContextUtm>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<ContextUtm>
    READER$ = (org.apache.avro.io.DatumReader<ContextUtm>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
