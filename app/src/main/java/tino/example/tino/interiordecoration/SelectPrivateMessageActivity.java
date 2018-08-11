package tino.example.tino.interiordecoration;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectPrivateMessageActivity extends AppCompatActivity {

    private ConstraintLayout mLauriLayout;
    private ConstraintLayout mPaulaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_private_message);

        mLauriLayout = findViewById(R.id.lauri_messages);
        mPaulaLayout = findViewById(R.id.paula_messages);

        mLauriLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BrowseRequestsActivity.class);
                intent.putExtra("private", "lauri_laurela");
                startActivity(intent);
            }
        });

        mPaulaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BrowseRequestsActivity.class);
                intent.putExtra("private", "paula_pulla");
                startActivity(intent);
            }
        });
    }
}
