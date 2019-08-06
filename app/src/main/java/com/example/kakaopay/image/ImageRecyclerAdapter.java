package com.example.kakaopay.image;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.example.kakaopay.R;
import com.example.kakaopay.model.Image_material.Documents;

import java.util.ArrayList;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder> {
    private final ArrayList<Documents> mItemList;

    //클릭 리스너
    private ImageRecyclerViewClickListener mListener;
    public void setOnClickListener(ImageRecyclerViewClickListener listener){
        mListener=listener;
    }
    public interface ImageRecyclerViewClickListener{
        void onItemClicked(int position);
    }

    //생성자
    public ImageRecyclerAdapter(ArrayList<Documents> mItemList) {
        this.mItemList = mItemList;
    }

    @NonNull
    @Override
    //뷰 혿더를 생성하는 부분, 레이아웃을 만든느 부분
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return null;
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card,parent,false);
        return new ViewHolder(view);
    }

    //뷰 홀더에 데이터를 설정하는 부분
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Glide
        Glide.with(holder.mThumbnail_url.getContext())
                .load(mItemList.get(position).getThumbnail_url())
                .skipMemoryCache(true)
                .priority(Priority.HIGH)
                .centerCrop()
                .error(R.drawable.ic_launcher_background)
                .fitCenter()
                .into(holder.mThumbnail_url);

        //html처리
        holder.mDisplay_sitename.setText(Html.fromHtml(mItemList.get(position).getDisplay_sitename()));
    }

    //아이템의 수
    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    //각각의 아이템의 레퍼런스를 저장할 뷰 홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mThumbnail_url;
        TextView mDisplay_sitename;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mThumbnail_url=itemView.findViewById(R.id.thumbnail_url);
            mDisplay_sitename=itemView.findViewById(R.id.display_sitename);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //아이템 클릭
                    if(mListener!=null){
                        int pos=getAdapterPosition();
                        if(pos!= RecyclerView.NO_POSITION){
                            mListener.onItemClicked(pos);
                        }
                    }
                }
            });
        }
    }

    public void addItems(ArrayList<Documents>items){
        for(Documents item : items)mItemList.add(item);
    }
}
