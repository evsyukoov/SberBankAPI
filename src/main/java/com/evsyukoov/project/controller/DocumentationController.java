package com.evsyukoov.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class DocumentationController {

    public static String redirectUrl = "/swagger-ui/index.html";

    @GetMapping(path = "/service/bank/v1/documentation")
    public Object redirectWithUsingRedirectView() {
        return new RedirectView(redirectUrl);
    }

}
