package Page3_1_1_1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attachpage3_200428_mj.R;

import java.util.ArrayList;

public class Page3_1_1_1_bottomSheetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Api_Item> mData = null;
    onSetList onSetList;

    //헤더인지 아이템인지 확인하는데 필요함
    public static final int HEADER = 0;
    public static final int CHILD = 1;


    Page3_1_1_1_bottomSheetAdapter(ArrayList<Api_Item> list, onSetList onSetList) {
        this.onSetList = onSetList;
        this.mData = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        switch (viewType) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.page3_1_1_1_apisheet_header, parent, false);
                Page3_1_1_1_bottomSheetAdapter.HeaderViewHolder headerViewHolder = new Page3_1_1_1_bottomSheetAdapter.HeaderViewHolder(view);
                return headerViewHolder;

            case CHILD:
                LayoutInflater inflater2 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view2 = inflater2.inflate(R.layout.page3_1_1_1_apisheet_child,parent,false);
                Page3_1_1_1_bottomSheetAdapter.ItemViewHolder itemViewHolder = new Page3_1_1_1_bottomSheetAdapter.ItemViewHolder(view2);
                return itemViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Api_Item item = mData.get(position);
        int itemViewType = getItemViewType(position);

        if (itemViewType == HEADER) {
            final Page3_1_1_1_bottomSheetAdapter.HeaderViewHolder headerViewHolder = (Page3_1_1_1_bottomSheetAdapter.HeaderViewHolder) holder;
            headerViewHolder.depTime.setText(item.depTime);
            headerViewHolder.arrTime.setText(item.arrTime);
            headerViewHolder.startCityNAme.setText(item.spendTime);
            headerViewHolder.trainNumber.setText(item.trainNumber);
            headerViewHolder.depTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSetList.onsetlist(item.getDepTime());
                }
            });

        } else if (itemViewType == CHILD) {
            final Page3_1_1_1_bottomSheetAdapter.ItemViewHolder itemViewHolder = (Page3_1_1_1_bottomSheetAdapter.ItemViewHolder) holder;
            itemViewHolder.depTime_child.setText(item.depTime);
            itemViewHolder.arrTime_child.setText(item.arrTime);
            itemViewHolder.spendTime_child.setText(item.spendTime);
            itemViewHolder.trainNumber_child.setText(item.trainNumber);
        }




    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public int getItemViewType(int position) {
        return mData.get(position).type;
    }



    // 헤더의 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class  HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView depTime ;
        TextView arrTime ;
        TextView startCityNAme ;
        TextView trainNumber ;

        HeaderViewHolder(View itemView) {
            super(itemView) ;
            depTime = itemView.findViewById(R.id.api_dep) ;
            arrTime = itemView.findViewById(R.id.api_arr) ;
            startCityNAme = itemView.findViewById(R.id.api_startCity) ;
            trainNumber = itemView.findViewById(R.id.api_trainNumber) ;
        }
    }


    // 헤더의 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class  ItemViewHolder extends RecyclerView.ViewHolder {
        TextView depTime_child ;
        TextView arrTime_child ;
        TextView spendTime_child ;
        TextView trainNumber_child ;

        ItemViewHolder(View itemView) {
            super(itemView) ;
            depTime_child = itemView.findViewById(R.id.api_dep_child) ;
            arrTime_child = itemView.findViewById(R.id.api_arr_child) ;
            spendTime_child = itemView.findViewById(R.id.api_spendtime_child) ;
            trainNumber_child = itemView.findViewById(R.id.api_trainNumber_child) ;
        }
    }


    //아이템 변수 선언
    public static class Api_Item implements Comparable<Api_Item>{
        int type;
        String depTime;
        String arrTime;
        String spendTime;
        String trainNumber;

        public Api_Item(int type, String dep, String arr, String spend, String train){
            this.type = type;
            this.depTime = dep;
            this.arrTime = arr;
            this.spendTime = spend;
            this.trainNumber= train;
        }

        String getDepTime() {
            return this.depTime;
        }
        String getArrTime() {
            return this.arrTime;
        }
        String getSpendTime() {
            return this.spendTime;
        }
        String getTrainNumber() {
            return this.trainNumber;
        }

        @Override
        public int compareTo(Api_Item o) {
            return this.depTime.compareTo(o.depTime);
        }
    }


    //인터페이스 구현-바텀시트 아이템 선택하면 리사이클러뷰에 넣어주기 위함
    public interface onSetList {
        void onsetlist(String text);
    }
}
