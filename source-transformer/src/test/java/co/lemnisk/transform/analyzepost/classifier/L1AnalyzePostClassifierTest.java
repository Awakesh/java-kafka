package co.lemnisk.transform.analyzepost.classifier;

import co.lemnisk.common.Util;
//import co.lemnisk.transform.analyzepost.model.AnalyzePostDataFormat;
import co.lemnisk.common.constants.AnalyzePostDataFormat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class L1AnalyzePostClassifierTest {

    private String inputData;
    private String inputData2;
    private String inputData3;

    @BeforeAll
    void constructData() throws IOException {
        String rawData = getRawData();
        String rawData2 = getRawData2();
        String rawData3 = getRawData3();
        inputData =  L1AnalyzePostClassifier.getDataType(rawData);
        inputData2 =  L1AnalyzePostClassifier.getDataType(rawData2);
        inputData3 =  L1AnalyzePostClassifier.getDataType(rawData3);
    }

    private String getRawData() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/classifier/v1type.txt");
        return Util.readFileAsString(file);
    }

    private String getRawData2() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/classifier/v2type.txt");
        return Util.readFileAsString(file);
    }

    private String getRawData3() throws IOException {
        File file = Util.getFile("/fixtures/analyze_post/classifier/v3type.txt");
        return Util.readFileAsString(file);
    }

    @Test
    @DisplayName("Validate EventType Data")
    void validateEventTypeData() {

        assertEquals(AnalyzePostDataFormat.V1 , inputData);
        assertEquals(AnalyzePostDataFormat.V2 , inputData2);
        assertEquals(AnalyzePostDataFormat.V3, inputData3);

    }


}