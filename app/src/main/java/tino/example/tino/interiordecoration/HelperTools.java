package tino.example.tino.interiordecoration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HelperTools extends AppCompatActivity {
    public static final int RC_PHOTO_PICKER = 0;
    public static final int RC_SIGN_IN = 1;


    static GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};

    public static String mUserName;

    static List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());
    private static StorageReference mRootStorageReference;
    private static StorageReference mChatPhotosStorageReference;
    private static StorageReference mRequestPhotosStorageReference;

    private static DatabaseReference mRootReference;
    private static DatabaseReference mMessagesDatabaseReference;
    private static DatabaseReference mRequestDatabaseReference;
    private static DatabaseReference mPublicMessagesDatabaseReference;
    private static DatabaseReference mLauriRequestDatabaseReference;
    private static DatabaseReference mPaulaRequestDatabaseReference;


    private static FirebaseDatabase mFirebaseDatabase;
    private static FirebaseStorage mFirebaseStorage;
    private static ValueEventListener mChildEventListener;

    private static List<DecorationRequest> mDecorationRequests = new ArrayList<>();
    private static List<ChatMessage> mChatMessages = new ArrayList<>();
    private static List<DecorationRequest> mFinalDecorationRequets;
    private static List<ChatMessage> mFinalChatRequests;
    private static String mRequestKey;


    public static void logInAndGetUserName(AppCompatActivity activity){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mRootReference = mFirebaseDatabase.getInstance().getReference();
        mMessagesDatabaseReference = mRootReference.child("messages");
        mPublicMessagesDatabaseReference = mMessagesDatabaseReference.child("public_messages");


        mRequestDatabaseReference = mRootReference.child("requests");
        mLauriRequestDatabaseReference = mRootReference.child("lauri_laurela");
        mPaulaRequestDatabaseReference = mRootReference.child("paula_pulla");

        mRootStorageReference = mFirebaseStorage.getInstance().getReference();
        mChatPhotosStorageReference = mRootStorageReference.child("chat_photos");
        mRequestPhotosStorageReference = mRootStorageReference.child("request_photos");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        activity.startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                RC_SIGN_IN);
    }

    public static void sendMessage(String content, DecorationRequest decorationRequest, String privateMessage){
        String requestKey = decorationRequest.mKey;
        ChatMessage chatMessage;
        DatabaseReference requestReference;
        if(privateMessage!=null && !privateMessage.equals("")){
            if(privateMessage.equals("lauri_laurela")){
                requestReference = mLauriRequestDatabaseReference.child(requestKey).child("messages").push();
                chatMessage = new ChatMessage("Lauri Laurela", getTime(),
                        "Hello. Thanks for choosing me as your private interior decorator. " +
                                "Based on your given input, your fee is: " +
                                Integer.parseInt(decorationRequest.getArea())*5 +
                        " euros. Please send the amount to this bank address: \n" +
                                "****-****-****-****-****\n" +
                                "and I will respond within 2 business days", null
                        );
            } else {
                requestReference = mPaulaRequestDatabaseReference.child(requestKey).child("messages").push();
                chatMessage = new ChatMessage("Paula Pulla", getTime(),
                        "Hello. Thanks for choosing me as your private interior decorator. " +
                                "Based on your given input, your fee is: " +
                                Integer.parseInt(decorationRequest.getArea())*5 +
                                " euros. Please send the amount to this bank address: \n" +
                                "****-****-****-****-****\n" +
                                "and I will respond within 2 business days", null
                );
            }
        } else {
            chatMessage = new ChatMessage(mUserName, getTime(), content, null);
            requestReference = mRequestDatabaseReference.child(requestKey).child("messages").push();
        }
        DatabaseReference requestMessagesDatabaseReference = mPublicMessagesDatabaseReference.child(requestKey);
        DatabaseReference finalReference = requestMessagesDatabaseReference.push();
        String finalKey = finalReference.getKey();
        finalReference.setValue(chatMessage);
        requestReference.setValue(finalKey);
    }

    public static void getMessages(String requestKey, final Context context, final int layout, final ListView listView){
        readChatData(requestKey, new MyChatCallback() {
            @Override
            public void onCallback(List<ChatMessage> chatMessages) {
                mFinalChatRequests = chatMessages;
                listView.setAdapter(new MessageAdapter(context, layout, mFinalChatRequests));
            }
        });
    }

    public static void readChatData(String requestKey, final MyChatCallback myChatCallback){
        DatabaseReference chatDatabaseReference = mPublicMessagesDatabaseReference.child(requestKey);
        chatDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatMessages.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setName(ds.child("name").getValue(String.class));
                    chatMessage.setMessageContent(ds.child("messageContent").getValue(String.class));
                    chatMessage.setPhotoUrl(ds.child("photoUrl").getValue(String.class));
                    chatMessage.setTimeOfPost(ds.child("timeOfPost").getValue(String.class));
                    mChatMessages.add(chatMessage);
                }
                myChatCallback.onCallback(mChatMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public interface MyChatCallback {
        void onCallback(List<ChatMessage> chatMessages);
    }

    public static void getPublicRequests(final Context context, final int layout, final ListView listView, String priv){
        readRequestData(priv, new MyRequestCallback() {
            @Override
            public void onCallback(List<DecorationRequest> decorationRequests) {
                mFinalDecorationRequets = decorationRequests;
                listView.setAdapter(new RequestAdapter(context, layout, mFinalDecorationRequets));
            }
        });
    }

    public static void readRequestData(String priv, final MyRequestCallback myRequestCallback){
        DatabaseReference readDatabaseReference;
        if(priv!=null){
            if (priv.equals("lauri_laurela")) {
                readDatabaseReference = mLauriRequestDatabaseReference;
            } else{
                readDatabaseReference = mPaulaRequestDatabaseReference;
            }
        } else {
            readDatabaseReference = mRequestDatabaseReference;
        }
        readDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDecorationRequests.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DecorationRequest decorationRequest = new DecorationRequest();
                    decorationRequest.setKey(ds.getKey());
                    decorationRequest.setName(ds.child("name").getValue(String.class));
                    decorationRequest.setArea(ds.child("area").getValue(String.class));
                    decorationRequest.setRooms(ds.child("rooms").getValue(String.class));
                    decorationRequest.setBudget(ds.child("budget").getValue(String.class));
                    decorationRequest.setDescription(ds.child("description").getValue(String.class));
                    decorationRequest.setTime(ds.child("time").getValue(String.class));
                    DataSnapshot photoSnapshot = ds.child("photoUrls");
                    Log.e("Toka", Long.toString(dataSnapshot.getChildrenCount()));
                    List<String> photoUrls = new ArrayList<>();
                    for (int i = 0; i < photoSnapshot.getChildrenCount(); i++) {
                        photoUrls.add(photoSnapshot.child("photo" + i).getValue(String.class));
                        }
                        decorationRequest.setPhotoUrls(photoUrls);
                        mDecorationRequests.add(decorationRequest);
                    }
                    myRequestCallback.onCallback(mDecorationRequests);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
    }

    public interface MyRequestCallback {
        void onCallback(List<DecorationRequest> decorationRequests);
    }

    public static void sendHelpRequest(final AppCompatActivity activity, DecorationRequest decorationRequest, String hire){
        List<Uri> pictures = decorationRequest.inUriFormat();
        decorationRequest.setPhotoUrls(new ArrayList<String>());
        decorationRequest.setTime(getTime());
        final DatabaseReference keyRef;
        if (mPaulaRequestDatabaseReference.getKey().equals(hire)) {
            keyRef = mPaulaRequestDatabaseReference.push();
        } else if(mLauriRequestDatabaseReference.getKey().equals(hire)) {
            keyRef = mLauriRequestDatabaseReference.push();
        } else {
            keyRef = mRequestDatabaseReference.push();
        }
        String requestKey = keyRef.getKey();
        decorationRequest.setKey(requestKey);
        if(hire!=null && !hire.equals("")){
            sendMessage(null, decorationRequest, hire);
        }
        keyRef.setValue(decorationRequest);
        for(int i = 0; i < pictures.size(); i++){
            final String value = Integer.toString(i);
            final StorageReference photoRef = mRequestPhotosStorageReference.child(pictures.get(i).getLastPathSegment());

            Task<Uri> urlTask = photoRef.putFile(pictures.get(i)).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    } else {
                        return photoRef.getDownloadUrl();
                    }
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        DatabaseReference photoReference = keyRef.child("photoUrls").child("photo"+value);
                        photoReference.setValue(downloadUri.toString());
                    } else {
                        Toast.makeText(activity.getApplicationContext(), "Failure", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    public static void selectAndSendImage(AppCompatActivity activity, String requestKey){
        mRequestKey = requestKey;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        activity.startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
        Log.e("TAG" , "YOURE SHIT 1");
    }

    public static void setupRequestLayout(TextView nameTextView, TextView timeTextView,
                                          TextView roomsTextView, TextView budgetTextView,
                                          TextView areaTextView, TextView descriptionTextView,
                                          final List<ImageView> imageViews,
                                          final DecorationRequest decorationRequest,
                                          Context context){
        nameTextView.setText(decorationRequest.getName());
        timeTextView.setText(decorationRequest.getTime());
        roomsTextView.setText(decorationRequest.getRooms());
        budgetTextView.setText(decorationRequest.getBudget());
        areaTextView.setText(decorationRequest.getArea());
        descriptionTextView.setText(decorationRequest.getDescription());
        for(int i=0; i<decorationRequest.inUriFormat().size(); i++){
            final int finalInt = i;
            final Context finalContext = context;
            Glide.with(imageViews.get(i).getContext())
                    .load(decorationRequest.inUriFormat().get(i))
                    .into(imageViews.get(i));
            imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), EnlargedImageActivity.class);
                    intent.putExtra("3", decorationRequest);
                    intent.putExtra("4", finalInt);
                    finalContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void finalizePhotoSend(Uri selectedImageUri){
        Log.e("TAG" , "YOURE SHIT 2.1");
        Log.e("TAG" , "YOURE SHIT 2.3");
        final StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
        Log.e("TAG" , "YOURE SHIT 3");
        Task<Uri> urlTask = photoRef.putFile(selectedImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()) {
                    Log.e("TAG" , "YOURE SHIT 4");
                    throw task.getException();
                } else {
                    Log.e("TAG" , "YOURE SHIT 5");
                    return photoRef.getDownloadUrl();
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Log.e("TAG" , "YOURE SHIT 6");
                    Uri downloadUri = task.getResult();
                    ChatMessage chatMessage =
                            new ChatMessage(mUserName, getTime(), null, downloadUri.toString());
                    Log.e("TAG" , "YOURE SHIT 7");
                    DatabaseReference photoMessageReference = mPublicMessagesDatabaseReference.child(mRequestKey).push();
                    String chatphotoKey = photoMessageReference.getKey();
                    photoMessageReference.setValue(chatMessage);
                    mRequestDatabaseReference.child(mRequestKey).child("messages").push().setValue(chatphotoKey);
                    Log.e("TAG" , "YOURE SHIT 8");
                } else {
                    Log.e("TAG" , "YOURE SHIT 9");
                }
            }
        });
    }

    private static String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm MMM dd yyyy");
        String date = sdf.format(new Date());
        return date;
    }

}
