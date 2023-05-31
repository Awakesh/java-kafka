package co.lemnisk.consumer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

public class MacroParser {
    static void mapAppender(Map<String, String> result, Map.Entry<String, JsonNode> node, List<String> names) {
        names.add(node.getKey());
        if (node.getValue().isTextual()) {
            String name = names.stream().collect(joining("__")).toUpperCase(Locale.ROOT);
            result.put(name, node.getValue().asText());
        } else {
            node.getValue().fields()
                    .forEachRemaining(nested -> mapAppender(result, nested, new ArrayList<>(names)));
        }
    }

    public static String substituteVariables(String template, Map<String, String> variables) {
        Pattern pattern = Pattern.compile(Constants.MACROS_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(template);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            if (variables.containsKey(matcher.group(1).toUpperCase(Locale.ROOT))) {
                String replacement = variables.get(matcher.group(1).toUpperCase(Locale.ROOT));
                matcher.appendReplacement(buffer, replacement != null ? Matcher.quoteReplacement(replacement) : "null");
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static String parse(String configJson, String originalMacroStr) throws JsonProcessingException {
        Map<String, String> result = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.readTree(configJson)
                .fields()
                .forEachRemaining(node -> mapAppender(result, node, new ArrayList<>()));

        return substituteVariables(originalMacroStr, result);
    }
}
