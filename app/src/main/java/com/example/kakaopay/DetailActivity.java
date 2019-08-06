package com.example.kakaopay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.kakaopay.MainActivity.EXTRA_collection;
import static com.example.kakaopay.MainActivity.EXTRA_datetime;
import static com.example.kakaopay.MainActivity.EXTRA_display_sitename;
import static com.example.kakaopay.MainActivity.EXTRA_doc_url;
import static com.example.kakaopay.MainActivity.EXTRA_height;
import static com.example.kakaopay.MainActivity.EXTRA_image_url;
import static com.example.kakaopay.MainActivity.EXTRA_thumbnail_url;
import static com.example.kakaopay.MainActivity.EXTRA_width;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent=getIntent();
        String collection=intent.getStringExtra(EXTRA_collection);
        String datetime=intent.getStringExtra(EXTRA_datetime);
        String display_sitename=intent.getStringExtra(EXTRA_display_sitename);
        String doc_url=intent.getStringExtra(EXTRA_doc_url);
        int height=intent.getIntExtra(EXTRA_height,0);
        String image_url=intent.getStringExtra(EXTRA_image_url);
        String thumbnail_url=intent.getStringExtra(EXTRA_thumbnail_url);
        int width=intent.getIntExtra(EXTRA_width,0);

        ImageView iv_Imageurl=findViewById(R.id.image_url_detail);
        TextView tv_collection=findViewById(R.id.collection_detail);
        TextView tv_width=findViewById(R.id.width_detail);
        TextView tv_height=findViewById(R.id.height_detail);
        TextView tv_display_sitename=findViewById(R.id.display_sitename_detail);
        TextView tv_datetime=findViewById(R.id.datetime_detail);
        TextView tv_doc_url=findViewById(R.id.doc_url_detail);

        //glide
        RequestOptions options=new RequestOptions();
        Glide.with(getApplicationContext())
                .load(image_url)
                .disallowHardwareConfig()
                //.apply(new RequestOptions().centerCrop())
                .apply(RequestOptions.placeholderOf(R.drawable.kakaotalk_icon))
                .apply(RequestOptions.fitCenterTransform())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.errorOf(R.drawable.kakao_account_logo))
                .apply(RequestOptions.priorityOf(Priority.HIGH))
                .into(iv_Imageurl);


        tv_collection.setText(collection);
        tv_datetime.setText(datetime);
        tv_display_sitename.setText(display_sitename);
        tv_width.setText(""+width);
        tv_height.setText(""+height);
        tv_doc_url.setText(doc_url);
        Linkify.TransformFilter mTransform=new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher matcher, String s) {
                return "";
            }
        };

        Pattern pattern=Pattern.compile(doc_url);

        Linkify.addLinks(tv_doc_url,pattern,doc_url,null,mTransform);
    }
}
