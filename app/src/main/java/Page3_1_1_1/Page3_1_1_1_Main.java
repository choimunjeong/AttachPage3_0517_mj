package Page3_1_1_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attachpage3_200428_mj.R;

import java.util.ArrayList;

public  class Page3_1_1_1_Main extends AppCompatActivity {
    TextView addSpot;
    String date, dayPass;
    ArrayList<String> next_data;
    ArrayList<String> next_data_second;
    String split_1 [];
    private ArrayList<Page3_1_1_1_dargData> getitem = new ArrayList<>();
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_1_1_1_main);

        Intent intent = getIntent();
        next_data = (ArrayList<String>) intent.getSerializableExtra("next_data");
        next_data_second = (ArrayList<String>) intent.getSerializableExtra("next_data");
        date = (intent.getExtras().getString("date")).replaceAll("[^0-9]", "");
        dayPass = intent.getExtras().getString("dayPass");

        Log.i("데이트", date);

        for(int i =0; i < next_data.size(); i++){
            split_1 = next_data.get(i).split(",");
            getitem.add(new Page3_1_1_1_dargData(split_1[0], split_1[1]));
        }

        ArrayList<RecycleItem> list = new ArrayList<>();
        list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "", "1일차"));

        //환승인지 아닌지 걸러내는 작업
        for(int i =0; i < next_data.size()-1; i++){
            if(!getitem.get(i).getNumber().contains("환승") ){
                if(!getitem.get(i+1).getNumber().contains("환승")) {
                    list.add(new RecycleItem(Page3_1_1_1_trainAdapter.CHILD, getitem.get(i).getName() + "," + getitem.get(i + 1).getName(), getitem.get(i).getName() + "-" + getitem.get(i + 1).getName()));
                }
                else if(!getitem.get(i+2).getNumber().contains("환승")) {
                    list.add(new RecycleItem(Page3_1_1_1_trainAdapter.CHILD, getitem.get(i).getName()+","+getitem.get(i+1).getName()+","+getitem.get(i+2).getName(), getitem.get(i).getName()+"-"+getitem.get(i+2).getName()));
                } else if(!getitem.get(i+3).getNumber().contains("환승")) {
                    list.add(new RecycleItem(Page3_1_1_1_trainAdapter.CHILD,getitem.get(i).getName()+","+getitem.get(i+1).getName()+","+getitem.get(i+2).getName()+","+getitem.get(i+3).getName() ,getitem.get(i).getName()+"-"+getitem.get(i+3).getName()));
                } else {
                    list.add(new RecycleItem(Page3_1_1_1_trainAdapter.CHILD,getitem.get(i).getName()+","+getitem.get(i+1).getName()+","+getitem.get(i+2).getName()+","+getitem.get(i+3).getName()+","+getitem.get(i+3).getName() ,getitem.get(i).getName()+"-"+getitem.get(i+4).getName()));
                }
            }
        }
        list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "","2일차"));
        list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "","3일차"));

        //5일차면
        if(dayPass.contains("5")){
            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "","4일차"));
            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "","5일차"));
        }

        //7일차면
        else if(dayPass.contains("7")){
            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "","4일차"));
            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "","5일차"));
            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "","6일차"));
            list.add(new RecycleItem(Page3_1_1_1_trainAdapter.HEADER, "","7일차"));
        }

        // 레이아웃 안에 레이아웃 만들기
        LinearLayout contentsLayout = (LinearLayout) findViewById(R.id.page3_1_1_box_round);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.page3_1_1_1_recyclerview, contentsLayout, true);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.scheduleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 Adapter 객체 지정.
        final Page3_1_1_1_trainAdapter adapter = new Page3_1_1_1_trainAdapter(list, date);
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
//                Toast.makeText(getApplicationContext(), "버튼 눌림", Toast.LENGTH_SHORT).show();
//                AddSpotBottomSheetDialog addSpotBottomSheetDialog = AddSpotBottomSheetDialog.getInstance();
//                addSpotBottomSheetDialog.show(getSupportFragmentManager(), "BottomSheet");
            }
        });
    }



    public class RecycleItem {
        String time;
        String course;
        String days;
        int type;
        String text;
        String text_shadow;

        public RecycleItem(int type, String text_shadow, String text){
            this.type = type;
            this.text_shadow = text_shadow;
            this.text = text;
        }

        String getTime() {
            return this.time;
        }

        String getCourse() {
            return this.course;
        }

        String getDays(){
            return this.days;
        }
    }

}
