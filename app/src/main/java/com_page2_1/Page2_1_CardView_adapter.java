package com_page2_1;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.attachpage3_200428_mj.R;

import java.util.List;

public class Page2_1_CardView_adapter extends RecyclerView.Adapter<Page2_1_CardView_adapter.ViewHolder> {

    private Page2_1_MainActivity mainActivity;
    private String[] stay = new String[5];  // 하트의 클릭 여부
    private List<Page2_1_Fragment.Recycler_item> items;  //리사이클러뷰 안에 들어갈 값 저장
    OnItemClick mCallback;
    String cityName;

    //메인에서 불러올 때, 이 함수를 씀
    public Page2_1_CardView_adapter(List<Page2_1_Fragment.Recycler_item> items, Page2_1_MainActivity mainActivity, String cityName,  OnItemClick mCallback) {
        this.items=items;
        this.mainActivity = mainActivity;
        this.cityName = cityName;
        this.mCallback = mCallback;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_page2,null);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Page2_1_Fragment.Recycler_item item=items.get(position);

        if(item.getImage() == null){
            Log.i("이미지가 없을때", "널이다 이녀석아,,");
        } else {
            //이미지뷰에 url 이미지 넣기.
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
            Glide.with(mainActivity).load(item.getImage())
                    .apply(requestOptions).into(holder.image);
        }

        holder.title.setText(item.getTitle());
        holder.type.setText(item.getType());


        //하트누르면 내부 데이터에 저장
        holder.heart.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(stay[position]==null){
                    holder.heart.setBackgroundResource(R.drawable.ic_heart_off);
                    mCallback.make_db(item.getContentviewID(), item.getTitle(), cityName);   //countId랑 title을 db에 넣으려고 함( make_db라는 인터페이스 이용)
                    mCallback.make_dialog();                                       //db에 잘 넣으면 띄우는 다이얼로그(위와 마찬가지로 인터페이스 이용
                    stay[position] = "ON";
                   // Toast.makeText(context,"관심관광지를 눌렀습니다",Toast.LENGTH_SHORT).show();

                } else{
                    holder.heart.setBackgroundResource(R.drawable.ic_icon_addmy);
                    mCallback.delete_db(item.getContentviewID());
                    stay[position] = null;
                   // Toast.makeText(context,"관심관광지를 취소했습니다",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //여기에 리스트를 클릭하면, 관광지 상세페이지로 넘어가는거 구현
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,item.getContentviewID(),Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, Page3_1_X_X.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return this.items.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, type;
        CardView cardview;
        Button heart;

        public ViewHolder(View itemView) {
            super(itemView);
            heart = (Button)itemView.findViewById(R.id.cardview_heart);
            image=(ImageView)itemView.findViewById(R.id.page2_1_no_image);
            title=(TextView)itemView.findViewById(R.id.page2_1_title);
            type=(TextView)itemView.findViewById(R.id.cardview_type);
            cardview=(CardView)itemView.findViewById(R.id.page2_1_cardview);
        }
    }



}