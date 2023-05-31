package co.lemnisk.validation;

import co.lemnisk.common.exception.TransformerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidationTest
{

    Validation valid =new Validation();

    @Test
    void whenExceptionThrown_MessageID() {
        Exception exception = assertThrows(TransformerException.class, () -> {
            valid.validate("", "1", "6106", "161467423", "15");
        });
        String actualMessage = exception.getMessage();
        assertEquals("Invalid campaign id",actualMessage);
    }
    @Test
    void whenExceptionThrown_IpAddress() {
        Exception exception = assertThrows(TransformerException.class, () -> {
            valid.validate("2614e9ad-b836-448f-abf1-b983e57e30bc", "", "6106", "16146742394599", "15");
        });
        String actualMessage = exception.getMessage();
        assertEquals("Invalid Ip address",actualMessage);
    }
    @Test
    void whenExceptionThrown_AccountId() {
        Exception exception = assertThrows(TransformerException.class, () -> {
            valid.validate("2614e9ad-b836-448f-abf1-b983e57e30bc", "1", "", "1614699", "15");
        });
        String actualMessage = exception.getMessage();
        assertEquals("Invalid campaign id",actualMessage);
    }

    @Test
    void whenExceptionThrown_SrcId() {
        Exception exception = assertThrows(TransformerException.class, () -> {
            valid.validate("2614e9ad-b836-448f-abf1-b983e57e30bc", "1", "", "1614699", "");
        });
        String actualMessage = exception.getMessage();
        assertEquals("Invalid Ip address",actualMessage);
    }

    @Test
    void whenExceptionThrown_ServerTs() {
        Exception exception = assertThrows(TransformerException.class, () -> {
            valid.validate("2614e9ad-b836-448f-abf1-b983e57e30bc", "1", "M6106", "16146742394599", "15");
        });
        String actualMessage = exception.getMessage();
        assertEquals("Invalid server timestamp",actualMessage);
    }

//    @Test
//    void whenExceptionNotThrown_serverTS() {
//        assertDoesNotThrow(() -> {
//            valid.validate("2614e9ad-b836-448f-abf1-b983e57e30bc", "1", "6106", "", "15");
//        });
//    }
}