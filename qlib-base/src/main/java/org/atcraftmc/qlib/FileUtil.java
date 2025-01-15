package org.atcraftmc.qlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public interface FileUtil {
    Logger LOGGER = LogManager.getLogger("FILES");

    static File tryReleaseAndGetFile(String src, String dest) {
        File f = new File(dest);
        if (f.exists() && f.length() != 0) {
            return f;
        }
        coverFile(src, dest);
        return f;
    }

    static boolean coverFile(String srcDir, String fileDir) {
        File f = new File(fileDir);
        if (f.getParentFile().mkdirs()) {
            LOGGER.info("created folder of file: {}", fileDir);
        }
        try {
            InputStream is = getPluginResource(srcDir);
            OutputStream s = new FileOutputStream(f);
            if (is == null) {
                return false;
            }

            if (f.createNewFile()) {
                LOGGER.info("created file:{}", fileDir);
            }

            var b = is.readAllBytes();
            is.close();

            if (b.length == 0) {
                return false;
            }

            s.write(b);
            s.close();

            return true;
        } catch (Exception e) {
            LOGGER.error("failed to save resource(src: {},dest: {}): {}", srcDir, fileDir, e.getMessage());
        }

        return false;
    }

    static InputStream getPluginResource(String path) {
        String fixedPath = path.replaceFirst("/", "");

        File folder = new File(PluginPlatform.instance().pluginsFolder());

        for (File f : Objects.requireNonNull(folder.listFiles())) {
            if (!f.getName().endsWith(".jar")) {
                continue;
            }
            try {
                JarFile jf = new JarFile(f);
                ZipEntry entry = jf.getEntry(fixedPath);
                if (entry == null) {
                    jf.close();
                    continue;
                }
                return jf.getInputStream(entry);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
