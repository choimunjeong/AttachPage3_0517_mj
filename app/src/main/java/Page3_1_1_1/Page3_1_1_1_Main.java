package Page3_1_1_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attachpage3_200428_mj.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import DB.Heart_page;
import Page3_1.Page3_1_Main;
import Page3_1_1.Page3_1_1_addConformDialog;
import Page3_1_1.Page3_1_1_dargData;

public  class Page3_1_1_1_Main extends AppCompatActivity implements Page3_1_1_1_addCityBottomSheet.onSetList {
    TextView addSpot;
    String date= null, dayPass = null;
    ArrayList<String> next_data;
    ArrayList<String> next_data_second;
    String split_1 [];
    private ArrayList<Page3_1_1_1_dargData> getitem = new ArrayList<>();
    LayoutInflater inflater;
    Page3_1_1_1_trainAdapter adapter;
    ArrayList<RecycleItem> list = new ArrayList<>();
    String day1_date, day2_date, day3_date, day4_date, day5_date, day6_date, day7_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_1_1_1_main);

        Intent intent = getIntent();
        next_data = (ArrayList<String>) intent.getSerializableExtra("next_data");
        next_data_second = (ArrayList<String>) intent.getSerializableExtra("next_data");
        date = (intent.getExtras().getString("date")).replaceAll("[^0-9]", "");
        dayPass = intent.getExtras().getString("dayPass");

        //날짜를 더할때 실제 날짜 반영해서 더해야함
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date_format = null;
        try {
            date_format = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date_format);

        day1_date = date;
        calendar.add(Calendar.DATE, 1);
        day2_date = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        day3_date = dateFormat.format(calendar.getTime());




        for(int i =0; i < next_data.size(); i++){
            split_1 = next_data.get(i).split(",");
            getitem.add(new Page3_1_1_1_dargData(split_1[0], split_1[1]));
        }


        list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "", "1일차", day1_date));

        //환승인지 아닌지 걸러내는 작업
        for(int i =0; i < next_data.size()-1; i++){
            if(!getitem.get(i).getNumber().contains("환승") ){
                if(!getitem.get(i+1).getNumber().contains("환승")) {
                    list.add(new RecycleItem(Page3_1_1_1_trainAdapter.CHILD, getitem.get(i).getName() + "," + getitem.get(i + 1).getName(), getitem.get(i).getName() + " - " + getitem.get(i + 1).getName(), day1_date ));
                }
                else if(!getitem.get(i+2).getNumber().contains("환승")) {
                    list.add(new RecycleItem(Page3_1_1_1_trainAdapter.CHILD, getitem.get(i).getName()+","+getitem.get(i+1).getName()+","+getitem.get(i+2).getName(), getitem.get(i).getName()+" - "+getitem.get(i+2).getName() ,  day1_date ));
                } else if(!getitem.get(i+3).getNumber().contains("환승")) {
                    list.add(new RecycleItem(Page3_1_1_1_trainAdapter.CHILD,getitem.get(i).getName()+","+getitem.get(i+1).getName()+","+getitem.get(i+2).getName()+","+getitem.get(i+3).getName() ,getitem.get(i).getName()+" - "+getitem.get(i+3).getName() ,  day1_date ));
                } else {
                    list.add(new RecycleItem(Page3_1_1_1_trainAdapter.CHILD,getitem.get(i).getName()+","+getitem.get(i+1).getName()+","+getitem.get(i+2).getName()+","+getitem.get(i+3).getName()+","+getitem.get(i+3).getName() ,getitem.get(i).getName()+" - "+getitem.get(i+4).getName() ,  day1_date));
                }
            }
        }
        list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "","2일차",  day2_date));
        list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "","3일차",  day3_date));

        //5일차면
        if(dayPass.contains("5")){
            //날짜 더해줌
            calendar.add(Calendar.DATE, 1);
            day4_date = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
            day5_date = dateFormat.format(calendar.getTime());

            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "", "4일차", day4_date));
            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "", "5일차", day5_date));
        }

        //7일차면
        else if(dayPass.contains("7")){
            //날짜 더해줌
            calendar.add(Calendar.DATE, 1);
            day4_date = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
            day5_date = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
            day6_date = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
            day7_date = dateFormat.format(calendar.getTime());

            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "", "4일차", day4_date));
            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "", "5일차", day5_date ));
            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "", "6일차", day6_date ));
            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "", "7일차", day7_date ));
        }

        // 레이아웃 안에 레이아웃 만들기
        LinearLayout contentsLayout = (LinearLayout) findViewById(R.id.page3_1_1_box_round);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.page3_1_1_1_recyclerview, contentsLayout, true);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.scheduleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 Adapter 객체 지정.
        adapter = new Page3_1_1_1_trainAdapter(list, getSupportFragmentManager());
        recyclerView.setAdapter(adapter);

        // 드래그 이벤트
        ItemTouchHelper.Callback callback = new TrainItemTouchHelper(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        adapter.setTouchHelper(touchHelper);

        // 관광지 추가하기 버튼 눌렀을 때
        addSpot = (TextView) findViewById(R.id.page3_1_1_1_1_addbtn);
        addSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Page3_1_1_1_addCityBottomSheet addCityBottomSheet = Page3_1_1_1_addCityBottomSheet.getInstance();
                addCityBottomSheet.show(getSupportFragmentManager(), "AddCityBottomSheet");
            }
        });

        Button reset_btn = (Button)findViewById(R.id.page3_1_1_1_1_reset_btn);
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onsetlist(String text, String cityname) {
        boolean isAdd = false;

        //도시 아래에 넣기 위함
        for(int i =0; i < list.size(); i++){
            if(list.get(i).type == Page3_1_1_1_trainAdapter.CHILD){
                String text_split[] = list.get(i).text.split("-");

                //불필요한 공백 제거
                String station = text_split[1].trim();

                if(station.contains(cityname)){
                    list.add(i+1, new RecycleItem(Page3_1_1_1_trainAdapter.CITY, "",text,  "0"));
                    adapter.notifyDataSetChanged();
                    isAdd = true;
                    break;
                }
            }
        }

        if(!isAdd)
            Toast.makeText(getApplicationContext(), "해당되는 정차역이 없습니다.", Toast.LENGTH_LONG).show();
    }



    public class RecycleItem {
        int type;
        String text;
        String text_shadow;
        String date;

        public RecycleItem(int type, String text_shadow, String text, String  date){
            this.type = type;
            this.text_shadow = text_shadow;
            this.text = text;
            this.date = date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.backbutton, R.anim.backbutton);
    }

    @Override
    protected void onPause() {
        super.onPause();
        list.clear();
        getitem.clear();
    }



}
