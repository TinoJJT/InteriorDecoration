package tino.example.tino.interiordecoration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SelectHireActivity extends AppCompatActivity{

    private TextView mFirstHireTextview;
    private TextView mSecondHireTextview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_hire);

        mFirstHireTextview = findViewById(R.id.first_description);
        mSecondHireTextview = findViewById(R.id.second_description);

        mFirstHireTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FillInformationActivity.class);
                intent.putExtra("hire", "lauri_laurela");
                startActivity(intent);
            }
        });
        mSecondHireTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FillInformationActivity.class);
                intent.putExtra("hire", "paula_pulla");
                startActivity(intent);
            }
        });
    }
}
