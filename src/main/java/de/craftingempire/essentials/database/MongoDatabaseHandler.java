/**
 * CEssentials | Copyright (c) 2022 LuciferMorningstarDev
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.craftingempire.essentials.database;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * CEssentials; de.craftingempire.essentials.database:MongoDatabaseHandler
 *
 * @license MIT <https://opensource.org/licenses/MIT>
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 14.03.2022
 */
public class MongoDatabaseHandler {

    private String databaseName;
    private MongoClient mongoClient;

    /**
     * Create default MongoDBHandler instance
     * @param connectionString the MongoDB connectionString
     * @param databaseName the databases name you want to use
     */
    public MongoDatabaseHandler(String connectionString, String databaseName) {
        this.databaseName = databaseName;
        connect(connectionString);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!isConnected()) {
                    try {closeSession();}catch(Exception ex){}
                    connect(connectionString);
                }
            }
        }, 0, 60000*3);
    }

    /**
     * if you want disable the connection check use this instance
     * @param connectionString the MongoDB connectionString
     * @param databaseName the databases name you want to use
     * @param checkConnectionEvery3Minutes should the connection be upgraded
     */
    public MongoDatabaseHandler(String connectionString, String databaseName, boolean checkConnectionEvery3Minutes) {
        this.databaseName = databaseName;
        connect(connectionString);
        if(checkConnectionEvery3Minutes)
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if(!isConnected()) {
                        try {closeSession();}catch(Exception ex){}
                        connect(connectionString);
                    }
                }
            }, 0, 60000*3);
    }

    /**
     * Build a Document insertable to a collection
     * @param documentName name of the document which you want to get
     * @param keyValueCollection a collection of keys and values which to append to the document
     * @return document /%/
     */
    public Document buildDocument(String documentName, Object[][] keyValueCollection) {
        Document document = new Document("documentName", documentName);
        for (Object[] append : keyValueCollection) {
            document = document.append((String) append[0], append[1]);
        }
        return document;
    }

    /**
     * Add a Document to given collection
     * @param collection collections name
     * @param document the document to insert
     */
    public void insertDocument(String collection, Document document) {
        mongoClient.getDatabase(databaseName).getCollection(collection).insertOne(document);
    }

    /**
     * Replace a Document in given collection with a new Document
     * @param collection collections name
     * @param documentName /%/
     * @param newDocument /%/
     */
    public void replaceDocument(String collection, String documentName, Document newDocument) {
        getCollection(collection).deleteOne(getDocument(collection, documentName));
        getCollection(collection).insertOne((newDocument));
    }

    /**
     * Add a property to a specified document in a specified collection
     * @param collection collections name
     * @param documentName /%/
     * @param propertiesToAdd /%/
     */
    public void addPropertyToDocument(String collection, String documentName, Object[][] propertiesToAdd) {
        Document toUpdateDocument = getDocument(collection, documentName);
        for (Object[] append : propertiesToAdd) {
            toUpdateDocument.append((String) append[0], append[1]);
        }
        replaceDocument(collection, documentName, toUpdateDocument);
    }

    /**
     * Replace document property with a new value
     * @param collection collections name
     * @param documentName /%/
     * @param property /%/
     * @param newvalue /%/
     */
    public void replaceProperty(String collection, String documentName, String property, Object newvalue) {
        Document document = getDocument(collection, documentName);
        Bson filter = document;
        Document newDocument = new Document("documentName", documentName);
        for (Map.Entry<String, Object> entry : document.entrySet()) {
            if (entry.getKey().equals(property)) {
                if (entry.getKey().equals(document.get("_id")))
                    continue;
                newDocument.append(entry.getKey(), newvalue);
            } else {
                if (entry.getKey().equals(document.get("_id")))
                    continue;
                newDocument.append(entry.getKey(), entry.getValue());
            }
        }
        getCollection(collection).deleteOne(filter);
        getCollection(collection).insertOne((newDocument));
    }

    /**
     * Get a new Document object with updated Property
     * @param oldDocument /%/
     * @param property /%/
     * @param newvalue /%/
     * @return newDocument /%/
     */
    public Document replaceProperty(Document oldDocument, String property, Object newvalue) {
        Document newDocument = new Document("documentName", oldDocument.getString("documentName"));
        for (Map.Entry<String, Object> entry : oldDocument.entrySet()) {
            if (entry.getKey().equals(property)) {
                if (entry.getKey().equals(oldDocument.get("_id")))
                    continue;
                newDocument.append(entry.getKey(), newvalue);
            } else {
                if (entry.getKey().equals(oldDocument.get("_id")))
                    continue;
                newDocument.append(entry.getKey(), entry.getValue());
            }
        }
        return newDocument;
    }

    /**
     * Get all Documents in a collection
     * @param collectionName collections name
     * @return documents  /%/
     */
    public FindIterable<Document> getAllDocuments(String collectionName) {
        return getCollection(collectionName).find();
    }

    /**
     * Get the count of all documents in a Collection
     * @param collectionName collections name
     * @return long DocumentCount
     */
    @Deprecated
    public long allDocumentsCount(String collectionName) {
        return getCollection(collectionName).countDocuments();
    }

    /**
     * Get a collection out of the database
     * @param collectionName collections name
     * @return collection  /%/
     */
    public MongoCollection<Document> getCollection(String collectionName) {
        if (mongoClient.getDatabase(databaseName).getCollection(collectionName) != null) {
            return mongoClient.getDatabase(databaseName).getCollection(collectionName);
        }
        return null;
    }

    /**
     *  Create a collection in a specified database / collection
     * @param collectionname collections name
     * @param options /%/
     */
    public void createCollection(String collectionname, CreateCollectionOptions options) {
        mongoClient.getDatabase(databaseName).createCollection(collectionname, options);
    }


    /**
     * Get a document out in a specified collection
     * @param collection collections name
     * @param documentName /%/
     * @return document /%/
     */
    public Document getDocument(String collection, String documentName) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("documentName", documentName);
        FindIterable<Document> cursor = mongoClient.getDatabase(databaseName).getCollection(collection)
                .find(whereQuery);
        return cursor.first();
    }

    /**
     * Get a document out in a specified collection by a given key where the value match whereValue
     * @param collection collections name
     * @param byKey /%/
     * @param whereValue /%/
     * @return document /%/
     */
    public Document getDocument(String collection, String byKey, Object whereValue) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put(byKey, whereValue);
        FindIterable<Document> cursor = mongoClient.getDatabase(databaseName).getCollection(collection)
                .find(whereQuery);
        return cursor.first();
    }

    /**
     * delete a document in a specified collection
     * @param collection collections name
     * @param documentName /%/
     * @return bool /%/
     */
    public boolean deleteOne(String collection, String documentName) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("documentName", documentName);
        FindIterable<Document> cursor = mongoClient.getDatabase(databaseName).getCollection(collection)
                .find(whereQuery);

        getCollection(collection).deleteOne(cursor.first());
        return true;
    }

    /**
     * delete a document in a specified collection by a given key where the value match whereValue
     * @param collection collections name
     * @param key /%/
     * @param whereValue /%/
     * @return bool
     */
    public boolean deleteOne(String collection, String key, Object whereValue) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put(key, whereValue);
        FindIterable<Document> cursor = mongoClient.getDatabase(databaseName).getCollection(collection)
                .find(whereQuery);

        getCollection(collection).deleteOne(cursor.first());
        return true;
    }

    /**
     * Check if the client is Connected to the database server
     * @return boolean /%/
     */
    public boolean isConnected() {
        if (getDatabase(databaseName) != null)
            return true;
        else
            return false;
    }

    /**
     * Let the mongoClient connect to database per given mongoUri
     * @param connectionString the MongoDB connectionString
     */
    public void connect(String connectionString) {
        try {
            mongoClient = new MongoClient(new MongoClientURI(connectionString));
        } catch(Exception ex) {ex.printStackTrace();}
    }

    /**
     * Get a database in MongoDB server
     * @param dbName database name
     * @return MongoDatabase
     */
    public MongoDatabase getDatabase(String dbName) {
        return mongoClient.getDatabase(dbName);
    }

    /**
     * close the active session
     */
    public void closeSession() {
        try { mongoClient.close(); } catch(Exception e) {}
    }

    /**
     * Get the mongoClient
     * @return MongoClient the instance of the connected client
     */
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    /**
     * change database name for this handler
     * @param databaseName database name
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

}
