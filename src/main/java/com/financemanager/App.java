package com.financemanager;

import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;

/**
 * Main Application Class.
 * <p>
 * Running this class as regular java application will start the
 * Javalin HTTP Server and the web application.
 *
 * @author Joel Cunningham 2023
 */
public class App {
     
    public static final int         JAVALIN_PORT    = 7000;
    public static final String      JS_DIR          = "js/";
    public static final String      CSS_DIR         = "css/";
    public static final String      IMAGES_DIR      = "images/";

    //Starts the Javalin HTTP Server and web application
    public static void main(String[] args) {
        // Create a HTTP server and listen in port 7000
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/help/routes"));
            config.addStaticFiles(JS_DIR);
            config.addStaticFiles(CSS_DIR);
            config.addStaticFiles(IMAGES_DIR);
        }).start(JAVALIN_PORT);

        // Configure Web Routes
        configureRoutes(app);
    }

    public static void configureRoutes(Javalin app) {
        app.get(PageMain.URL, new PageMain());
      
        app.post(PageMain.URL, new PageMain());
    }
    
}