package si.irose.posttracking.post;

public class Post {

    private int post_id;
    private String post;

    public Post(int post_id, String post) {
        this.post_id = post_id;
        this.post = post;
    }

    @Override
    public String toString() {
        return post_id + "," + post;
    }

}
