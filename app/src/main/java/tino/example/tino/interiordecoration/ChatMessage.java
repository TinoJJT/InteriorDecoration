package tino.example.tino.interiordecoration;

public class ChatMessage {

    private String mName;
    private String mTimeOfPost;
    private String mMessageContent;
    private String mPhotoUrl;

    public ChatMessage(){

    }

    public ChatMessage(String name, String timeOfPost, String messageContent, String photoUrl) {
        mName = name;
        mTimeOfPost = timeOfPost;
        mMessageContent = messageContent;
        mPhotoUrl = photoUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTimeOfPost() {
        return mTimeOfPost;
    }

    public void setTimeOfPost(String timeOfPost) {
        mTimeOfPost = timeOfPost;
    }

    public String getMessageContent() {
        return mMessageContent;
    }

    public void setMessageContent(String messageContent) {
        mMessageContent = messageContent;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }
}
