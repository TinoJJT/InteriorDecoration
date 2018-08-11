package tino.example.tino.interiordecoration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static tino.example.tino.interiordecoration.HelperTools.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity {


    private TextView mHireTextview;
    private TextView mLeaveRequestTextview;
    private TextView mMessagesTextview;
    private TextView mBrowseRequestTextview;

    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HelperTools.logInAndGetUserName(this);

        mHireTextview = findViewById(R.id.hire_textview);
        mHireTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SelectHireActivity.class);
                startActivity(intent);
            }
        });

        mLeaveRequestTextview = findViewById(R.id.public_request_textview);
        mLeaveRequestTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FillInformationActivity.class);
                startActivity(intent);
            }
        });

        mMessagesTextview = findViewById(R.id.message_textview);
        mMessagesTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SelectPrivateMessageActivity.class);
                startActivity(intent);
            }
        });

        mBrowseRequestTextview = findViewById(R.id.browse_textview);
        mBrowseRequestTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BrowseRequestsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.e("TAG" , "YOURE SHIT 11");
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                HelperTools.mUserName = user.getDisplayName();
            } else {
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_LONG).show();
                //exits app
                finish();
            }
        }
    }
}
