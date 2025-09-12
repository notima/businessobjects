package org.notima.generic.businessobjects.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.Tax;
import org.notima.generic.businessobjects.exception.NoMatchingTaxException;
import org.notima.generic.businessobjects.tax.TaxTool;

class TestTaxTool {

	@BeforeEach
	void setUp() throws Exception {
	}

    @Test
    void testExactMatch() throws Exception {
        Tax tax25 = new Tax("VAT25");
        tax25.setRate(25.0);
        List<Tax> taxes = Arrays.asList(tax25);

        Tax result = TaxTool.getFirstMatchOnRate(25.0, 0.0, taxes);

        assertNotNull(result);
        assertEquals(25.0, result.getRate());
        assertEquals("VAT25", result.getKey());
    }

    @Test
    void testMatchWithinAllowedDeviation() throws Exception {
        Tax tax12 = new Tax("VAT12");
        tax12.setRate(12.0);
        List<Tax> taxes = Arrays.asList(tax12);

        Tax result = TaxTool.getFirstMatchOnRate(11.5, 1.0, taxes);

        assertNotNull(result);
        assertEquals("VAT12", result.getKey());
    }

    @Test
    void testNoMatchThrowsException() {
        Tax tax6 = new Tax("VAT6");
        tax6.setRate(6.0);
        List<Tax> taxes = Arrays.asList(tax6);

        assertThrows(NoMatchingTaxException.class, () -> {
            TaxTool.getFirstMatchOnRate(25.0, 0.0, taxes);
        });
    }

    @Test
    void testNullTaxesThrowsException() {
        assertThrows(NoMatchingTaxException.class, () -> {
            TaxTool.getFirstMatchOnRate(25.0, 0.0, null);
        });
    }

    @Test
    void testMultipleTaxesFirstMatchReturned() throws Exception {
        Tax tax6 = new Tax("VAT6");
        tax6.setRate(6.0);
        Tax tax12 = new Tax("VAT12");
        tax12.setRate(12.0);
        Tax tax25 = new Tax("VAT25");
        tax25.setRate(25.0);

        List<Tax> taxes = Arrays.asList(tax6, tax12, tax25);

        Tax result = TaxTool.getFirstMatchOnRate(12.0, 0.0, taxes);

        assertNotNull(result);
        assertEquals("VAT12", result.getKey());
    }
}
