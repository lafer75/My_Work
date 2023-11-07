package com.example.Homework_2.session;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    // Using a map to store user credentials
    private static final Map<String, String> userCredentials = new HashMap<>();

    static {
        userCredentials.put("admin", "password");
        userCredentials.put("user", "pass");
        userCredentials.put("guest", "1234");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String user = request.getParameter("user");
        String pwd = request.getParameter("pwd");

        // Retrieving password from the map based on the provided username
        String storedPassword = userCredentials.get(user);

        if (storedPassword != null && storedPassword.equals(pwd)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60);
            Cookie userName = new Cookie("user", user);
            userName.setMaxAge(30 * 60);
            response.addCookie(userName);
            PrintWriter out = response.getWriter();
            out.println("Welcome back to the team, " + user + "!");
        } else {
            PrintWriter out = response.getWriter();
            out.println("Either user name or password is wrong!");
        }
    }
}
