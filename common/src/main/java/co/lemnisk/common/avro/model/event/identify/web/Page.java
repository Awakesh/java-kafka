/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package co.lemnisk.common.avro.model.event.identify.web;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Page extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 5613382117857567968L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Page\",\"namespace\":\"co.lemnisk.common.avro.model.event.identify.web\",\"fields\":[{\"name\":\"path\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"referrer\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"search\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"title\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"url\",\"type\":\"string\",\"default\":\"\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Page> ENCODER =
      new BinaryMessageEncoder<Page>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Page> DECODER =
      new BinaryMessageDecoder<Page>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<Page> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<Page> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Page>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this Page to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a Page from a ByteBuffer. */
  public static Page fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.CharSequence path;
  @Deprecated public java.lang.CharSequence referrer;
  @Deprecated public java.lang.CharSequence search;
  @Deprecated public java.lang.CharSequence title;
  @Deprecated public java.lang.CharSequence url;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Page() {}

  /**
   * All-args constructor.
   * @param path The new value for path
   * @param referrer The new value for referrer
   * @param search The new value for search
   * @param title The new value for title
   * @param url The new value for url
   */
  public Page(java.lang.CharSequence path, java.lang.CharSequence referrer, java.lang.CharSequence search, java.lang.CharSequence title, java.lang.CharSequence url) {
    this.path = path;
    this.referrer = referrer;
    this.search = search;
    this.title = title;
    this.url = url;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return path;
    case 1: return referrer;
    case 2: return search;
    case 3: return title;
    case 4: return url;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: path = (java.lang.CharSequence)value$; break;
    case 1: referrer = (java.lang.CharSequence)value$; break;
    case 2: search = (java.lang.CharSequence)value$; break;
    case 3: title = (java.lang.CharSequence)value$; break;
    case 4: url = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'path' field.
   * @return The value of the 'path' field.
   */
  public java.lang.CharSequence getPath() {
    return path;
  }

  /**
   * Sets the value of the 'path' field.
   * @param value the value to set.
   */
  public void setPath(java.lang.CharSequence value) {
    this.path = value;
  }

  /**
   * Gets the value of the 'referrer' field.
   * @return The value of the 'referrer' field.
   */
  public java.lang.CharSequence getReferrer() {
    return referrer;
  }

  /**
   * Sets the value of the 'referrer' field.
   * @param value the value to set.
   */
  public void setReferrer(java.lang.CharSequence value) {
    this.referrer = value;
  }

  /**
   * Gets the value of the 'search' field.
   * @return The value of the 'search' field.
   */
  public java.lang.CharSequence getSearch() {
    return search;
  }

  /**
   * Sets the value of the 'search' field.
   * @param value the value to set.
   */
  public void setSearch(java.lang.CharSequence value) {
    this.search = value;
  }

  /**
   * Gets the value of the 'title' field.
   * @return The value of the 'title' field.
   */
  public java.lang.CharSequence getTitle() {
    return title;
  }

  /**
   * Sets the value of the 'title' field.
   * @param value the value to set.
   */
  public void setTitle(java.lang.CharSequence value) {
    this.title = value;
  }

  /**
   * Gets the value of the 'url' field.
   * @return The value of the 'url' field.
   */
  public java.lang.CharSequence getUrl() {
    return url;
  }

  /**
   * Sets the value of the 'url' field.
   * @param value the value to set.
   */
  public void setUrl(java.lang.CharSequence value) {
    this.url = value;
  }

  /**
   * Creates a new Page RecordBuilder.
   * @return A new Page RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.identify.web.Page.Builder newBuilder() {
    return new co.lemnisk.common.avro.model.event.identify.web.Page.Builder();
  }

  /**
   * Creates a new Page RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Page RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.identify.web.Page.Builder newBuilder(co.lemnisk.common.avro.model.event.identify.web.Page.Builder other) {
    return new co.lemnisk.common.avro.model.event.identify.web.Page.Builder(other);
  }

  /**
   * Creates a new Page RecordBuilder by copying an existing Page instance.
   * @param other The existing instance to copy.
   * @return A new Page RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.identify.web.Page.Builder newBuilder(co.lemnisk.common.avro.model.event.identify.web.Page other) {
    return new co.lemnisk.common.avro.model.event.identify.web.Page.Builder(other);
  }

  /**
   * RecordBuilder for Page instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Page>
    implements org.apache.avro.data.RecordBuilder<Page> {

    private java.lang.CharSequence path;
    private java.lang.CharSequence referrer;
    private java.lang.CharSequence search;
    private java.lang.CharSequence title;
    private java.lang.CharSequence url;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(co.lemnisk.common.avro.model.event.identify.web.Page.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.path)) {
        this.path = data().deepCopy(fields()[0].schema(), other.path);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.referrer)) {
        this.referrer = data().deepCopy(fields()[1].schema(), other.referrer);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.search)) {
        this.search = data().deepCopy(fields()[2].schema(), other.search);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.title)) {
        this.title = data().deepCopy(fields()[3].schema(), other.title);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.url)) {
        this.url = data().deepCopy(fields()[4].schema(), other.url);
        fieldSetFlags()[4] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing Page instance
     * @param other The existing instance to copy.
     */
    private Builder(co.lemnisk.common.avro.model.event.identify.web.Page other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.path)) {
        this.path = data().deepCopy(fields()[0].schema(), other.path);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.referrer)) {
        this.referrer = data().deepCopy(fields()[1].schema(), other.referrer);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.search)) {
        this.search = data().deepCopy(fields()[2].schema(), other.search);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.title)) {
        this.title = data().deepCopy(fields()[3].schema(), other.title);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.url)) {
        this.url = data().deepCopy(fields()[4].schema(), other.url);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'path' field.
      * @return The value.
      */
    public java.lang.CharSequence getPath() {
      return path;
    }

    /**
      * Sets the value of the 'path' field.
      * @param value The value of 'path'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.web.Page.Builder setPath(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.path = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'path' field has been set.
      * @return True if the 'path' field has been set, false otherwise.
      */
    public boolean hasPath() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'path' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.web.Page.Builder clearPath() {
      path = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'referrer' field.
      * @return The value.
      */
    public java.lang.CharSequence getReferrer() {
      return referrer;
    }

    /**
      * Sets the value of the 'referrer' field.
      * @param value The value of 'referrer'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.web.Page.Builder setReferrer(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.referrer = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'referrer' field has been set.
      * @return True if the 'referrer' field has been set, false otherwise.
      */
    public boolean hasReferrer() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'referrer' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.web.Page.Builder clearReferrer() {
      referrer = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'search' field.
      * @return The value.
      */
    public java.lang.CharSequence getSearch() {
      return search;
    }

    /**
      * Sets the value of the 'search' field.
      * @param value The value of 'search'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.web.Page.Builder setSearch(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.search = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'search' field has been set.
      * @return True if the 'search' field has been set, false otherwise.
      */
    public boolean hasSearch() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'search' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.web.Page.Builder clearSearch() {
      search = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'title' field.
      * @return The value.
      */
    public java.lang.CharSequence getTitle() {
      return title;
    }

    /**
      * Sets the value of the 'title' field.
      * @param value The value of 'title'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.web.Page.Builder setTitle(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.title = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'title' field has been set.
      * @return True if the 'title' field has been set, false otherwise.
      */
    public boolean hasTitle() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'title' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.web.Page.Builder clearTitle() {
      title = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'url' field.
      * @return The value.
      */
    public java.lang.CharSequence getUrl() {
      return url;
    }

    /**
      * Sets the value of the 'url' field.
      * @param value The value of 'url'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.web.Page.Builder setUrl(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.url = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'url' field has been set.
      * @return True if the 'url' field has been set, false otherwise.
      */
    public boolean hasUrl() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'url' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.web.Page.Builder clearUrl() {
      url = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Page build() {
      try {
        Page record = new Page();
        record.path = fieldSetFlags()[0] ? this.path : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.referrer = fieldSetFlags()[1] ? this.referrer : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.search = fieldSetFlags()[2] ? this.search : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.title = fieldSetFlags()[3] ? this.title : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.url = fieldSetFlags()[4] ? this.url : (java.lang.CharSequence) defaultValue(fields()[4]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Page>
    WRITER$ = (org.apache.avro.io.DatumWriter<Page>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Page>
    READER$ = (org.apache.avro.io.DatumReader<Page>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
