package org.example.server;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.server.model.Amount;
import org.example.server.model.Log;
import org.example.server.util.MongoDB;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @Author: yzhang8
 */
@WebServlet(name = "dashboardServlet", value = "/dashboard")
public class Dashboard extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        MongoDB instance = MongoDB.getInstance();

        Gson gson = new Gson();

        List<Amount> amounts = instance.getAllAmount();
        request.setAttribute("account_names", gson.toJson(amounts.stream().map(Amount::getAddress).toArray()));
        request.setAttribute("account_amounts", gson.toJson(amounts.stream().map(Amount::getAmount).toArray()));

        // analyze logs
        List<Log> logs = instance.getAllLog();
        int[] counts = new int[24];
        for (Log log : logs) {
            long time = Long.parseLong(log.getTime());
            int hour = (int) ((time / 1000 / 60 / 60) % 24);
            counts[hour]++;
        }
        request.setAttribute("hour_group", gson.toJson(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));
        request.setAttribute("hour_counts", gson.toJson(counts));

        request.setAttribute("request_logs", logs);
        String htmlTrs = "";
        for (int i = 0; i < logs.size(); i++) {
            htmlTrs += "<tr><td>" + logs.get(i).time + "</td><td>" + logs.get(i).ip + "</td><td>" + logs.get(i).path + "</td><td>" + logs.get(i).getTime() + "</td></tr>";
        }
        request.setAttribute("request_logs_text", htmlTrs);


        request.getRequestDispatcher("dashboard.jsp").forward(request, response);

    }

    public void destroy() {
    }
}