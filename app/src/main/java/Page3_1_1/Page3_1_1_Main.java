package Page3_1_1;

import android.content.Intent;
import android.database.DataSetObservable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attachpage3_200428_mj.R;
import com.example.attachpage3_200428_mj.send_data;

import java.util.ArrayList;
import java.util.List;

import Page3_1.Page3_1_Main;

public class Page3_1_1_Main extends AppCompatActivity implements Page3_1_1_addBottomSheet.onSetList {
    Page3_1_1_Main page3_1_1_main;
    RecyclerView recyclerView;

    private ArrayList<Page3_1_1_dargData> list = new ArrayList<>();
    Page3_1_1_adapter adapter = new Page3_1_1_adapter(list);
    String result;
    List<Page3_1_Main.item_data> next_data;
    int number = 0;

    boolean checkStart = false;     //'출발'을 한 번만 넣기 위함
    List<item_data> add_list = new ArrayList<>();
    ArrayList<String> result_name = new ArrayList<String>();
    ArrayList<String> result_number = new ArrayList<String>();


    Button add_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_1_1_main);

        //앞에서 보내는 값을 받음
        Intent intent = getIntent();
        result =  intent.getStringExtra("result");


        //리사이클러뷰
        recyclerView = (RecyclerView)findViewById(R.id.page3_1_1_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(page3_1_1_main));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //드래그 기능
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        adapter.setTouchHelper(touchHelper);

        //리사이클러뷰-드래그 연결
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

        ongetdata(result);


        for(int i =0; i < add_list.size(); i++){
            if(!add_list.get(i).getNumber().contains("환승")){
                list.add(new Page3_1_1_dargData(add_list.get(i).getName() , number));
                number ++;
            }
        }


        //도시 추가하기 버튼
        add_city = (Button) findViewById(R.id.page3_1_1_listAdd);
        add_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Page3_1_1_addBottomSheet add = Page3_1_1_addBottomSheet.getInstance();
                add.show(getSupportFragmentManager(), "add");
            }
        });


        //

        //추가됐을 때만 나타남
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Log.i("ㅗㅗㅗㅗ", "됐다");
            }
        });
    }


    @Override
    public void onsetlist(String text) {
        list.add(new Page3_1_1_dargData(text , number));
        adapter.notifyDataSetChanged();
    }

    //알고리즘 값을 정리
    public void ongetdata(String result) {

        //줄바꿈 단위로 나눈 것을 개수/출발/시간/도착 으로 쪼갬
        String[] split_result = result.split("\n");
        for (int i = 0; i < split_result.length; i++) {
            String[] split_result2 = split_result[i].split(",");

            //그냥 지나감
            if (split_result[i].contains("개수"))
                continue;

            else if (split_result[i].contains("출발")) {
                result_name.add(split_result2[1]);
                if (!checkStart) {
                    result_number.add("출발");
                    checkStart = true;
                } else
                    result_number.add("경유");
            }

            else if (split_result[i].contains("시간")) {
                continue;
            }

            else if (split_result[i].contains("도착") && i == split_result.length - 1) {
                result_name.add(split_result2[1]);
                result_number.add("도착");
            }

            else if (split_result[i].contains("환승")) {
                result_name.add(split_result2[1]);
                result_number.add("환승");
            }
        }

        for (int i = 0; i < result_name.size(); i++) {
            add_list.add(new item_data(result_number.get(i), result_name.get(i)));
        }

    }

    public static class item_data  {
        String number;
        String name;

        String getNumber() {
            return this.number;
        }

        String getName() {
            return this.name;
        }

        public item_data(String number, String name) {
            this.number  = number;
            this.name  = name;
        }
    }
}
