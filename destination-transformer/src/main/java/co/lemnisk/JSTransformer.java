package co.lemnisk;
 import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
 import org.graalvm.polyglot.Context;
 import org.graalvm.polyglot.HostAccess;
import org.springframework.stereotype.Component;

import javax.script.*;
 import java.util.concurrent.ConcurrentHashMap;

@Component
public class JSTransformer {

    private static final String parseFunction =
            "function parseAndTransformEvents(inputJsonStr){" +
                    "let parsedEventData = JSON.parse(inputJsonStr); " +
                    "parsedEventData[\"transformedPayload\"] = JSON.stringify(transformEvents(parsedEventData));" +
                    "return parsedEventData;" +
            "}";

    private static ConcurrentHashMap<String, ScriptEngine> scriptEnginesEntries = new ConcurrentHashMap<>();

    public Object transformEvent(String jsonStr, String transformerFunction) throws ScriptException, NoSuchMethodException {
        var scriptEngine = getScriptEngine(Thread.currentThread().getName());
        scriptEngine.eval(parseFunction);
        scriptEngine.eval(transformerFunction);
        Invocable inv  = (Invocable) scriptEngine;
        Object transformedEvent = inv.invokeFunction("parseAndTransformEvents", jsonStr);
        return transformedEvent;
    }

    private ScriptEngine getScriptEngine(String threadName) {
        if (scriptEnginesEntries.containsKey(threadName)) {
            return scriptEnginesEntries.get(threadName);
        }
        else {
            var newScriptEngine = GraalJSScriptEngine.create(null,
                    Context.newBuilder("js")
                            .allowHostAccess(HostAccess.ALL)
                            .allowHostClassLookup(s -> true)
                            .option("js.ecmascript-version", "2020"));
            scriptEnginesEntries.put(threadName, newScriptEngine);
            return newScriptEngine;
        }
    }

}
