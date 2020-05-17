package Page3_1_1_1;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attachpage3_200428_mj.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import Page3_1_1.Page3_1_1_addBottomSheet;


public class Page3_1_1_1_trainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements TrainItemTouchHelper.ItemTouchHelperAdapter{
    private ItemTouchHelper touchHelper;
    private ArrayList<Page3_1_1_1_Main.RecycleItem> items = null;
FragmentManager fragmentManager;
    boolean firstdone = false;

    //헤더인지 아이템인지 확인하는데 필요함
    public static final int HEADER = 0;
    public static final int CHILD = 1;
    public static final int CITY =2;



    Page3_1_1_1_trainAdapter(ArrayList<Page3_1_1_1_Main.RecycleItem> list, FragmentManager supportFragmentManager) {
        items = list;
        this.fragmentManager = supportFragmentManager;
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

            case CITY:
                LayoutInflater inflater3 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view3 = inflater3.inflate(R.layout.page3_1_1_1_cityitem, parent, false);
                Page3_1_1_1_trainAdapter.CityViewHolder cityViewHolder = new Page3_1_1_1_trainAdapter.CityViewHolder(view3);
                return cityViewHolder;
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
                        Log.i("움직였니", "ㅇㅇ");


                    }
                    return false;
                }
            });
        }


        else if (itemViewType == CHILD) {

            //이용권바 움직이면 날짜 변동
            if(position > 0){
                for(int i =position; i > 0; i--){
                    if(items.get(i-1).text.contains("일차") ){
                        item.setDate(items.get(i-1).date);
                        break;
                    }
                }
            }
            ;
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mCourseText.setText(item.text);
            itemViewHolder.mShadowText.setText(item.text_shadow);
            Log.i("움직이나", String.valueOf(itemViewHolder.itemView.getId()));
        }

        //itemViewType == CITY
        else {
            CityViewHolder cityViewHolder = (CityViewHolder) holder;
            cityViewHolder.city_text.setText(item.text);
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
        if(toPos != 0 ){
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




    //헤더 xml 연결
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

    //촤일드 xml 연결
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mTimeText, mCourseText, mShadowText; // 시간, 경로
        public ImageView search_img;
        LinearLayout item_touch;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mCourseText = (TextView) itemView.findViewById(R.id.course_Page3_1_1);
            mShadowText = (TextView) itemView.findViewById(R.id.searchTime_Page3_1_1_shadow);
            mTimeText = (TextView) itemView.findViewById(R.id.searchTime_Page3_1_1);
            search_img = (ImageView) itemView.findViewById(R.id.search_img);
            item_touch = (LinearLayout) itemView.findViewById(R.id.item_touch);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context context = v.getContext();   //현재 활성환된 activity에 접근하기 위함
                    final int pos = getAdapterPosition() ;          //현재 위치를 받아오기 위함
                    Log.i("현재위치기 어딘데", String.valueOf(pos));


                    ArrayList<String> data = new ArrayList<>();
                    data.add(items.get(pos).text_shadow);
//                    final Page3_1_1_1_bottomSheet_ add = Page3_1_1_1_bottomSheet_.getInstance();
//                    add.show(fragmentManager, "add");
//                    add.send_position(pos);

                    final BottomSheetDialog dialog = new BottomSheetDialog(context);
                    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.page3_1_1_1_apisheet, null);
                    dialog.setContentView(view);

                    ListView listView = view.findViewById(R.id.api_list);
                    final ArrayList<Page3_1_1_1_bottomSheetAdapter.Api_Item> completeList = new ArrayList<Page3_1_1_1_bottomSheetAdapter.Api_Item>();

                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "11", "aa", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "22"+"\n"+"dd", "aa"+"\n"+"dd", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "33", "aa", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "44", "aa", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "55", "aa", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "66", "aa", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "77", "aa", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "88", "aa", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "99", "aa", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "00", "aa", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "11", "aa", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "22", "aa", "bb","cc"));
                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "33", "aa", "bb","cc"));

                    Page3_1_1_1_bottomSheet_Adapter adapter = new Page3_1_1_1_bottomSheet_Adapter(completeList, context);
                    listView.setAdapter(adapter);

                    listView.setOnTouchListener(new ListView.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            int action = event.getAction();
                            switch (action) {
                                case MotionEvent.ACTION_DOWN:
                                    // Disallow NestedScrollView to intercept touch events.
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                    break;

                                case MotionEvent.ACTION_UP:
                                    // Allow NestedScrollView to intercept touch events.
                                    v.getParent().requestDisallowInterceptTouchEvent(false);
                                    break;
                            }

                            // Handle ListView touch events.
                            v.onTouchEvent(event);
                            return true;
                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String[] startTIme = completeList.get(position).getDepTime().split("\n");
                            String[] endTime = completeList.get(position).getArrTime().split("\n");
                            mTimeText.setText(startTIme[0]+"-"+endTime[endTime.length-1]);
                            mTimeText.setTextColor(Color.parseColor("#000000"));
                            mTimeText.setTextSize(16f);
                            search_img.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
        }
    }

    //시티 xml 연결
    public class CityViewHolder extends RecyclerView.ViewHolder {
        public TextView city_text;

        public CityViewHolder(View itemView) {
            super(itemView);
            city_text = (TextView) itemView.findViewById(R.id.page3_1_1_1_cityText);
        }
    }

}
