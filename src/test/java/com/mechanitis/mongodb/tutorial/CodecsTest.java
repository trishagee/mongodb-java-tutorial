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

import java.net.UnknownHostException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CodecsTest {
    private MongoDatabase database;

    @Test
    public void shouldUseCodecsToSimplifyRetrieval() {
        // Given
        // TODO: setup a collection of Person
        MongoCollection<Person> peopleCollection = null;

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        peopleCollection.insert(charlie);

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        peopleCollection.insert(bob);

        // When
        Document query = new Document("name", "Charles");
        //TODO: query the collection of Person objects for people with the name Charles
        List<Person> results = null;

        // Then
        assertThat(results.size(), is(1));
        assertThat(results.get(0), is(charlie));
    }

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = MongoClients.create(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("JAXDatabase");
    }

    @After
    public void tearDown() {
        database.tools().drop();
    }
}
