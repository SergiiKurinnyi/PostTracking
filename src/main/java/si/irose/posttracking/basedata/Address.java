package si.irose.posttracking.basedata;

public class Address {

    private int id;
    private String city;
    private String street;
    private int hs;
    private char hd;
    private int postId;

    public Address(int id, String city, String street, int hs, char hd, int postId) {
        this.id = id;
        this.city = city;
        this.street = street;
        this.hs = hs;
        this.hd = hd;
        this.postId = postId;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        if (hd == ' ') {
            return city + ", " + street + ", " + hs + ", " + postId;
        }
        return city + ", " + street + ", " + hs + hd + ", " + postId;
    }

}
