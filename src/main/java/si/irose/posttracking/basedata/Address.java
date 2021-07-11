package si.irose.posttracking.basedata;

import java.util.Objects;

public class Address {

    private int id;
    private String city;
    private String street;
    private int houseNumber;
    private char houseDetails;
    private int postId;

    public Address(int id, String city, String street, int houseNumber, char houseDetails, int postId) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.houseDetails = houseDetails;
        this.postId = postId;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public char getHouseDetails() {
        return houseDetails;
    }

    public int getPostId() {
        return postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id == address.id && getHouseNumber() == address.getHouseNumber() && getHouseDetails() == address.getHouseDetails() && getPostId() == address.getPostId() && Objects.equals(getCity(), address.getCity()) && Objects.equals(getStreet(), address.getStreet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getCity(), getStreet(), getHouseNumber(), getHouseDetails(), getPostId());
    }

}
