package co.lemnisk.transform.dmpsstdata.builder;

import co.lemnisk.common.constants.EventType;

import javax.lang.model.type.NullType;
import java.util.HashMap;

public class DmpSstDataUnknownTypeBuilder extends DmpSstDataBuilder<NullType> {

    public DmpSstDataUnknownTypeBuilder(HashMap<String, String> payload, String rawData) {
        super(payload, rawData);
    }

    @Override
    public String eventType() {
        return EventType.UNKNOWN;
    }

    @Override
    public NullType getData() {
        return null;
    }

    @Override
    public void constructData() {
    }

}
