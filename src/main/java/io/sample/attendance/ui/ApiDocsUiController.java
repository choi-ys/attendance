package io.sample.attendance.ui;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(
    value = "docs",
    produces = MediaType.TEXT_HTML_VALUE
)
public class ApiDocsUiController {
    private static final String DOCS_PATH = "docs/index.html";

    @GetMapping
    public String docs() {
        System.out.println("요청");
        return DOCS_PATH;
    }
}
