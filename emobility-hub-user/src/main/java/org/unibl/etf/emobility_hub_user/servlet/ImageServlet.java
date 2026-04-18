package org.unibl.etf.emobility_hub_user.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.servlet.http.*;

public class ImageServlet extends HttpServlet {
    private static final String BASE_DIR = "/tmp/uploads/images/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Prvo provjeri u /tmp/uploads/images/
        File file = new File("/tmp/uploads/images/" + pathInfo);

        // Ako ne postoji, traži u WAR-u
        if (!file.exists() || !file.isFile()) {
            String realPath = getServletContext().getRealPath("/images" + pathInfo);
            if (realPath != null) {
                file = new File(realPath);
            }
        }

        if (!file.exists() || !file.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mimeType = Files.probeContentType(file.toPath());
        resp.setContentType(mimeType != null ? mimeType : "application/octet-stream");
        resp.setContentLength((int) file.length());

        try (InputStream in = new FileInputStream(file);
                OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}