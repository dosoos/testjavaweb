package org.example.server;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.server.model.Log;
import org.example.server.util.Ethereum;
import org.example.server.util.MongoDB;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yzhang8
 */
@WebServlet(name = "remarkServlet", value = "/remark-servlet")
public class RemarkServlet extends HttpServlet {

    public void init() {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        Map<String, Object> responseBody = new HashMap<>();

        String address = request.getParameter("address");
        String remark = request.getParameter("remark");

        // log request
        String reqIp = request.getRemoteAddr();
        String reqMac = request.getParameter("mac");
        if (reqMac == null || reqMac.isEmpty()) reqMac = "anonymous";
        String reqPath = request.getRequestURI();
        String reqTime = String.valueOf(System.currentTimeMillis());
        Log reqLog = new Log(reqIp, reqMac, reqPath, reqTime);
        MongoDB.getInstance().insertLog(reqLog);

        // validate address
        if (address == null || address.isEmpty() || !Ethereum.validateAddress(address)) {
            responseBody.put("code", 1);
            responseBody.put("message", "invalid address");
            response.getWriter().write(gson.toJson(responseBody));
            return;
        }

        MongoDB.getInstance().insertRemark(address, reqMac, remark);
        responseBody.put("code", 0);
        responseBody.put("message", "success");
        response.getWriter().write(gson.toJson(responseBody));
        return;
    }

    public void destroy() {
    }
}