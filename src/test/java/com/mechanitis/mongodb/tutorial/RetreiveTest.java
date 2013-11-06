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
import org.mongodb.MongoView;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

@SuppressWarnings("unchecked")
public class RetreiveTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = MongoClients.create(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("JAXDatabase");
        collection = database.getCollection("people");
    }

    // Examples from the slides
    @Test
    public void shouldConstructListOfPersonFromQueryResults() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        // When
        List<Person> results = new ArrayList<>();
        // TODO populate the results from the Collection

        // Then
        assertThat(results.size(), is(2));
        assertThat(results, contains(charlie, bob));
    }

    //    @Test
    //    public void shouldConstructListOfPersonFromQueryResultsJava8() {
    //        // Given
    //        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
    //        collection.insert(charlie.toDocument());
    //
    //        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
    //        collection.insert(bob.toDocument());
    //
    //        // When
    //        List<Person> results = new ArrayList<>();
    //        // TODO: lambda version here
    //
    //        // Then
    //        assertThat(results.size(), is(2));
    //        assertThat(results, contains(charlie, bob));
    //    }

    // Other ways of getting stuff out of the database

    @Test
    public void shouldRetrieveBobFromTheDatabase() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        // When
        // TODO: get this from querying the collection.  Hint: you can find just one
        Document result = null;

        // Then
        assertThat((String) result.get("_id"), is("bob"));
    }

    @Test
    public void shouldRetrieveEverythingFromTheDatabaseAsDocuments() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        // When
        // TODO: get a list of Documents from the collection
        List<Document> results = null;

        // Then
        assertThat(results.size(), is(2));
        assertThat((String) results.get(0).get("_id"), is("bob"));
        assertThat((String) results.get(1).get("_id"), is("charlie"));
    }

    @Test
    public void shouldSearchForAndReturnOnlyBobFromTheDatabaseWhenMorePeopleExist() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        // When
        Document query = null; // TODO create the query object
        MongoView<Document> mongoView = collection.find(query);

        // Then
        assertThat(mongoView.count(), is(1L));
        assertThat((String) mongoView.getOne().get("name"), is("Bob The Amazing"));
    }

    @After
    public void tearDown() {
        database.tools().drop();
    }
}
