package tino.example.tino.interiordecoration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class BrowseRequestsActivity extends AppCompatActivity {


    private ListView mListView;

    private String privateMessagesIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        mListView = findViewById(R.id.browse_list_view);

        privateMessagesIntent = getIntent().getStringExtra("private");

        HelperTools.getPublicRequests(this, R.layout.browse_list_item, mListView, privateMessagesIntent);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), ReadRequestActivity.class);
                DecorationRequest decorationRequest = (DecorationRequest) mListView.getAdapter().getItem(i);
                intent.putExtra("2", decorationRequest);
                startActivity(intent);
            }
        });
    }
}
