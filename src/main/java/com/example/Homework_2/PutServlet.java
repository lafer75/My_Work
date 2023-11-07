package com.example.Homework_2;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/putServlet")
public class PutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String sid = request.getParameter("id");
        int id = Integer.parseInt(sid);

        String name = request.getParameter("name");
        String email = request.getParameter("email");

        Employee employee = new Employee(id, name, email, request.getParameter("country"));

        int status = EmployeeRepository.update(employee);
        if (status > 0) {
            response.sendRedirect("viewByIDServlet?id=68");
        } else {
            out.println("Sorry! unable to update record");
        }
        out.close();
    }
}
