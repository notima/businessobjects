package org.notima.generic.businessobjects.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.AdapterInfo;
import org.notima.generic.businessobjects.Setting;

class AdapterInfoTest {

    @Test
    void testToStringHumanReadable() {
        AdapterInfo ai = new AdapterInfo();

        ai.setSystemName("test-system");
        ai.setDisplayName("Test System");
        ai.setSystemUrl("https://example.com");
        ai.setSystemUrlDescription("Example endpoint");
        ai.setOnline(true);
        ai.setLocal(false);

        ai.addRequiredSetting("username");
        ai.addRequiredSetting("password");

        ai.setSetting("timeout", new Setting()); // content not important for toString()

        String s = ai.toString();
        System.out.println(s);

        // High-level structure
        assertTrue(s.contains("AdapterInfo"));
        assertTrue(s.contains("Test System"));

        // Status formatting
        assertTrue(s.contains("Online"));
        assertTrue(s.contains("Remote"));

        // URL info
        assertTrue(s.contains("https://example.com"));
        assertTrue(s.contains("Example endpoint"));

        // Settings summary
        assertTrue(s.contains("timeout"));
        assertTrue(s.contains("username"));
        assertTrue(s.contains("password"));
    }

    @Test
    void testToStringWithMinimalData() {
        AdapterInfo ai = new AdapterInfo();

        String s = ai.toString();

        // Should not throw and should be readable
        assertTrue(s.contains("AdapterInfo"));
        assertTrue(s.contains("Offline"));
        assertTrue(s.contains("None")); // settings / required settings
    }
}
