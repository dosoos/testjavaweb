package org.example.server.util;


import com.mongodb.client.*;
import org.bson.Document;
import org.example.server.model.Amount;
import org.example.server.model.Log;
import org.example.server.model.Remark;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * @author yzhang8
 * MongoDB util
 * 1. Connect to MongoDB
 * 2. Insert data to MongoDB
 * 3. Query data from MongoDB
 */
public class MongoDB {

//    private static String CONNECTION_URI = "mongodb://admin:admin@localhost:27017/";
    private static String CONNECTION_URI = "mongodb+srv://admin:Xyz1234567890-@cluster0.ni45b.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

    private static String DATABASE_NAME = "task2";

    private static MongoClient mongoClient;

    private static MongoDB instance;

    private MongoDB() {
        mongoClient = MongoClients.create(CONNECTION_URI);
    }

    public static MongoDB getInstance() {
        if (instance == null) {
            instance = new MongoDB();
        }
        return instance;
    }

    /**
     * Insert data to MongoDB
     */
    public boolean insertRemark(String address, String author, String remark) {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("remark");
        Document doc = new Document("address", address).append("author", author).append("remark", remark);
        collection.insertOne(doc);
        return true;
    }

    public boolean deleteRemark(String address) {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Remark> collection = database.getCollection("remark", Remark.class);
        collection.deleteOne(eq("address", address));
        return true;
    }

    public boolean updateRemark(String address, String remark) {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Remark> collection = database.getCollection("remark", Remark.class);
        collection.updateOne(eq("address", address), new Document("$set", new Document("remark", remark)));
        return true;
    }

    /**
     * Query data from MongoDB
     */
    public List<Remark> queryRemark(String address) {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("remark");
        Document document = collection.find(eq("address", address)).first();
        List<Remark> list = new ArrayList<Remark>();
        if (document != null) {
            Remark extra = new Remark(document.getString("address"), document.getString("author"), document.getString("remark"));
            list.add(extra);
        }
        return list;
    }

    public List<Remark> getAllRemark() {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("remark");
        FindIterable<Document> documents = collection.find();
        List<Remark> list = new ArrayList<Remark>();
        for (Document document : documents) {
            Remark extra = new Remark(document.getString("address"), document.getString("author"), document.getString("remark"));
            list.add(extra);
        }
        return list;
    }

    public boolean insertLog(Log log) {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("log");
        Document doc = new Document("ip", log.ip).append("mac", log.mac).append("path", log.path).append("time", log.time);
        collection.insertOne(doc);
        return true;
    }

    public List<Log> getAllLog() {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("log");
        FindIterable<Document> documents = collection.find();
        List<Log> list = new ArrayList<Log>();
        for (Document document : documents) {
            Log log = new Log(document.getString("ip"), document.getString("mac"), document.getString("path"), document.getString("time"));
            list.add(log);
        }
        return list;
    }

    public boolean deleteLog(String ip) {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Log> collection = database.getCollection("log", Log.class);
        collection.deleteOne(eq("ip", ip));
        return true;
    }

    public boolean updateLog(String ip, String path) {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Log> collection = database.getCollection("log", Log.class);
        collection.updateOne(eq("ip", ip), new Document("$set", new Document("path", path)));
        return true;
    }

    public Log queryLog(String ip) {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("log");
        Document document = collection.find(eq("ip", ip)).first();
        if (document != null) {
            return new Log(document.getString("ip"), document.getString("mac"), document.getString("path"), document.getString("time"));
        }
        return null;
    }

    public boolean insertAmount(String address, float amount) {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("amount");
        Document doc = new Document("address", address).append("amount", amount);
        collection.insertOne(doc);
        return true;
    }

    public List<Amount> getAllAmount() {
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        MongoCollection<Document> collection = database.getCollection("amount");
        FindIterable<Document> documents = collection.find();
        List<Amount> list = new ArrayList<Amount>();
        for (Document document : documents) {
            Double amountText = document.getDouble("amount");
            Amount extra = new Amount(document.getString("address"), Float.parseFloat(amountText.toString()));
            list.add(extra);
        }
        return list;
    }

}
