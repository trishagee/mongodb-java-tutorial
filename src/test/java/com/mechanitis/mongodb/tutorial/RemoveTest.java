package com.mechanitis.mongodb.tutorial;

import com.mechanitis.mongodb.tutorial.person.Address;
import com.mechanitis.mongodb.tutorial.person.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.Document;
import org.mongodb.MongoClient;
import org.mongodb.MongoClientURI;
import org.mongodb.MongoClients;
import org.mongodb.MongoCollection;
import org.mongodb.MongoDatabase;
import org.mongodb.WriteResult;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class RemoveTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Test
    public void shouldDeleteOnlyCharlieFromTheDatabase() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        Person emily = new Person("emily", "Emily", new Address("5", "Some Town", 646383), Collections.<Integer>emptyList());
        collection.insert(emily.toDocument());

        // When
        // TODO create a query to find charlie by ID
        Document query = null;
        // TODO execute the remove
        WriteResult resultOfRemove = null;

        // Then
        assertThat(resultOfRemove.getResult().isOk(), is(true));

        ArrayList<Document> remainingPeople = collection.find().into(new ArrayList<Document>());
        assertThat(remainingPeople.size(), is(2));

        for (final Document remainingPerson : remainingPeople) {
            assertThat((String) remainingPerson.get("_id"), is(not(charlie.getId())));
        }
    }

    @Test
    public void shouldDeleteAllPeopleWhoLiveInLondon() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        Person emily = new Person("emily", "Emily", new Address("5", "Some Town", 646383), Collections.<Integer>emptyList());
        collection.insert(emily.toDocument());

        // When
        // TODO create the query to check the city field inside the address subdocument for 'LondonTown'
        Document query = null;
        // TODO execute the remove
        WriteResult resultOfRemove = null;

        // Then
        assertThat(resultOfRemove.getResult().isOk(), is(true));

        ArrayList<Document> remainingPeople = collection.find().into(new ArrayList<Document>());
        assertThat(remainingPeople.size(), is(1));

        assertThat((String) remainingPeople.get(0).get("_id"), is(emily.getId()));
    }

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = MongoClients.create(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("JAXDatabase");
        collection = database.getCollection("people");
    }

    @After
    public void tearDown() {
        database.tools().drop();
    }
}
