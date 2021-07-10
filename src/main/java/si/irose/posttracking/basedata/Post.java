package si.irose.posttracking.basedata;

public class Post {

    private int postId;
    private String post;

    public Post(int postId, String post) {
        this.postId = postId;
        this.post = post;
    }

    public String getPost() {
        return post;
    }

}
