package com.financemanager;

import java.util.HashMap;
import java.util.Map;

import com.financemanager.page.BudgetPage;
import com.financemanager.page.StatementPage;
import com.financemanager.page.SummaryPage;
import com.financemanager.page.TopnavForms;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
* Main java class using Javalin, Thymeleaf & JDBC.
*/
public class Main implements Handler{

    public  static final String     URL         = "/";
    private static final String     TEMPLATE    = "html/main.html";

    @Override
    public void handle(Context context) throws Exception {

        Map<String, Object> model = new HashMap<String, Object>();
        JDBC jdbc = new JDBC();

        Helper.verifyDatabase(jdbc);

        BudgetPage budget_page = new BudgetPage(context, model, jdbc);
        SummaryPage summary_page = new SummaryPage(context, model, jdbc);
        StatementPage statement_page = new StatementPage(context, model, jdbc);

        budget_page.load();
        summary_page.load();
        statement_page.load();

        TopnavForms.loadAddCategory(context, model, jdbc);
        TopnavForms.loadCreateCategory(context, model, jdbc);
        TopnavForms.loadCreateHeader(context, model, jdbc);

        context.render(TEMPLATE, model); //Make Javalin render the webpage
    }
}
