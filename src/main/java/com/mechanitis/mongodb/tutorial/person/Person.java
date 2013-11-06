package com.mechanitis.mongodb.tutorial.person;

import com.mechanitis.mongodb.tutorial.person.Address;
import org.mongodb.ConvertibleToDocument;
import org.mongodb.Document;

import java.util.List;

public class Person implements ConvertibleToDocument{
    private final String id;
    private final String name;
    private final Address address;
    private final List<Integer> bookIds;

    public Person(final String id, final String name, final Address address, final List<Integer> bookIds) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.bookIds = bookIds;
    }

    @Override
    public Document toDocument() {
        // TODO: implement this
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public List<Integer> getBookIds() {
        return bookIds;
    }

}
