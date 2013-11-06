package com.mechanitis.mongodb.tutorial;

import com.mechanitis.mongodb.tutorial.person.Address;
import com.mechanitis.mongodb.tutorial.person.Person;
import org.junit.Test;
import org.mongodb.Document;
import org.mongodb.MongoClient;
import org.mongodb.MongoClientURI;
import org.mongodb.MongoClients;
import org.mongodb.MongoCollection;
import org.mongodb.MongoDatabase;

import java.net.UnknownHostException;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InsertTest {
    @Test
    public void shouldTurnAPersonIntoADocument() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));

        // When
        Document bobAsDocument = bob.toDocument();

        // Then
        String expectedDocument = "{" +
                                  " \"_id\" : \"bob\"," +
                                  " \"name\" : \"Bob The Amazing\"," +
                                  " \"address\" : {" +
                                     " \"street\" : \"123 Fake St\"," +
                                     " \"city\" : \"LondonTown\"," +
                                     " \"phone\" : 1234567890" +
                                  " }," +
                                  " \"books\" : [27464, 747854] " +
                                  "}";
        assertThat(bobAsDocument.toString(), is(expectedDocument));
    }

    @Test
    public void shouldBeAbleToSaveAPerson() throws UnknownHostException {
        // Given
        MongoClient mongoClient = MongoClients.create(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("JAXDatabase");
        MongoCollection<Document> collection = database.getCollection("people");

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));

        // When
        // TODO: insert Charlie into the collection

        // Then
        assertThat(collection.find().count(), is(1L));

        // Clean up
        database.tools().drop();
    }

}
