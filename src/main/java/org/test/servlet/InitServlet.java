package org.test.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * InitServlet
 */

@WebServlet(loadOnStartup = 100)
public class InitServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("init called");
        // input your logic here
    }

    private void execute() {

    }
}
