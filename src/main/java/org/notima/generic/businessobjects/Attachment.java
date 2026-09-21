package org.notima.generic.businessobjects;

import java.time.LocalDate;

/**
 * Metadata record for a file attachment associated with a canonical business object
 * (e.g. an {@link AccountingVoucher}).
 * <p>
 * The actual file is stored on disk by an {@link AttachmentStore}; {@code path} is
 * relative to that store's root folder, so the owning data file (and its attachments)
 * remain portable as a pair of sibling folders.
 */
public class Attachment {

	private String    id;
	private String    fileName;
	/** Path relative to the attachment store's root folder (forward slashes). */
	private String    path;
	private String    description;
	private LocalDate date;

	public Attachment() {}

	public String    getId()                          { return id; }
	public void      setId(String id)                { this.id = id; }

	public String    getFileName()                   { return fileName; }
	public void      setFileName(String fileName)    { this.fileName = fileName; }

	public String    getPath()                       { return path; }
	public void      setPath(String path)            { this.path = path; }

	public String    getDescription()               { return description; }
	public void      setDescription(String d)       { this.description = d; }

	public LocalDate getDate()                       { return date; }
	public void      setDate(LocalDate date)         { this.date = date; }

}
