package com.mechanitis.mongodb.tutorial.person;

public class Address {
    private final String street;
    private final String town;
    private final int phone;

    public Address(final String street, final String town, final int phone) {
        this.street = street;
        this.town = town;
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public String getTown() {
        return town;
    }

    public int getPhone() {
        return phone;
    }

    /* Below this: hashCode, equals and toString all useful for testing */

    @Override
    public String toString() {
        return "Address{"
               + "street='" + street + '\''
               + ", town='" + town + '\''
               + ", phone=" + phone
               + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Address address = (Address) o;

        if (phone != address.phone) {
            return false;
        }
        if (!street.equals(address.street)) {
            return false;
        }
        if (!town.equals(address.town)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = street.hashCode();
        result = 31 * result + town.hashCode();
        result = 31 * result + phone;
        return result;
    }
}
