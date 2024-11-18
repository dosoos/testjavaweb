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
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yzhang8
 */
@WebServlet(name = "balanceServlet", value = "/balance-servlet")
public class BalanceServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        Map<String, Object> responseBody = new HashMap<>();

        String address = request.getParameter("address");

        String reqIp = request.getRemoteAddr();
        String reqMac = request.getParameter("mac");
        if (reqMac == null || reqMac.isEmpty()) reqMac = "anonymous";
        String reqPath = request.getRequestURI();
        String reqTime = String.valueOf(System.currentTimeMillis());
        Log reqLog = new Log(reqIp, reqMac, reqPath, reqTime);
        MongoDB.getInstance().insertLog(reqLog);

        if (address == null || address.isEmpty() || !Ethereum.validateAddress(address)) {
            responseBody.put("code", 1);
            responseBody.put("message", "invalid address");
            response.getWriter().write(gson.toJson(responseBody));
            return;
        }

        BigInteger balance = BigInteger.valueOf(0);
        try {
            balance = Ethereum.getBalance(address);
        } catch (InterruptedException e) {
            responseBody.put("code", 2);
            responseBody.put("message", "api server error!");
            response.getWriter().write(gson.toJson(responseBody));
            return;
        }

        // save balance to MongoDB
        MongoDB.getInstance().insertAmount(address, Float.parseFloat(Ethereum.toEther(balance).toString()));

        responseBody.put("code", 0);
        responseBody.put("message", "success");
        BigInteger finalBalance = balance;
        responseBody.put("data", new HashMap<>(){
            {
                put("balance", finalBalance.toString());
                put("balance_eth", Ethereum.toEther(finalBalance).toString());
                put("remarks", MongoDB.getInstance().queryRemark(address));
            }
        });
        response.getWriter().write(gson.toJson(responseBody));
        return;
    }

    public void destroy() {
    }
}
