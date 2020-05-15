package Page3_1_1_1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attachpage3_200428_mj.R;

import java.util.ArrayList;

import Page3_1_1.Page3_1_1_addBottomSheet;


public class Page3_1_1_1_trainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements TrainItemTouchHelper.ItemTouchHelperAdapter{
    private ItemTouchHelper touchHelper;
    private ArrayList<Page3_1_1_1_Main.RecycleItem> items = null;

    boolean firstdone = false;

    //헤더인지 아이템인지 확인하는데 필요함
    public static final int HEADER = 0;
    public static final int CHILD = 1;


    Page3_1_1_1_trainAdapter(ArrayList<Page3_1_1_1_Main.RecycleItem> list) {
        items = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        switch (viewType) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.page3_1_1_1_header, parent, false);
                Page3_1_1_1_trainAdapter.HeaderViewHolder headerViewHolder = new Page3_1_1_1_trainAdapter.HeaderViewHolder(view);
                return headerViewHolder;

            case CHILD:
                LayoutInflater inflater2 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view2 = inflater2.inflate(R.layout.page3_1_1_1_item,parent,false);
                Page3_1_1_1_trainAdapter.ItemViewHolder itemViewHolder = new Page3_1_1_1_trainAdapter.ItemViewHolder(view2);
                return itemViewHolder;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Page3_1_1_1_Main.RecycleItem item = items.get(position);

        int itemViewType = getItemViewType(position);
        if (itemViewType == HEADER) {
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.header_title.setText(item.text);

            //1일차는 움직이는 버튼 없앰
            if(position==0 && !firstdone){
                ((HeaderViewHolder) holder).move_img.setVisibility(View.INVISIBLE);
                firstdone = true;
            }

            headerViewHolder.move_btn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        touchHelper.startDrag(holder);
                    }
                    return false;
                }
            });
        }

        //itemViewType == CHILD
        else {

            //이용권바 움직이면 날짜 변동
            if(position > 0){
                for(int i =position; i > 0; i--){
                    if(items.get(i-1).text.contains("일차") ){
                        item.setDate(items.get(i-1).date);
                        break;
                    }
                }
            }

            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mTimeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();

                    ArrayList<String> data = new ArrayList<>();
                    data.add(item.text_shadow);
                    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.page3_1_1_1_apisheet, null);
                    Page3_1_1_1_bottomSheet dialog = new Page3_1_1_1_bottomSheet(context);
                    dialog.setContentView(view);
                    dialog.show();
                    dialog.send(data, item.date);

                }
            });
            itemViewHolder.mCourseText.setText(item.text);
            itemViewHolder.mShadowText.setText(item.text_shadow);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public int getItemViewType(int position) {
        return items.get(position).type;
    }


    @Override
    public void onItemMove(int fromPos, int toPos) {
        //0번째=1일차로는 움직일 수 없음
        if(toPos != 0){
            Page3_1_1_1_Main.RecycleItem target = items.get(fromPos);
            items.remove(fromPos);
            items.add(toPos, target);
            notifyItemMoved(fromPos, toPos);
            notifyItemChanged(fromPos);
            notifyItemChanged(toPos);
        }
    }


    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mTimeText, mCourseText, mShadowText; // 시간, 경로

        public ItemViewHolder(View itemView) {
            super(itemView);
            mCourseText = (TextView) itemView.findViewById(R.id.course_Page3_1_1);
            mShadowText = (TextView) itemView.findViewById(R.id.searchTime_Page3_1_1_shadow);
            mTimeText = (TextView) itemView.findViewById(R.id.searchTime_Page3_1_1);
        }
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView header_title;
        public TextView move_btn;
        public LinearLayout list_header;
        public ImageView move_img;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            move_btn = (TextView) itemView.findViewById(R.id.move_btn);
            list_header = (LinearLayout) itemView.findViewById(R.id.list_header);
            move_img = (ImageView)itemView.findViewById(R.id.move_btn_img);
        }
    }

}
