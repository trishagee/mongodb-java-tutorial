package com.mechanitis.mongodb.tutorial;

import org.junit.Test;
import org.mongodb.Document;
import org.mongodb.MongoClient;
import org.mongodb.MongoCollection;
import org.mongodb.MongoDatabase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ConnectionTest {
    @Test
    public void shouldCreateANewMongoClientConnectedToLocalhost() throws Exception {
        // When
        // TODO: get/create the MongoClient
        MongoClient mongoClient = null;

        // Then
        assertThat(mongoClient, is(notNullValue()));
    }

    @Test
    public void shouldGetADatabaseFromTheMongoClient() throws Exception {
        // Given
        // TODO any setup

        // When
        //TODO get the database from the client
        MongoDatabase database = null;

        // Then
        assertThat(database, is(notNullValue()));
    }

    @Test
    public void shouldGetACollectionFromTheDatabase() throws Exception {
        // Given
        // TODO any setup

        // When
        // TODO get collection
        MongoCollection<Document> collection = null;

        // Then
        assertThat(collection, is(notNullValue()));
    }

}
