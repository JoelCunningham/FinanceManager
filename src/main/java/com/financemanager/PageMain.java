package com.financemanager;

import java.util.HashMap;
import java.time.LocalDate;
import java.util.Map;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
* Homepage java class using Javalin, Thymeleaf & JDBC.
*
* @author Joel Cunningham 2023
*/
public class PageMain implements Handler{

    public  static final String     URL         = "/";
    private static final String     TEMPLATE    = "html/main.html";

    @Override
    public void handle(Context context) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();
        //JDBC jdbc = new JDBC();



        model.put("CurrentYear", LocalDate.now().getYear());
        model.put("VersionNumber", Helper.getGitVersion());

        context.render(TEMPLATE, model); //Make Javalin render the webpage

    }

}
