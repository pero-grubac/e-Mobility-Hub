package org.unibl.etf.emobility_hub_user.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.*;

import org.unibl.etf.emobility_hub_user.storage.AvatarStorage;

// Serves uploaded avatars, falling back to the images packaged in the WAR.
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.contains("..")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Prvo provjeri u /tmp/uploads/images/
        File file = new File(AvatarStorage.BASE_DIR + pathInfo);

        // Ako ne postoji, traži u WAR-u
        if (!file.isFile()) {
            String realPath = getServletContext().getRealPath("/images" + pathInfo);
            if (realPath != null) {
                file = new File(realPath);
            }
        }

        if (!file.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mimeType = getServletContext().getMimeType(file.getName());
        resp.setContentType(mimeType != null ? mimeType : "application/octet-stream");
        resp.setContentLengthLong(file.length());
        Files.copy(file.toPath(), resp.getOutputStream());
    }
}
