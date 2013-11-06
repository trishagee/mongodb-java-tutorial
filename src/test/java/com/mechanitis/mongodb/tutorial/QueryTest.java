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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class QueryTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Test
    public void shouldFindAllDocumentsWithTheNameCharles() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        // When
        // TODO create the correct query to find Charlie by name
        Document query = null;
        // TODO use this query to get a List of matching Documents from the database
        List<Document> results = null;

        // Then
        assertThat(results.size(), is(1));
        assertThat((String) results.get(0).get("_id"), is(charlie.getId()));
    }

    @Test
    public void shouldFindAllDocumentsWithTheNameCharlesAndOnlyReturnNameAndId() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        // When
        // TODO create the correct query to find Charlie by name (see above)
        Document query = null;
        // TODO use this query, combined with the "fields" selector, to get a list of result documents with only the name and ID fields
        List<Document> results = null;

        // Then
        assertThat(results.size(), is(1));
        Document theOnlyResult = results.get(0);
        //ID is always included unless specificatlly excluded
        assertThat((String) theOnlyResult.get("_id"), is(charlie.getId()));
        assertThat((String) theOnlyResult.get("name"), is(charlie.getName()));
        assertThat(theOnlyResult.get("address"), is(nullValue()));
        assertThat(theOnlyResult.get("books"), is(nullValue()));
    }

    //BONUS - same as previous test but excluding a single field, not including one
    @Test
    public void shouldFindAllDocumentsWithTheNameCharlesAndExcludeAddressInReturn() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        // When
        // TODO create the correct query to find Charlie by name (see above)
        Document query = null;
        // TODO use this query, combined with the "fields" selector, to get a list of result documents without address subdocument
        List<Document> results = null;

        // Then
        assertThat(results.size(), is(1));
        Document theOnlyResult = results.get(0);
        assertThat((String) theOnlyResult.get("_id"), is(charlie.getId()));
        assertThat((String) theOnlyResult.get("name"), is(charlie.getName()));
        assertThat(theOnlyResult.get("address"), is(nullValue()));
        assertThat((List<Integer>) theOnlyResult.get("books"), is(charlie.getBookIds()));
    }

    //BONUS
    @Test
    public void shouldReturnADocumentWithAPhoneNumberLessThan1000000000() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 987654321), asList(27464, 747854));
        collection.insert(bob.toDocument());

        // When
        //TODO build up a query which checks the numeric value
        Document query = null;
        // TODO use this query to get a List of matching Documents from the database
        List<Document> results = null;

        assertThat(results.size(), is(1));
        assertThat((String) results.get(0).get("_id"), is(bob.getId()));
    }

    //BONUS
    @Test
    public void shouldReturnDocuments3to9Of20DocumentsUsingSkipAndLimit() {
        // Given
        for (int i = 0; i < 20; i++) {
            collection.insert(new Document("name", "person" + i).append("someIntValue", i));
        }

        // When
        // TODO no need for a query, just combine the find with the other operators available
        List<Document> results = null;

        // Then
        assertThat(results.size(), is(7));
        assertThat((int) results.get(0).get("someIntValue"), is(3));
        assertThat((int) results.get(1).get("someIntValue"), is(4));
        assertThat((int) results.get(2).get("someIntValue"), is(5));
        assertThat((int) results.get(3).get("someIntValue"), is(6));
        assertThat((int) results.get(4).get("someIntValue"), is(7));
        assertThat((int) results.get(5).get("someIntValue"), is(8));
        assertThat((int) results.get(6).get("someIntValue"), is(9));
    }

    @Before
    public void setUp() throws UnknownHostException {
        MongoClient mongoClient = MongoClients.create(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDatabase("JAXDatabase");
        collection = database.getCollection("people");
        collection = database.getCollection("people");
    }

    @After
    public void tearDown() {
        database.tools().drop();
    }
}
