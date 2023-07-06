package com.financemanager;

import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;

import java.net.URI;
import java.awt.Desktop;
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
    public static final String      FONT_DIR        = "font/";
    public static final String      IMAGE_DIR       = "image/";

    //Starts the Javalin HTTP Server and web application
    public static void main(String[] args) {
        // Create a HTTP server and listen in port 7000
        Javalin app = Javalin.create(config -> {
            config.registerPlugin(new RouteOverviewPlugin("/help/routes"));
            config.addStaticFiles(JS_DIR);
            config.addStaticFiles(CSS_DIR);
            config.addStaticFiles(FONT_DIR);
            config.addStaticFiles(IMAGE_DIR);
        }).start(JAVALIN_PORT);

        // Create a new instance of the Main class
        Main main = new Main();

        // Configure Web Routes
        configureRoutes(app, main);

        // Create a new thread to monitor the value of the main.quit variable
        new Thread(() -> {
            while (true) {
                if (main.quit) {
                    app.stop();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // Open the website in the default browser
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {     
            try {
                Desktop.getDesktop().browse(new URI("http://localhost:" + JAVALIN_PORT));
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    public static void configureRoutes(Javalin app, Main main) {
        app.get(Main.URL, main);
        app.post(Main.URL, main);
    }

}