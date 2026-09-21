package org.notima.generic.businessobjects.test;

import org.junit.Test;
import org.notima.generic.businessobjects.Attachment;
import org.notima.generic.businessobjects.AttachmentStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class AttachmentStoreTest {

	@Test
	public void testAddResolveAndDeleteFile() throws IOException {

		Path tmpDir = Files.createTempDirectory("attachmentstore-test");
		Path baseFile = tmpDir.resolve("company.json");
		Files.writeString(baseFile, "{}");

		Path sourceFile = tmpDir.resolve("receipt.txt");
		Files.writeString(sourceFile, "hello");

		AttachmentStore store = new AttachmentStore(baseFile, "Acme_files", null);

		Path root = store.resolveRoot();
		assertEquals(tmpDir.resolve("Acme_files"), root);

		Attachment a = store.addFile(sourceFile, "vouchers/abc-123", "A receipt");
		assertNotNull(a.getId());
		assertEquals("receipt.txt", a.getFileName());
		assertEquals("A receipt", a.getDescription());
		assertNotNull(a.getDate());
		assertTrue(a.getPath().startsWith("vouchers/abc-123/"));

		Path abs = store.resolveAbsolute(a);
		assertTrue(Files.exists(abs));
		assertEquals("hello", Files.readString(abs));

		store.deleteFile(a);
		assertFalse(Files.exists(abs));
	}

	@Test
	public void testCustomRootOverridesDefault() throws IOException {
		Path customRoot = Files.createTempDirectory("attachmentstore-custom");
		AttachmentStore store = new AttachmentStore(null, "unused_files", customRoot);
		assertEquals(customRoot, store.resolveRoot());
	}

	@Test
	public void testNoRootResolvesToNullBeforeFirstSave() {
		AttachmentStore store = new AttachmentStore(null, "Acme_files", null);
		assertNull(store.resolveRoot());
	}

	@Test
	public void testSanitizeFolderName() {
		assertEquals("Acme_AB", AttachmentStore.sanitizeFolderName("Acme AB"));
		assertEquals("556677-1234", AttachmentStore.sanitizeFolderName("556677-1234"));
		assertEquals("a_b", AttachmentStore.sanitizeFolderName("a/b"));
		assertEquals("attachments", AttachmentStore.sanitizeFolderName(null));
		assertEquals("attachments", AttachmentStore.sanitizeFolderName("  "));
	}

}
