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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
