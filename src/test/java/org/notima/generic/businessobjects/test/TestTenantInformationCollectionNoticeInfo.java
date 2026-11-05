package org.notima.generic.businessobjects.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.notima.generic.businessobjects.CollectionNoticeInfo;
import org.notima.generic.businessobjects.TaxSubjectIdentifier;
import org.notima.generic.businessobjects.TenantInformation;
import org.notima.util.json.JsonUtil;

import com.google.gson.Gson;

public class TestTenantInformationCollectionNoticeInfo {

	@Test
	public void collectionNoticeInfoSurvivesJson() {
		TenantInformation ti = new TenantInformation();
		ti.setTenant(new TaxSubjectIdentifier("556000-0000", "SE"));
		assertNull(ti.getCollectionNoticeInfo());

		CollectionNoticeInfo cni = ti.getOrCreateCollectionNoticeInfo();
		assertTrue(cni.isEmpty());
		cni.setPhone("08-123 45 67");
		cni.setEmail("inkasso@exempel.se");
		cni.setRemitToAccount("123-4567");
		cni.setRemitToAccountType("PG");

		Gson gson = JsonUtil.buildGson();
		TenantInformation copy = gson.fromJson(gson.toJson(ti), TenantInformation.class);

		CollectionNoticeInfo c = copy.getCollectionNoticeInfo();
		assertEquals("08-123 45 67", c.getPhone());
		assertEquals("inkasso@exempel.se", c.getEmail());
		assertEquals("123-4567", c.getRemitToAccount());
		assertTrue(c.isPlusgiro());
		assertFalse(c.isEmpty());
	}

	@Test
	public void blankValuesDontOverride() {
		CollectionNoticeInfo cni = new CollectionNoticeInfo();
		cni.setPhone("  ");
		assertFalse(cni.hasPhone());
		assertTrue(cni.isEmpty());
	}

}
