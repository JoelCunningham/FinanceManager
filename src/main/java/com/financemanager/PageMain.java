package com.financemanager;

import java.util.HashMap;
import java.util.Map;

import com.financemanager.helper.Helper;
import com.financemanager.pages.BudgetPage;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
* Main java class using Javalin, Thymeleaf & JDBC.
*/
public class PageMain implements Handler{

    public  static final String     URL         = "/";
    private static final String     TEMPLATE    = "html/main.html";

    @Override
    public void handle(Context context) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();
        JDBC jdbc = new JDBC();

        BudgetPage.loadBudgetPage(context, model, jdbc);
      
        model.put("version", Helper.getGitVersion());

        context.render(TEMPLATE, model); //Make Javalin render the webpage
    }

}
