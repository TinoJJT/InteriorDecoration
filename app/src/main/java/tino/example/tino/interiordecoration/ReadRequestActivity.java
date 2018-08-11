package tino.example.tino.interiordecoration;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReadRequestActivity extends AppCompatActivity {

    private ConstraintLayout mRequestContstraint;
    private TextView mRequestNameTextview;
    private TextView mTimeTextview;
    private View mLine1;
    private TextView mRoomsTextview;
    private TextView mRoomsNumber;
    private TextView mBudgetTextview;
    private TextView mBudgetNumber;
    private TextView mAreaTextview;
    private TextView mAreaNumber;
    private ImageView mRequestImageview1;
    private ImageView mRequestImageview2;
    private ImageView mRequestImageview3;
    private ImageView mRequestImageview4;
    private ImageView mRequestImageview5;
    private TextView mRequestContentTextview;
    private ListView mRequestListView;
    private ConstraintLayout mComposeRequestMessageLayout;
    private EditText mWriteRequestMessageEdittext;
    private Button mSendButton;
    private Button mSendImageButton;

    private List<ImageView> mImageViews = new ArrayList<>();

    private DecorationRequest mDecorationRequest;
    private String mRequestKey;
    private String mPrivateRequest;

    private AppCompatActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        mDecorationRequest = (DecorationRequest) getIntent().getSerializableExtra("2");
        mRequestKey = mDecorationRequest.mKey;
        mActivity = this;

        mRequestContstraint = (ConstraintLayout)findViewById( R.id.request_contstraint );
        mRequestNameTextview = (TextView)findViewById( R.id.request_name_textview );
        mTimeTextview = (TextView)findViewById( R.id.time_textview );
        mLine1 = (View)findViewById( R.id.line1 );
        mRoomsTextview = (TextView)findViewById( R.id.rooms_textview );
        mRoomsNumber = (TextView)findViewById( R.id.rooms_number );
        mBudgetTextview = (TextView)findViewById( R.id.budget_textview );
        mBudgetNumber = (TextView)findViewById( R.id.budget_number );
        mAreaTextview = (TextView)findViewById( R.id.area_textview );
        mAreaNumber = (TextView)findViewById( R.id.area_number );
        mImageViews.add((ImageView) findViewById( R.id.request_imageview1 ));
        mImageViews.add((ImageView) findViewById( R.id.request_imageview2 ));
        mImageViews.add((ImageView) findViewById( R.id.request_imageview3 ));
        mImageViews.add((ImageView) findViewById( R.id.request_imageview4 ));
        mImageViews.add((ImageView) findViewById( R.id.request_imageview5 ));

        mRequestContentTextview = (TextView)findViewById( R.id.request_content_textview );
        mRequestListView = (ListView)findViewById( R.id.request_list_view );
        mComposeRequestMessageLayout = (ConstraintLayout)findViewById( R.id.compose_request_message_layout );
        mWriteRequestMessageEdittext = (EditText)findViewById( R.id.write_request_message_edittext );
        mSendButton = (Button)findViewById( R.id.send_button );
        mSendImageButton = (Button)findViewById( R.id.send_image_button );

        HelperTools.setupRequestLayout(mRequestNameTextview, mTimeTextview, mRoomsNumber,
                mBudgetNumber, mAreaNumber, mRequestContentTextview, mImageViews, mDecorationRequest, this);


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = mWriteRequestMessageEdittext.getText().toString();
                if(content.length() > 0){
                    HelperTools.sendMessage(content, mDecorationRequest, null);
                    mWriteRequestMessageEdittext.setText("");
                }
            }
        });

        mSendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperTools.selectAndSendImage(mActivity, mRequestKey);
            }
        });

        HelperTools.getMessages(mRequestKey, this, R.layout.chat_activity, mRequestListView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == HelperTools.RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            HelperTools.finalizePhotoSend(data.getData());
        }
    }
}
