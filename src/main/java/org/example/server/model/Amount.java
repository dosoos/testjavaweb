package org.example.server.model;

public class Amount {

    String address;

    float amount;

    public Amount() {
    }

    public Amount(String address, float amount) {
        this.address = address;
        this.amount = amount;
    }

    public String toString() {
        return "Amount{" + "address='" + address + ", amount='" + amount + '}';
    }

}
