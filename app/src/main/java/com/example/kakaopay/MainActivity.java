package com.example.kakaopay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kakaopay.image.ImageRecyclerAdapter;
import com.example.kakaopay.model.ImageInfo;
import com.example.kakaopay.model.Image_material.Documents;
import com.example.kakaopay.model.Image_material.Meta;
import com.example.kakaopay.util.ApiUtils;
import com.example.kakaopay.util.ImageApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener,
        ImageRecyclerAdapter.ImageRecyclerViewClickListener{

    private EditText mImageNameEditText;
    //search
    private ImageApiInterface mImageApiInterface;
    private boolean flag;
    private String mImage_name;
    //recyclerView
    private RecyclerView mRecyclerView;
    private ImageRecyclerAdapter mAdapter;
    private ArrayList<Documents>mImageData=new ArrayList<>();
    private ArrayList<Meta>mMetaData=new ArrayList<>();

    private LinearLayoutManager manager;

    //리사이클러 클릭
    public static final String EXTRA_collection="collection";
    public static final String EXTRA_datetime="datetime";
    public static final String EXTRA_display_sitename="display_sitename";
    public static final String EXTRA_doc_url="doc_url";
    public static final String EXTRA_height="height";
    public static final String EXTRA_image_url="image_url";
    public static final String EXTRA_thumbnail_url="thumbnail_url";
    public static final String EXTRA_width="width";

    //무한 스크롤
    private boolean itShouldLoadMore = true;

    private static int page_count,total_size;
    private boolean is_end=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageApiInterface= ApiUtils.getImageApi();
        //리사이클러뷰
        mRecyclerView=findViewById(R.id.recycler_image);
        mRecyclerView.setHasFixedSize(false);
        manager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        //endless recyclerView
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!mRecyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        if (itShouldLoadMore) {
                            if (is_end==false) {
                                fetchData();
                            } else {
                                Toast.makeText(MainActivity.this, "더 이상 자료가 없습니다...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });
        //버튼 클릭 이벤트
        mImageNameEditText=findViewById(R.id.image_string_edit);
        findViewById(R.id.image_search_button).setOnClickListener(this);
    }
    private void fetchData() {
        page_count++;
        itShouldLoadMore = false;
        Call<ImageInfo> call = mImageApiInterface.getImageItemsMores(mImage_name, page_count);
        call.enqueue(new Callback<ImageInfo>() {
            @Override
            public void onResponse(Call<ImageInfo> call, Response<ImageInfo> response) {

                itShouldLoadMore = true;
                ImageInfo imageInfo=response.body();
                ArrayList<Documents> addInfo = imageInfo.getDocuments();
                is_end=imageInfo.getMeta().isIs_end();
                mAdapter.addItems(addInfo);
                mAdapter.notifyItemRangeInserted(total_size,addInfo.size());
                //total_size+=addInfo.size();
                //System.out.println("총 개수 "+total_size+"    추가 개수 : "+addInfo.size()+"   추가 여부 "+is_end);
            }

            @Override
            public void onFailure(Call<ImageInfo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "통신 오류입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void firstSearch(){
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mImageNameEditText.getWindowToken(),0);

        page_count = 1;
        flag=true;
        mImage_name = mImageNameEditText.getText().toString();
        if (mImage_name.equals("")) flag=false;

        if(flag==false){
            Handler mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 사용하고자 하는 코드
                    Toast.makeText(MainActivity.this, "검색어를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
            }, 0);
        }else{
            itShouldLoadMore = false;
            Call<ImageInfo> call = mImageApiInterface.getImageItemsLoad(mImage_name);
            call.enqueue(new Callback<ImageInfo>() {
                @Override
                public void onResponse(Call<ImageInfo> call, Response<ImageInfo> response) {

                    itShouldLoadMore = true;
                    ImageInfo imageInfo=response.body();
                    mImageData = imageInfo.getDocuments();
                    if (mImageData.isEmpty()) {
                        Toast.makeText(MainActivity.this, "'" + mImage_name + "'" + " 검색결과는 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //계속 불러올 것인지
                    is_end=imageInfo.getMeta().isIs_end();
//                    int mpageable_count=imageInfo.getMeta().getPageable_count();
//                    int total_count=imageInfo.getMeta().getTotal_count();

                    total_size=mImageData.size();
                    //System.out.println("처음 개수 "+total_size+"   추가 여부 "+is_end+"\n"+mpageable_count+"\n"+total_count+"\n");

                    mAdapter = new ImageRecyclerAdapter(mImageData);
                    mRecyclerView.setAdapter(mAdapter);
                    //각 리사이클러뷰 클릭
                    mAdapter.setOnClickListener(MainActivity.this);
                }
                @Override
                public void onFailure(Call<ImageInfo> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "통신 오류입니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("");
        progressDialog.setMessage("잠시만요..");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    firstSearch();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
        progressDialog.show();
    }

    @Override
    public void onItemClicked(int position) {
        Intent detailIntent=new Intent(this,DetailActivity.class);
        Documents clickItem=mImageData.get(position);

        detailIntent.putExtra(EXTRA_collection,clickItem.getCollection());
        detailIntent.putExtra(EXTRA_datetime,clickItem.getDatetime());
        detailIntent.putExtra(EXTRA_display_sitename,clickItem.getDisplay_sitename());
        detailIntent.putExtra(EXTRA_doc_url,clickItem.getDoc_url());
        detailIntent.putExtra(EXTRA_height,clickItem.getHeight());
        detailIntent.putExtra(EXTRA_image_url,clickItem.getImage_url());
        detailIntent.putExtra(EXTRA_thumbnail_url,clickItem.getThumbnail_url());
        detailIntent.putExtra(EXTRA_width,clickItem.getWidth());

        startActivity(detailIntent);
    }
}