package si.irose.posttracking.address;

public class Address {

    private int id;
    private String city;
    private String street;
    private int hs;
    private char hd;
    private int post_id;

    public Address(int id, String city, String street, int hs, char hd, int post_id) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.hs = hs;
        this.hd = hd;
        this.post_id = post_id;
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getHs() {
        return hs;
    }

    public char getHd() {
        return hd;
    }

    public int getPost_id() {
        return post_id;
    }

    @Override
    public String toString() {
        return city + ", " + street + ", " + hs + "," + hd + ", " + post_id;
    }

}
