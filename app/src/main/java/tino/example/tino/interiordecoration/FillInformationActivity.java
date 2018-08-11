package tino.example.tino.interiordecoration;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FillInformationActivity extends AppCompatActivity {

    private final int RC_PHOTO_PICKER = 0;

    private EditText mRoomsPicker;
    private EditText mEditBudget;
    private EditText mPickArea;
    private Button mPictureButton;
    private ImageView mPicture1;
    private ImageView mPicture2;
    private ImageView mPicture3;
    private ImageView mPicture4;
    private ImageView mPicture5;
    private EditText mEditDescription;
    private Button mSubmitButton;

    private String mHire;

    private AppCompatActivity mActivity = this;

    private int mPicturesSelected = 0;
    private List<Uri> pictures = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_information);

        mHire = "";
        mHire = getIntent().getStringExtra("hire");

        mRoomsPicker = findViewById( R.id.rooms_picker );
        mEditBudget = findViewById( R.id.edit_budget );
        mPickArea = findViewById( R.id.pick_area );
        mPictureButton = findViewById( R.id.picture_button );
        mPicture1 = findViewById( R.id.picture1 );
        mPicture2 = findViewById( R.id.picture2 );
        mPicture3 = findViewById( R.id.picture3 );
        mPicture4 = findViewById( R.id.picture4 );
        mPicture5 = findViewById( R.id.picture5 );
        mEditDescription = findViewById( R.id.edit_description );
        mSubmitButton = findViewById( R.id.submit_button );

        mPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPicturesSelected < 5) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/jpeg");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                }
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFilledInformation()){
                    DecorationRequest decorationRequest = new DecorationRequest(pictures, HelperTools.mUserName,
                            mRoomsPicker.getText().toString(),
                            mEditBudget.getText().toString(),
                            mPickArea.getText().toString(),
                            mEditDescription.getText().toString());
                    HelperTools.sendHelpRequest(mActivity, decorationRequest, mHire);
                    Intent intent = new Intent(view.getContext(), BrowseRequestsActivity.class);
                    if(mHire!=null && !mHire.equals("")){
                        intent.putExtra("private", mHire);
                    }
                    startActivity(intent);
                }
            }
        });
    }


    //Returns true if all mandatory fields are filled.
    private boolean checkFilledInformation(){
        if(mRoomsPicker.getText() == null ||
                mEditBudget.getText() == null ||
                mPickArea.getText() == null ||
                mEditDescription.getText() == null ||
                mPicturesSelected == 0){
            Toast.makeText(this, "Please fill all mandatory fields", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            pictures.add(data.getData());
            switch (mPicturesSelected){
                case 0: {
                    addPicture(mPicture1);
                    break;
                } case 1: {
                    addPicture(mPicture2);
                    break;
                } case 2: {
                    addPicture(mPicture3);
                    break;
                } case 3: {
                    addPicture(mPicture4);
                    break;
                } case 4: {
                    addPicture(mPicture5);
                    break;
                }
            }
            mPicturesSelected++;
        }
    }

    private void addPicture(ImageView imageView){
        Glide.with(imageView.getContext())
                .load(pictures.get(mPicturesSelected))
                .into(imageView);
    }
}
