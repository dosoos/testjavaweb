package org.example.server.model;


/**
 * @Author: yzhang8
 */
public class Remark {

    public String address;

    public String author;

    public String remark;

    public Remark() {
    }

    public Remark(String address, String author, String remark) {
        this.address = address;
        this.author = author;
        this.remark = remark;
    }

    public String toString() {
        return "Extra{" + "address='" + address + ", author='" + author + ", remark='" + remark + '}';
    }
}
