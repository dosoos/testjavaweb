package org.example.server.util;

import com.google.gson.Gson;
import org.example.server.model.Balance;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;


/**
 * @Author: yzhang8
 */
public class Ethereum {

//    private static String BASE_URL = "https://ethereum-rpc.publicnode.com";
    private static String BASE_URL = "https://eth.llamarpc.com";

    /**
     * Get Ethereum balance by address
     * @param address Ethereum address
     * @return Ethereum balance
     * @throws IOException
     * @throws InterruptedException
     */
    public static BigInteger getBalance(String address) throws IOException, InterruptedException {
        // curl https://docs-demo.quiknode.pro/ \
        //  -X POST \
        //  -H "Content-Type: application/json" \
        //  --data '{"method":"eth_getBalance","params":["0x8D97689C9818892B700e27F316cc3E41e17fBeb9", "latest"],"id":1,"jsonrpc":"2.0"}'
        Map<String, Object> body = Map.of(
                "method", "eth_getBalance",
                "params", List.of(address, "latest"),
                "id", 1,
                "jsonrpc", "2.0"
        );
        Gson gson = new Gson();
//        System.out.println(gson.toJson(body));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());

        Balance balance = gson.fromJson(response.body(), Balance.class);

        // parse hex string to BigInteger
        return new BigInteger(balance.result.substring(2), 16);
    }

    /**
     * Validate Ethereum address
     * @param address Ethereum address
     * @return
     */
    public static boolean validateAddress(String address) {
        // check address length
        if (address.length() != 42) {
            return false;
        }

        // check address prefix
        if (!address.startsWith("0x")) {
            return false;
        }

        // check address characters
        for (int i = 2; i < address.length(); i++) {
            char c = address.toLowerCase().charAt(i);
            if (!Character.isDigit(c) && (c < 'a' || c > 'f')) {
                return false;
            }
        }

        return true;
    }

    /**
     * Convert balance to Ether
     * @param balance Ethereum balance
     * @return
     */
    public static Float toEther(BigInteger balance) {
        return balance.floatValue() / 1_000_000_000_000_000_000L;
    }
}
