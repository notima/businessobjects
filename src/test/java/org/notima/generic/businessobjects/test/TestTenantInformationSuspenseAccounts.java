package org.notima.generic.businessobjects.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.SuspenseAccountType;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;
import org.notima.generic.businessobjects.TenantInformation;
import org.notima.util.json.JsonUtil;

import com.google.gson.Gson;

public class TestTenantInformationSuspenseAccounts {

	@Test
	public void suspenseAccountsPerTypeSurviveJson() {
		TenantInformation ti = new TenantInformation();
		ti.setTenant(new TaxSubjectIdentifier("556000-0000", "SE"));
		assertFalse(ti.hasSuspenseAccounts());
		ti.setSuspenseAccounts(SuspenseAccountType.UNIDENTIFIED, Arrays.asList("1799", "2999"));
		ti.setSuspenseAccounts(SuspenseAccountType.OWNER_SETTLEMENT, Arrays.asList("2893"));

		Gson gson = JsonUtil.buildGson();
		String json = gson.toJson(ti);
		assertTrue(json.contains("\"UNIDENTIFIED\""), json);
		TenantInformation copy = gson.fromJson(json, TenantInformation.class);

		assertEquals(Arrays.asList("1799", "2999"), copy.getSuspenseAccounts(SuspenseAccountType.UNIDENTIFIED));
		assertEquals(Arrays.asList("2893"), copy.getSuspenseAccounts(SuspenseAccountType.OWNER_SETTLEMENT));
		assertTrue(copy.getSuspenseAccounts(SuspenseAccountType.IDENTIFIED).isEmpty());

		copy.setSuspenseAccounts(SuspenseAccountType.UNIDENTIFIED, null);
		copy.setSuspenseAccounts(SuspenseAccountType.OWNER_SETTLEMENT, null);
		assertFalse(copy.hasSuspenseAccounts());
		assertNull(copy.getSuspenseAccounts());
		assertEquals(SuspenseAccountType.OWNER_SETTLEMENT, SuspenseAccountType.parse("owner_settlement"));
	}

}
