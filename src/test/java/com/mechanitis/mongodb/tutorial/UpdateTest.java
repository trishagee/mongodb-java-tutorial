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
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mongodb.Sort.ascending;

public class UpdateTest {
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    // Update
    @Test
    public void shouldUpdateCharliesAddress() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        String charliesNewAddress = "987 The New Street";

        // When
        // TODO create query to find Charlie by ID
        Document findCharlie = null;
        // TODO use the query to find charlie and update his street with the new address
        WriteResult resultOfUpdate = null;

        // Then
        assertThat(resultOfUpdate.getResult().isOk(), is(true));

        Document newCharlie = collection.find(findCharlie).into(new ArrayList<Document>()).get(0);
        // this stuff should all be the same
        assertThat((String) newCharlie.get("_id"), is(charlie.getId()));
        assertThat((String) newCharlie.get("name"), is(charlie.getName()));

        // the address street, and only the street, should have changed
        Document address = (Document) newCharlie.get("address");
        assertThat((String) address.get("street"), is(charliesNewAddress));
        assertThat((String) address.get("city"), is(charlie.getAddress().getTown()));
        assertThat((int) address.get("phone"), is(charlie.getAddress().getPhone()));
    }

    //Upsert
    @Test
    public void shouldOnlyInsertDocumentIfItDidNotExistWhenUpsertIsTrue() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        // new person not in the database yet
        Person claire = new Person("claire", "Claire", new Address("1", "Town", 836558493), Collections.<Integer>emptyList());

        // When
        // TODO create query to find Claire by ID
        Document findClaire = null;
        // TODO Perform an update with this new person to show it does NOT get added to the database
        WriteResult resultOfUpdate = null;

        // Then
        assertThat(resultOfUpdate.getResult().isOk(), is(true));
        // without upsert this should not have been inserted
        assertThat(collection.find(findClaire).count(), is(0L));


        // When
        // TODO Perform an update with this new person to show it DOES get added to the database
        WriteResult resultOfUpsert = null;

        // Then
        assertThat(resultOfUpsert.getResult().isOk(), is(true));

        Document newClaireDocument = collection.find(findClaire).into(new ArrayList<Document>()).get(0);
        // all values should have been updated to the new object values
        assertThat((String) newClaireDocument.get("_id"), is(claire.getId()));
    }

    //Multi=false
    @Test
    public void shouldOnlyUpdateTheFirstDocumentMatchingTheQuery() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        Person emily = new Person("emily", "Emily", new Address("5", "Some Town", 646383), Collections.<Integer>emptyList());
        collection.insert(emily.toDocument());

        // When
        // TODO create query to find everyone with 'LondonTown' as their city
        Document findLondoners = null;
        assertThat("There should be two Londoners in the database", collection.find(findLondoners).count(), is(2L));

        // TODO update only the first Londonder here to have a new field, "wasUpdated", with a value of true


        // Then
        List<Document> londoners = collection.find(findLondoners).sort(ascending("_id")).into(new ArrayList<Document>());
        assertThat(londoners.size(), is(2));

        assertThat((String) londoners.get(0).get("name"), is(bob.getName()));
        assertThat((boolean) londoners.get(0).get("wasUpdated"), is(true));

        assertThat((String) londoners.get(1).get("name"), is(charlie.getName()));
        assertThat(londoners.get(1).get("wasUpdated"), is(nullValue()));
    }

    //Multi=true
    @Test
    public void shouldUpdateEveryoneLivingInLondon() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        Person emily = new Person("emily", "Emily", new Address("5", "Some Town", 646383), Collections.<Integer>emptyList());
        collection.insert(emily.toDocument());

        // When
        // TODO create query to find everyone with 'LondonTown' as their city
        Document findLondoners = null;
        assertThat("There should be two Londoners in the database", collection.find(findLondoners).count(), is(2L));

        // TODO update all Londonders here to have a new field, "wasUpdated", with a value of true

        // Then
        List<Document> londoners = collection.find(findLondoners).sort(ascending("_id")).into(new ArrayList<Document>());
        assertThat(londoners.size(), is(2));

        Document firstLondoner = londoners.get(0);
        assertThat((String) firstLondoner.get("name"), is(bob.getName()));
        assertThat((boolean) firstLondoner.get("wasUpdated"), is(true));

        Document secondLondoner = londoners.get(1);
        assertThat((String) secondLondoner.get("name"), is(charlie.getName()));
        assertThat((boolean) secondLondoner.get("wasUpdated"), is(true));
    }

    // BONUS
    @Test
    public void shouldReplaceWholeDocumentWithNewOne() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        // When
        Person updatedCharlieObject = new Person("charlie", "Charles the Suave", new Address("A new street", "GreatCity", 7654321),
                                                 Collections.<Integer>emptyList());
        // TODO create query to find Charlie by ID
        Document findCharlie = null;
        // TODO do an update replacing the whole previous Document with the new one
        WriteResult resultOfUpdate = null;

        // Then
        assertThat(resultOfUpdate.getResult().isOk(), is(true));

        Document newCharlieDocument = collection.find(findCharlie).into(new ArrayList<Document>()).get(0);
        // all values should have been updated to the new object values
        assertThat((String) newCharlieDocument.get("_id"), is(updatedCharlieObject.getId()));
        assertThat((String) newCharlieDocument.get("name"), is(updatedCharlieObject.getName()));
        assertThat((List<Integer>) newCharlieDocument.get("books"), is(updatedCharlieObject.getBookIds()));
        Document address = (Document) newCharlieDocument.get("address");
        assertThat((String) address.get("street"), is(updatedCharlieObject.getAddress().getStreet()));
        assertThat((String) address.get("city"), is(updatedCharlieObject.getAddress().getTown()));
        assertThat((int) address.get("phone"), is(updatedCharlieObject.getAddress().getPhone()));
    }

    //BONUS
    @Test
    public void shouldAddAnotherBookToBobsBookIds() {
        // Given
        Person bob = new Person("bob", "Bob The Amazing", new Address("123 Fake St", "LondonTown", 1234567890), asList(27464, 747854));
        collection.insert(bob.toDocument());

        Person charlie = new Person("charlie", "Charles", new Address("74 That Place", "LondonTown", 1234567890), asList(1, 74));
        collection.insert(charlie.toDocument());

        // When
        // TODO create query to find Bob by ID
        Document findBob = null;
        // TODO update the only Bob document to add the ID '66' to the array of Book IDs

        // Then
        Document newBob = collection.find(findBob).into(new ArrayList<Document>()).get(0);

        assertThat((String) newBob.get("name"), is(bob.getName()));

        // there should be another item in the array
        List<Integer> bobsBooks = (List<Integer>) newBob.get("books");
        // note these are  ordered
        assertThat(bobsBooks.size(), is(3));
        assertThat(bobsBooks.get(0), is(27464));
        assertThat(bobsBooks.get(1), is(747854));
        assertThat(bobsBooks.get(2), is(66));
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
