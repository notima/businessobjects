package org.notima.generic.businessobjects;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Manages {@link Attachment} files on disk for a single data file (e.g. an application's
 * save file), independent of what application or entity model owns the attachments.
 * <p>
 * Files are copied into a root folder and addressed by paths relative to that root, stored
 * in {@link Attachment#getPath()}. The root is either a centrally configured folder (the
 * {@code customRoot} constructor argument &mdash; useful for a shared/synced attachment
 * location across users) or a folder named {@code defaultFolderName} next to the owning
 * data file.
 * <p>
 * Within the root, callers group attachments by passing an {@code entitySubPath} to
 * {@link #addFile}, e.g. {@code "vouchers/" + voucher.getOrCreateId()} &mdash; there is no
 * fixed entity model here, so any stable per-entity key works.
 */
public class AttachmentStore {

	private Path baseFile;
	private final String defaultFolderName;
	private final Path customRoot;

	/**
	 * @param baseFile          path to the owning data file; may be {@code null} for a
	 *                          not-yet-saved file (in which case {@link #resolveRoot()}
	 *                          returns {@code null} unless {@code customRoot} is set)
	 * @param defaultFolderName folder name to use next to {@code baseFile} when
	 *                          {@code customRoot} is not set, e.g. {@code "Acme_files"}
	 * @param customRoot        central attachment root path from configuration;
	 *                          {@code null} means use the default folder next to {@code baseFile}
	 */
	public AttachmentStore(Path baseFile, String defaultFolderName, Path customRoot) {
		this.baseFile = baseFile;
		this.defaultFolderName = defaultFolderName;
		this.customRoot = customRoot;
	}

	/** Returns the path to the owning data file, or {@code null} if it has not been saved yet. */
	public Path getBaseFile() {
		return baseFile;
	}

	/**
	 * Updates the owning data file's path. Call after the first save of a new file so that
	 * the default local folder can be resolved.
	 */
	public void setBaseFile(Path baseFile) {
		this.baseFile = baseFile;
	}

	/**
	 * Returns the attachment root folder, or {@code null} when no base file has been saved
	 * yet and no custom root is configured.
	 */
	public Path resolveRoot() {
		if (customRoot != null) return customRoot;
		if (baseFile == null) return null;
		Path parent = baseFile.getParent();
		return parent != null ? parent.resolve(defaultFolderName) : Paths.get(defaultFolderName);
	}

	/**
	 * Copies {@code sourceFile} into {@code <root>/<entitySubPath>/} and returns a
	 * populated {@link Attachment} with a relative path.
	 *
	 * @param sourceFile    the file to copy
	 * @param entitySubPath sub-folder inside the root, e.g. {@code "vouchers/abc-123"}
	 * @param description   optional user-supplied description; may be {@code null}
	 * @throws IllegalStateException if no root can be resolved (unsaved file, no custom root)
	 * @throws IOException           if the file cannot be copied
	 */
	public Attachment addFile(Path sourceFile, String entitySubPath, String description)
			throws IOException {
		Path root = resolveRoot();
		if (root == null) throw new IllegalStateException("No attachment root could be resolved");

		String uuid     = UUID.randomUUID().toString();
		String fileName = sourceFile.getFileName().toString();
		Path targetDir  = root.resolve(entitySubPath);
		Files.createDirectories(targetDir);
		Path target = targetDir.resolve(uuid + "_" + fileName);
		Files.copy(sourceFile, target, StandardCopyOption.REPLACE_EXISTING);

		Attachment a = new Attachment();
		a.setId(uuid);
		a.setFileName(fileName);
		a.setPath(root.relativize(target).toString().replace('\\', '/'));
		a.setDescription(description);
		a.setDate(LocalDate.now());
		return a;
	}

	/**
	 * Resolves the absolute file path for an attachment, or {@code null} when the root
	 * cannot be determined.
	 */
	public Path resolveAbsolute(Attachment a) {
		Path root = resolveRoot();
		if (root == null || a == null || a.getPath() == null) return null;
		return root.resolve(a.getPath());
	}

	/**
	 * Deletes the stored file for an attachment. Silently ignores missing files and
	 * unresolvable paths.
	 */
	public void deleteFile(Attachment a) {
		Path abs = resolveAbsolute(a);
		if (abs != null) {
			try { Files.deleteIfExists(abs); } catch (IOException ignored) {}
		}
	}

	/** Sanitizes {@code name} into a filesystem-safe folder-name component. */
	public static String sanitizeFolderName(String name) {
		if (name == null || name.isBlank()) return "attachments";
		return name.replaceAll("[/\\\\:*?\"<>|\\s]", "_");
	}

}
