package com.parse.starter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class FeedActivity extends AppCompatActivity {

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);

        String feedusername = getIntent().getStringExtra(String.valueOf(R.string.intentUsername));

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Images");

        query.whereEqualTo("username", feedusername);
        query.orderByDescending("createdAt");

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Blue)));
        ab.setTitle(feedusername + getString(R.string.apostropheSFeed));

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e==null){

                    if(objects.size()>0){

                        for(ParseObject object : objects){

                            ParseFile file = (ParseFile) object.get("image");

                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if(e==null && data!=null){

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0 , data.length);

                                        ImageView imageView = new ImageView(getApplicationContext());
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT));
                                        imageView.setPadding(0,10,0,0);

                                        imageView.setImageBitmap(bitmap);

                                        linearLayout.addView(imageView);

                                    }
                                }
                            });

                        }
                    }
                }
            }
        });
    }
}
