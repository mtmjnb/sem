package com.napier.sem;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        // Connect to MongoDB on local system - we're using port 27000
        MongoClient mongo_client = new MongoClient("mongo-dbserver");
        // Get a database - will create when we use it
        MongoDatabase database = mongo_client.getDatabase("mydb");
        // Get a collection from the database
        MongoCollection<Document> collection = database.getCollection("test");
        // Create a document to store
        Document document = new Document("name", "Kevin Sim")
                .append("class", "Software Engineering Methods")
                .append("year", "2021")
                .append("result", new Document("CW", 95).append("EX", 85));
        // Add document to collection
        collection.insertOne(document);

        // Check document in collection
        Document my_document = collection.find().first();
        System.out.println(my_document.toJson());
    }
}
