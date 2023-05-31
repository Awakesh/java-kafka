/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package co.lemnisk.common.avro.model.event.identify.app;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Context extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -529082071438783423L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Context\",\"namespace\":\"co.lemnisk.common.avro.model.event.identify.app\",\"fields\":[{\"name\":\"library\",\"type\":{\"type\":\"record\",\"name\":\"ContextLibrary\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"version\",\"type\":\"string\",\"default\":\"\"}]}},{\"name\":\"app\",\"type\":{\"type\":\"record\",\"name\":\"ContextApp\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"version\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"build\",\"type\":\"string\",\"default\":\"\"}]}},{\"name\":\"device\",\"type\":{\"type\":\"record\",\"name\":\"ContextDevice\",\"fields\":[{\"name\":\"id\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"advertisingId\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"adTrackingEnabled\",\"type\":\"boolean\"},{\"name\":\"manufacturer\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"model\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"type\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"token\",\"type\":\"string\",\"default\":\"\"}]}},{\"name\":\"screen\",\"type\":{\"type\":\"record\",\"name\":\"ContextScreen\",\"fields\":[{\"name\":\"width\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"height\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"density\",\"type\":\"string\",\"default\":\"\"}]}},{\"name\":\"userAgent\",\"type\":{\"type\":\"record\",\"name\":\"ContextUserAgent\",\"fields\":[{\"name\":\"osType\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"osVersion\",\"type\":\"string\",\"default\":\"\"}]}},{\"name\":\"ip\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"srcId\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"accountId\",\"type\":\"string\",\"default\":\"\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Context> ENCODER =
      new BinaryMessageEncoder<Context>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Context> DECODER =
      new BinaryMessageDecoder<Context>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<Context> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<Context> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Context>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this Context to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a Context from a ByteBuffer. */
  public static Context fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public co.lemnisk.common.avro.model.event.identify.app.ContextLibrary library;
  @Deprecated public co.lemnisk.common.avro.model.event.identify.app.ContextApp app;
  @Deprecated public co.lemnisk.common.avro.model.event.identify.app.ContextDevice device;
  @Deprecated public co.lemnisk.common.avro.model.event.identify.app.ContextScreen screen;
  @Deprecated public co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent userAgent;
  @Deprecated public java.lang.CharSequence ip;
  @Deprecated public java.lang.CharSequence srcId;
  @Deprecated public java.lang.CharSequence accountId;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Context() {}

  /**
   * All-args constructor.
   * @param library The new value for library
   * @param app The new value for app
   * @param device The new value for device
   * @param screen The new value for screen
   * @param userAgent The new value for userAgent
   * @param ip The new value for ip
   * @param srcId The new value for srcId
   * @param accountId The new value for accountId
   */
  public Context(co.lemnisk.common.avro.model.event.identify.app.ContextLibrary library, co.lemnisk.common.avro.model.event.identify.app.ContextApp app, co.lemnisk.common.avro.model.event.identify.app.ContextDevice device, co.lemnisk.common.avro.model.event.identify.app.ContextScreen screen, co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent userAgent, java.lang.CharSequence ip, java.lang.CharSequence srcId, java.lang.CharSequence accountId) {
    this.library = library;
    this.app = app;
    this.device = device;
    this.screen = screen;
    this.userAgent = userAgent;
    this.ip = ip;
    this.srcId = srcId;
    this.accountId = accountId;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return library;
    case 1: return app;
    case 2: return device;
    case 3: return screen;
    case 4: return userAgent;
    case 5: return ip;
    case 6: return srcId;
    case 7: return accountId;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: library = (co.lemnisk.common.avro.model.event.identify.app.ContextLibrary)value$; break;
    case 1: app = (co.lemnisk.common.avro.model.event.identify.app.ContextApp)value$; break;
    case 2: device = (co.lemnisk.common.avro.model.event.identify.app.ContextDevice)value$; break;
    case 3: screen = (co.lemnisk.common.avro.model.event.identify.app.ContextScreen)value$; break;
    case 4: userAgent = (co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent)value$; break;
    case 5: ip = (java.lang.CharSequence)value$; break;
    case 6: srcId = (java.lang.CharSequence)value$; break;
    case 7: accountId = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'library' field.
   * @return The value of the 'library' field.
   */
  public co.lemnisk.common.avro.model.event.identify.app.ContextLibrary getLibrary() {
    return library;
  }

  /**
   * Sets the value of the 'library' field.
   * @param value the value to set.
   */
  public void setLibrary(co.lemnisk.common.avro.model.event.identify.app.ContextLibrary value) {
    this.library = value;
  }

  /**
   * Gets the value of the 'app' field.
   * @return The value of the 'app' field.
   */
  public co.lemnisk.common.avro.model.event.identify.app.ContextApp getApp() {
    return app;
  }

  /**
   * Sets the value of the 'app' field.
   * @param value the value to set.
   */
  public void setApp(co.lemnisk.common.avro.model.event.identify.app.ContextApp value) {
    this.app = value;
  }

  /**
   * Gets the value of the 'device' field.
   * @return The value of the 'device' field.
   */
  public co.lemnisk.common.avro.model.event.identify.app.ContextDevice getDevice() {
    return device;
  }

  /**
   * Sets the value of the 'device' field.
   * @param value the value to set.
   */
  public void setDevice(co.lemnisk.common.avro.model.event.identify.app.ContextDevice value) {
    this.device = value;
  }

  /**
   * Gets the value of the 'screen' field.
   * @return The value of the 'screen' field.
   */
  public co.lemnisk.common.avro.model.event.identify.app.ContextScreen getScreen() {
    return screen;
  }

  /**
   * Sets the value of the 'screen' field.
   * @param value the value to set.
   */
  public void setScreen(co.lemnisk.common.avro.model.event.identify.app.ContextScreen value) {
    this.screen = value;
  }

  /**
   * Gets the value of the 'userAgent' field.
   * @return The value of the 'userAgent' field.
   */
  public co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent getUserAgent() {
    return userAgent;
  }

  /**
   * Sets the value of the 'userAgent' field.
   * @param value the value to set.
   */
  public void setUserAgent(co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent value) {
    this.userAgent = value;
  }

  /**
   * Gets the value of the 'ip' field.
   * @return The value of the 'ip' field.
   */
  public java.lang.CharSequence getIp() {
    return ip;
  }

  /**
   * Sets the value of the 'ip' field.
   * @param value the value to set.
   */
  public void setIp(java.lang.CharSequence value) {
    this.ip = value;
  }

  /**
   * Gets the value of the 'srcId' field.
   * @return The value of the 'srcId' field.
   */
  public java.lang.CharSequence getSrcId() {
    return srcId;
  }

  /**
   * Sets the value of the 'srcId' field.
   * @param value the value to set.
   */
  public void setSrcId(java.lang.CharSequence value) {
    this.srcId = value;
  }

  /**
   * Gets the value of the 'accountId' field.
   * @return The value of the 'accountId' field.
   */
  public java.lang.CharSequence getAccountId() {
    return accountId;
  }

  /**
   * Sets the value of the 'accountId' field.
   * @param value the value to set.
   */
  public void setAccountId(java.lang.CharSequence value) {
    this.accountId = value;
  }

  /**
   * Creates a new Context RecordBuilder.
   * @return A new Context RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.identify.app.Context.Builder newBuilder() {
    return new co.lemnisk.common.avro.model.event.identify.app.Context.Builder();
  }

  /**
   * Creates a new Context RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Context RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.identify.app.Context.Builder newBuilder(co.lemnisk.common.avro.model.event.identify.app.Context.Builder other) {
    return new co.lemnisk.common.avro.model.event.identify.app.Context.Builder(other);
  }

  /**
   * Creates a new Context RecordBuilder by copying an existing Context instance.
   * @param other The existing instance to copy.
   * @return A new Context RecordBuilder
   */
  public static co.lemnisk.common.avro.model.event.identify.app.Context.Builder newBuilder(co.lemnisk.common.avro.model.event.identify.app.Context other) {
    return new co.lemnisk.common.avro.model.event.identify.app.Context.Builder(other);
  }

  /**
   * RecordBuilder for Context instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Context>
    implements org.apache.avro.data.RecordBuilder<Context> {

    private co.lemnisk.common.avro.model.event.identify.app.ContextLibrary library;
    private co.lemnisk.common.avro.model.event.identify.app.ContextLibrary.Builder libraryBuilder;
    private co.lemnisk.common.avro.model.event.identify.app.ContextApp app;
    private co.lemnisk.common.avro.model.event.identify.app.ContextApp.Builder appBuilder;
    private co.lemnisk.common.avro.model.event.identify.app.ContextDevice device;
    private co.lemnisk.common.avro.model.event.identify.app.ContextDevice.Builder deviceBuilder;
    private co.lemnisk.common.avro.model.event.identify.app.ContextScreen screen;
    private co.lemnisk.common.avro.model.event.identify.app.ContextScreen.Builder screenBuilder;
    private co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent userAgent;
    private co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent.Builder userAgentBuilder;
    private java.lang.CharSequence ip;
    private java.lang.CharSequence srcId;
    private java.lang.CharSequence accountId;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(co.lemnisk.common.avro.model.event.identify.app.Context.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.library)) {
        this.library = data().deepCopy(fields()[0].schema(), other.library);
        fieldSetFlags()[0] = true;
      }
      if (other.hasLibraryBuilder()) {
        this.libraryBuilder = co.lemnisk.common.avro.model.event.identify.app.ContextLibrary.newBuilder(other.getLibraryBuilder());
      }
      if (isValidValue(fields()[1], other.app)) {
        this.app = data().deepCopy(fields()[1].schema(), other.app);
        fieldSetFlags()[1] = true;
      }
      if (other.hasAppBuilder()) {
        this.appBuilder = co.lemnisk.common.avro.model.event.identify.app.ContextApp.newBuilder(other.getAppBuilder());
      }
      if (isValidValue(fields()[2], other.device)) {
        this.device = data().deepCopy(fields()[2].schema(), other.device);
        fieldSetFlags()[2] = true;
      }
      if (other.hasDeviceBuilder()) {
        this.deviceBuilder = co.lemnisk.common.avro.model.event.identify.app.ContextDevice.newBuilder(other.getDeviceBuilder());
      }
      if (isValidValue(fields()[3], other.screen)) {
        this.screen = data().deepCopy(fields()[3].schema(), other.screen);
        fieldSetFlags()[3] = true;
      }
      if (other.hasScreenBuilder()) {
        this.screenBuilder = co.lemnisk.common.avro.model.event.identify.app.ContextScreen.newBuilder(other.getScreenBuilder());
      }
      if (isValidValue(fields()[4], other.userAgent)) {
        this.userAgent = data().deepCopy(fields()[4].schema(), other.userAgent);
        fieldSetFlags()[4] = true;
      }
      if (other.hasUserAgentBuilder()) {
        this.userAgentBuilder = co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent.newBuilder(other.getUserAgentBuilder());
      }
      if (isValidValue(fields()[5], other.ip)) {
        this.ip = data().deepCopy(fields()[5].schema(), other.ip);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.srcId)) {
        this.srcId = data().deepCopy(fields()[6].schema(), other.srcId);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.accountId)) {
        this.accountId = data().deepCopy(fields()[7].schema(), other.accountId);
        fieldSetFlags()[7] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing Context instance
     * @param other The existing instance to copy.
     */
    private Builder(co.lemnisk.common.avro.model.event.identify.app.Context other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.library)) {
        this.library = data().deepCopy(fields()[0].schema(), other.library);
        fieldSetFlags()[0] = true;
      }
      this.libraryBuilder = null;
      if (isValidValue(fields()[1], other.app)) {
        this.app = data().deepCopy(fields()[1].schema(), other.app);
        fieldSetFlags()[1] = true;
      }
      this.appBuilder = null;
      if (isValidValue(fields()[2], other.device)) {
        this.device = data().deepCopy(fields()[2].schema(), other.device);
        fieldSetFlags()[2] = true;
      }
      this.deviceBuilder = null;
      if (isValidValue(fields()[3], other.screen)) {
        this.screen = data().deepCopy(fields()[3].schema(), other.screen);
        fieldSetFlags()[3] = true;
      }
      this.screenBuilder = null;
      if (isValidValue(fields()[4], other.userAgent)) {
        this.userAgent = data().deepCopy(fields()[4].schema(), other.userAgent);
        fieldSetFlags()[4] = true;
      }
      this.userAgentBuilder = null;
      if (isValidValue(fields()[5], other.ip)) {
        this.ip = data().deepCopy(fields()[5].schema(), other.ip);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.srcId)) {
        this.srcId = data().deepCopy(fields()[6].schema(), other.srcId);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.accountId)) {
        this.accountId = data().deepCopy(fields()[7].schema(), other.accountId);
        fieldSetFlags()[7] = true;
      }
    }

    /**
      * Gets the value of the 'library' field.
      * @return The value.
      */
    public co.lemnisk.common.avro.model.event.identify.app.ContextLibrary getLibrary() {
      return library;
    }

    /**
      * Sets the value of the 'library' field.
      * @param value The value of 'library'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setLibrary(co.lemnisk.common.avro.model.event.identify.app.ContextLibrary value) {
      validate(fields()[0], value);
      this.libraryBuilder = null;
      this.library = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'library' field has been set.
      * @return True if the 'library' field has been set, false otherwise.
      */
    public boolean hasLibrary() {
      return fieldSetFlags()[0];
    }

    /**
     * Gets the Builder instance for the 'library' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.identify.app.ContextLibrary.Builder getLibraryBuilder() {
      if (libraryBuilder == null) {
        if (hasLibrary()) {
          setLibraryBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextLibrary.newBuilder(library));
        } else {
          setLibraryBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextLibrary.newBuilder());
        }
      }
      return libraryBuilder;
    }

    /**
     * Sets the Builder instance for the 'library' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setLibraryBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextLibrary.Builder value) {
      clearLibrary();
      libraryBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'library' field has an active Builder instance
     * @return True if the 'library' field has an active Builder instance
     */
    public boolean hasLibraryBuilder() {
      return libraryBuilder != null;
    }

    /**
      * Clears the value of the 'library' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder clearLibrary() {
      library = null;
      libraryBuilder = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'app' field.
      * @return The value.
      */
    public co.lemnisk.common.avro.model.event.identify.app.ContextApp getApp() {
      return app;
    }

    /**
      * Sets the value of the 'app' field.
      * @param value The value of 'app'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setApp(co.lemnisk.common.avro.model.event.identify.app.ContextApp value) {
      validate(fields()[1], value);
      this.appBuilder = null;
      this.app = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'app' field has been set.
      * @return True if the 'app' field has been set, false otherwise.
      */
    public boolean hasApp() {
      return fieldSetFlags()[1];
    }

    /**
     * Gets the Builder instance for the 'app' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.identify.app.ContextApp.Builder getAppBuilder() {
      if (appBuilder == null) {
        if (hasApp()) {
          setAppBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextApp.newBuilder(app));
        } else {
          setAppBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextApp.newBuilder());
        }
      }
      return appBuilder;
    }

    /**
     * Sets the Builder instance for the 'app' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setAppBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextApp.Builder value) {
      clearApp();
      appBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'app' field has an active Builder instance
     * @return True if the 'app' field has an active Builder instance
     */
    public boolean hasAppBuilder() {
      return appBuilder != null;
    }

    /**
      * Clears the value of the 'app' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder clearApp() {
      app = null;
      appBuilder = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'device' field.
      * @return The value.
      */
    public co.lemnisk.common.avro.model.event.identify.app.ContextDevice getDevice() {
      return device;
    }

    /**
      * Sets the value of the 'device' field.
      * @param value The value of 'device'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setDevice(co.lemnisk.common.avro.model.event.identify.app.ContextDevice value) {
      validate(fields()[2], value);
      this.deviceBuilder = null;
      this.device = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'device' field has been set.
      * @return True if the 'device' field has been set, false otherwise.
      */
    public boolean hasDevice() {
      return fieldSetFlags()[2];
    }

    /**
     * Gets the Builder instance for the 'device' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.identify.app.ContextDevice.Builder getDeviceBuilder() {
      if (deviceBuilder == null) {
        if (hasDevice()) {
          setDeviceBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextDevice.newBuilder(device));
        } else {
          setDeviceBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextDevice.newBuilder());
        }
      }
      return deviceBuilder;
    }

    /**
     * Sets the Builder instance for the 'device' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setDeviceBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextDevice.Builder value) {
      clearDevice();
      deviceBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'device' field has an active Builder instance
     * @return True if the 'device' field has an active Builder instance
     */
    public boolean hasDeviceBuilder() {
      return deviceBuilder != null;
    }

    /**
      * Clears the value of the 'device' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder clearDevice() {
      device = null;
      deviceBuilder = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'screen' field.
      * @return The value.
      */
    public co.lemnisk.common.avro.model.event.identify.app.ContextScreen getScreen() {
      return screen;
    }

    /**
      * Sets the value of the 'screen' field.
      * @param value The value of 'screen'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setScreen(co.lemnisk.common.avro.model.event.identify.app.ContextScreen value) {
      validate(fields()[3], value);
      this.screenBuilder = null;
      this.screen = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'screen' field has been set.
      * @return True if the 'screen' field has been set, false otherwise.
      */
    public boolean hasScreen() {
      return fieldSetFlags()[3];
    }

    /**
     * Gets the Builder instance for the 'screen' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.identify.app.ContextScreen.Builder getScreenBuilder() {
      if (screenBuilder == null) {
        if (hasScreen()) {
          setScreenBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextScreen.newBuilder(screen));
        } else {
          setScreenBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextScreen.newBuilder());
        }
      }
      return screenBuilder;
    }

    /**
     * Sets the Builder instance for the 'screen' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setScreenBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextScreen.Builder value) {
      clearScreen();
      screenBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'screen' field has an active Builder instance
     * @return True if the 'screen' field has an active Builder instance
     */
    public boolean hasScreenBuilder() {
      return screenBuilder != null;
    }

    /**
      * Clears the value of the 'screen' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder clearScreen() {
      screen = null;
      screenBuilder = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'userAgent' field.
      * @return The value.
      */
    public co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent getUserAgent() {
      return userAgent;
    }

    /**
      * Sets the value of the 'userAgent' field.
      * @param value The value of 'userAgent'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setUserAgent(co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent value) {
      validate(fields()[4], value);
      this.userAgentBuilder = null;
      this.userAgent = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'userAgent' field has been set.
      * @return True if the 'userAgent' field has been set, false otherwise.
      */
    public boolean hasUserAgent() {
      return fieldSetFlags()[4];
    }

    /**
     * Gets the Builder instance for the 'userAgent' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent.Builder getUserAgentBuilder() {
      if (userAgentBuilder == null) {
        if (hasUserAgent()) {
          setUserAgentBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent.newBuilder(userAgent));
        } else {
          setUserAgentBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent.newBuilder());
        }
      }
      return userAgentBuilder;
    }

    /**
     * Sets the Builder instance for the 'userAgent' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setUserAgentBuilder(co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent.Builder value) {
      clearUserAgent();
      userAgentBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'userAgent' field has an active Builder instance
     * @return True if the 'userAgent' field has an active Builder instance
     */
    public boolean hasUserAgentBuilder() {
      return userAgentBuilder != null;
    }

    /**
      * Clears the value of the 'userAgent' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder clearUserAgent() {
      userAgent = null;
      userAgentBuilder = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'ip' field.
      * @return The value.
      */
    public java.lang.CharSequence getIp() {
      return ip;
    }

    /**
      * Sets the value of the 'ip' field.
      * @param value The value of 'ip'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setIp(java.lang.CharSequence value) {
      validate(fields()[5], value);
      this.ip = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'ip' field has been set.
      * @return True if the 'ip' field has been set, false otherwise.
      */
    public boolean hasIp() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'ip' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder clearIp() {
      ip = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'srcId' field.
      * @return The value.
      */
    public java.lang.CharSequence getSrcId() {
      return srcId;
    }

    /**
      * Sets the value of the 'srcId' field.
      * @param value The value of 'srcId'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setSrcId(java.lang.CharSequence value) {
      validate(fields()[6], value);
      this.srcId = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
      * Checks whether the 'srcId' field has been set.
      * @return True if the 'srcId' field has been set, false otherwise.
      */
    public boolean hasSrcId() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'srcId' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder clearSrcId() {
      srcId = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /**
      * Gets the value of the 'accountId' field.
      * @return The value.
      */
    public java.lang.CharSequence getAccountId() {
      return accountId;
    }

    /**
      * Sets the value of the 'accountId' field.
      * @param value The value of 'accountId'.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder setAccountId(java.lang.CharSequence value) {
      validate(fields()[7], value);
      this.accountId = value;
      fieldSetFlags()[7] = true;
      return this;
    }

    /**
      * Checks whether the 'accountId' field has been set.
      * @return True if the 'accountId' field has been set, false otherwise.
      */
    public boolean hasAccountId() {
      return fieldSetFlags()[7];
    }


    /**
      * Clears the value of the 'accountId' field.
      * @return This builder.
      */
    public co.lemnisk.common.avro.model.event.identify.app.Context.Builder clearAccountId() {
      accountId = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Context build() {
      try {
        Context record = new Context();
        if (libraryBuilder != null) {
          record.library = this.libraryBuilder.build();
        } else {
          record.library = fieldSetFlags()[0] ? this.library : (co.lemnisk.common.avro.model.event.identify.app.ContextLibrary) defaultValue(fields()[0]);
        }
        if (appBuilder != null) {
          record.app = this.appBuilder.build();
        } else {
          record.app = fieldSetFlags()[1] ? this.app : (co.lemnisk.common.avro.model.event.identify.app.ContextApp) defaultValue(fields()[1]);
        }
        if (deviceBuilder != null) {
          record.device = this.deviceBuilder.build();
        } else {
          record.device = fieldSetFlags()[2] ? this.device : (co.lemnisk.common.avro.model.event.identify.app.ContextDevice) defaultValue(fields()[2]);
        }
        if (screenBuilder != null) {
          record.screen = this.screenBuilder.build();
        } else {
          record.screen = fieldSetFlags()[3] ? this.screen : (co.lemnisk.common.avro.model.event.identify.app.ContextScreen) defaultValue(fields()[3]);
        }
        if (userAgentBuilder != null) {
          record.userAgent = this.userAgentBuilder.build();
        } else {
          record.userAgent = fieldSetFlags()[4] ? this.userAgent : (co.lemnisk.common.avro.model.event.identify.app.ContextUserAgent) defaultValue(fields()[4]);
        }
        record.ip = fieldSetFlags()[5] ? this.ip : (java.lang.CharSequence) defaultValue(fields()[5]);
        record.srcId = fieldSetFlags()[6] ? this.srcId : (java.lang.CharSequence) defaultValue(fields()[6]);
        record.accountId = fieldSetFlags()[7] ? this.accountId : (java.lang.CharSequence) defaultValue(fields()[7]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Context>
    WRITER$ = (org.apache.avro.io.DatumWriter<Context>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Context>
    READER$ = (org.apache.avro.io.DatumReader<Context>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}