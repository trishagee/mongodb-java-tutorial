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
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FindAndModifyTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Test
    public void shouldUpdateDocumentAndReturnOriginalDocument() {
        // Given
        Document order1 = new Document("orderNo", 1L).append("product", "Some Book").append("status", "PENDING");
        collection.insert(order1);

        // When
        // TODO create query to find order1 by ID
        Document query = null;
        // TODO use one of the find and modify methods combined with the query to change the status of order1 to "APPROVED" but return
        // the original document
        Document resultOfFindAndModify = null;

        // Then
        assertThat((String) resultOfFindAndModify.get("status"), is("PENDING"));
        assertThat((String) collection.find(query).getOne().get("status"), is("APPROVED"));
    }

    @Test
    public void shouldUpdateDocumentAndReturnNewDocument() {
        // Given
        Document order1 = new Document("orderNo", 1L).append("product", "Some Book").append("quantity", 2);
        collection.insert(order1);

        // When
        // TODO create query to find order1 by ID
        Document query = null;
        // TODO use one of the find and modify methods combined with the query to increment the quantity of order1 by one and return
        // the updated document
        Document resultOfFindAndModify = null;

        // Then
        assertThat((int) resultOfFindAndModify.get("quantity"), is(3));
    }

    @Test
    public void shouldReplaceDocumentAndReturnTheOriginalDocument() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        // When
        Person updatedCharlieObject = new Person("charlie", "Charles the Suave",
                                                 new Address("A new street", "GreatCity", 7654321), Collections.<Integer>emptyList());
        // TODO create query to find Charlie by ID
        Document findCharlie = null;
        // TODO call one of the find and modify methods to replace the existing Charlie object with the new one, but return the original
        Document resultOfFindAndModify = null;

        // Then
        // the returned object should be the original one
        assertThat((String) resultOfFindAndModify.get("_id"), is(charlie.getId()));
        assertThat((String) resultOfFindAndModify.get("name"), is(charlie.getName()));
        assertThat((List<Integer>) resultOfFindAndModify.get("books"), is(charlie.getBookIds()));
        Document address = (Document) resultOfFindAndModify.get("address");
        assertThat((String) address.get("street"), is(charlie.getAddress().getStreet()));
        assertThat((String) address.get("city"), is(charlie.getAddress().getTown()));
        assertThat((int) address.get("phone"), is(charlie.getAddress().getPhone()));

        // but the change should have been made in the database
        Document charlieInDatabase = collection.find(findCharlie).getOne();
        assertThat((String) charlieInDatabase.get("_id"), is(updatedCharlieObject.getId()));
        assertThat((String) charlieInDatabase.get("name"), is(updatedCharlieObject.getName()));
        assertThat((List<Integer>) charlieInDatabase.get("books"), is(updatedCharlieObject.getBookIds()));
        address = (Document) charlieInDatabase.get("address");
        assertThat((String) address.get("street"), is(updatedCharlieObject.getAddress().getStreet()));
        assertThat((String) address.get("city"), is(updatedCharlieObject.getAddress().getTown()));
        assertThat((int) address.get("phone"), is(updatedCharlieObject.getAddress().getPhone()));
    }

    @Test
    public void shouldReplaceDocumentAndReturnTheNewDocument() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        Person originalCharlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(originalCharlie.toDocument());

        // When
        Person updatedCharlieObject = new Person("charlie", "Charles the Suave",
                                                 new Address("A new street", "GreatCity", 7654321), Collections.<Integer>emptyList());
        // TODO create query to find Charlie by ID
        Document findCharlie = null;
        // TODO call one of the find and modify methods to replace the existing Charlie object with the new one and return the new one
        Document resultOfFindAndModify = collection.find(findCharlie)
                                                   .replaceOneAndGet(updatedCharlieObject.toDocument());

        // Then
        // all values should have been updated to the new object values
        assertThat((String) resultOfFindAndModify.get("_id"), is(updatedCharlieObject.getId()));
        assertThat((String) resultOfFindAndModify.get("name"), is(updatedCharlieObject.getName()));
        assertThat((List<Integer>) resultOfFindAndModify.get("books"), is(updatedCharlieObject.getBookIds()));
        Document address = (Document) resultOfFindAndModify.get("address");
        assertThat((String) address.get("street"), is(updatedCharlieObject.getAddress().getStreet()));
        assertThat((String) address.get("city"), is(updatedCharlieObject.getAddress().getTown()));
        assertThat((int) address.get("phone"), is(updatedCharlieObject.getAddress().getPhone()));
    }


    @Test
    public void shouldRemoveDocumentAndReturnTheOriginalDocument() {
        // Given
        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        // When
        // TODO create query to find Charlie by ID
        Document findCharlie = null;
        // TODO call one of the find and modify methods to remove Charlie and return the document that was removed
        Document resultOfFindAndModify = null;

        // Then
        // the returned object should be the document before it was deleted (frankly the other way around would be dumb)
        assertThat((String) resultOfFindAndModify.get("_id"), is(charlie.getId()));
        assertThat((String) resultOfFindAndModify.get("name"), is(charlie.getName()));
        assertThat((List<Integer>) resultOfFindAndModify.get("books"), is(charlie.getBookIds()));
        Document address = (Document) resultOfFindAndModify.get("address");
        assertThat((String) address.get("street"), is(charlie.getAddress().getStreet()));
        assertThat((String) address.get("city"), is(charlie.getAddress().getTown()));
        assertThat((int) address.get("phone"), is(charlie.getAddress().getPhone()));

        // but obviously it should have been deleted in the database
        assertThat(collection.find(findCharlie).count(), is(0L));
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
