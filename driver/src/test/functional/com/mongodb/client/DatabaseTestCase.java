/*
 * Copyright (c) 2008-2014 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mongodb.client;

import com.mongodb.MongoNamespace;
import com.mongodb.client.test.CollectionHelper;
import com.mongodb.codecs.DocumentCodec;
import com.mongodb.connection.ServerHelper;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.junit.After;
import org.junit.Before;
import org.mongodb.Document;

import static com.mongodb.Fixture.getDefaultDatabaseName;
import static com.mongodb.Fixture.getMongoClient;
import static com.mongodb.Fixture.getPrimary;

public class DatabaseTestCase {
    //For ease of use and readability, in this specific case we'll allow protected variables
    //CHECKSTYLE:OFF
    protected MongoDatabase database;
    protected MongoCollection<Document> collection;
    //CHECKSTYLE:ON

    @Before
    public void setUp() {
        database = getMongoClient().getDatabase(getDefaultDatabaseName());
        collection = database.getCollection(getClass().getName());
        collection.tools().drop();
    }

    @After
    public void tearDown() {
        if (collection != null) {
            collection.tools().drop();
        }
        try {
            ServerHelper.checkPool(getPrimary());
        } catch (InterruptedException e) {
            // ignore
        }
    }

    protected String getDatabaseName() {
        return database.getName();
    }

    protected String getCollectionName() {
        return collection.getNamespace().getCollectionName();
    }

    protected MongoNamespace getNamespace() {
        return collection.getNamespace();
    }

    protected CollectionHelper<Document> getCollectionHelper() {
        return new CollectionHelper<Document>(new DocumentCodec(), getNamespace());
    }

    protected BsonDocument wrap(final Document document) {
        return new BsonDocumentWrapper<Document>(document, new DocumentCodec());
    }
}