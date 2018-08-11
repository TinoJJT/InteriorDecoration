package tino.example.tino.interiordecoration;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class EnlargedImageActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Button mButton;

    private DecorationRequest mDecorationRequest;
    private int imageNumber;

    private String chatPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarged_image);

        mImageView = findViewById(R.id.large_imageview);
        mButton = findViewById(R.id.save_button);
        mDecorationRequest = (DecorationRequest) getIntent().getSerializableExtra("3");
        imageNumber = getIntent().getIntExtra("4", 0);
        chatPhotoUrl = getIntent().getStringExtra("5");

        if(mDecorationRequest!=null) {
            Glide.with(this)
                    .load(mDecorationRequest.inUriFormat().get(imageNumber))
                    .into(mImageView);
        } else if (chatPhotoUrl!=null){
            Glide.with(this)
                    .load(Uri.parse(chatPhotoUrl))
                    .into(mImageView);
        }

        final Bitmap icon = mImageView.getDrawingCache();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Intent share = new Intent(Intent.ACTION_SEND);
                Uri imageUri = mDecorationRequest.inUriFormat().get(imageNumber);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");

                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(intent , "Share"));
            }
        });
    }
}
