package org.unibl.etf.emobility_hub_user;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.jasper.JspC;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThatCode;

// Translates and compiles every page with Tomcat 9's JSP compiler, so a typo in
// a scriptlet fails the build instead of the first visitor.
class JspCompilationTest {

    @TempDir
    Path out;

    @Test
    void everyPageCompiles() throws Exception {
        JspC jspc = new JspC();
        jspc.setUriroot(Path.of("src", "main", "webapp").toAbsolutePath().toString());
        jspc.setOutputDir(out.toString());
        jspc.setClassPath(System.getProperty("java.class.path"));
        jspc.setCompile(true);
        jspc.setFailOnError(true);
        jspc.setValidateXml(false);
        jspc.setCompilerSourceVM("17");
        jspc.setCompilerTargetVM("17");

        assertThatCode(jspc::execute).doesNotThrowAnyException();
        try (var classes = Files.walk(out)) {
            org.assertj.core.api.Assertions.assertThat(classes.filter(p -> p.toString().endsWith("_jsp.class")))
                    .hasSize(11);
        }
    }
}
