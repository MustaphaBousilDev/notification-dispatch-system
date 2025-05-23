package com.auth.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleMathTest {

    @Test
    void testSum() {
        int result = 1 + 1;
        assertEquals(2, result);
    }
}