package org.unibl.etf.emobility_hub_user.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.Part;

// Saves avatar pictures on disk; ImageServlet serves them back under /images/.
public final class AvatarStorage {
	public static final String BASE_DIR = "/tmp/uploads/images/";
	private static final long MAX_BYTES = 5L * 1024 * 1024;
	private static final Set<String> EXTENSIONS = Set.of(".png", ".jpg", ".jpeg", ".gif", ".webp");

	private AvatarStorage() {
	}

	public static boolean isPresent(Part part) {
		return part != null && part.getSize() > 0 && part.getSubmittedFileName() != null
				&& !part.getSubmittedFileName().isEmpty();
	}

	// Returns the browser URL of the saved picture.
	public static String save(Part part, String username, String contextPath) throws IOException {
		String extension = extensionOf(part.getSubmittedFileName());
		if (!EXTENSIONS.contains(extension) || part.getContentType() == null
				|| !part.getContentType().startsWith("image/")) {
			throw new IllegalArgumentException("The avatar must be a PNG, JPG, GIF or WEBP image.");
		}
		if (part.getSize() > MAX_BYTES) {
			throw new IllegalArgumentException("The avatar must be smaller than 5 MB.");
		}

		File dir = new File(BASE_DIR + username);
		if (!dir.exists() && !dir.mkdirs()) {
			throw new IOException("Cannot create " + dir);
		}
		String fileName = UUID.randomUUID() + extension;
		try (FileOutputStream out = new FileOutputStream(new File(dir, fileName));
				InputStream in = part.getInputStream()) {
			in.transferTo(out);
		}
		return contextPath + "/images/" + username + "/" + fileName;
	}

	private static String extensionOf(String fileName) {
		if (fileName == null || !fileName.contains(".")) {
			return "";
		}
		return fileName.substring(fileName.lastIndexOf('.')).toLowerCase();
	}
}
