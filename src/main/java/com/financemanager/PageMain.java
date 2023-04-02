package com.financemanager;

import java.util.HashMap;
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

        String year = context.formParam("page");
        System.out.println(year);

        String[] years = {"2023", "2022", "2021"}; //TODO
        

        //float[][] budget = createBudget();

        //model.put("BudgetArray", budget);

        model.put("years", years);

        var page_statuses = Map.of(
            "dashboard_active"  , "active",
            "budget_active"     , "",
            "summary_active"    , "",
            "statement_active"  , ""
        );

        for (Map.Entry<String, String> page : page_statuses.entrySet()) {
            model.put(page.getKey(), page.getValue());
        }

        model.put("version", Helper.getGitVersion());

        context.render(TEMPLATE, model); //Make Javalin render the webpage

    }

//     public static float[][] createBudget()) {
        
//         ;
//         if (SearchType == null) { SearchType = "Title"; 
        
        
//         for (int i = 0; i < budget.length; i++) {
//             for (int j = 0; j < budget[i].length; j++) {
//                 budget[i][j] = (float) Math.random();
//             }
//         }
//         //TODO

//         return budget;
//     }
// }

}
