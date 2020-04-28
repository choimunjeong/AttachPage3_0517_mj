package Page3_1_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attachpage3_200428_mj.R;

import java.util.ArrayList;

public class Page3_1_1_Main extends AppCompatActivity implements Page3_1_1_addBottomSheet.onSetList {
    Page3_1_1_Main page3_1_1_main;
    RecyclerView recyclerView;

    private ArrayList<Page3_1_1_dargData> list = new ArrayList<>();
    Page3_1_1_adapter adapter = new Page3_1_1_adapter(list);
    ArrayList<String> next_data;
    int number = 0;

    Button add_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_1_1_main);

        //앞에서 보내는 값을 받음
        Intent intent = getIntent();
        next_data = (ArrayList<String>) intent.getSerializableExtra("next_data");

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


        for(int i =0; i < next_data.size(); i++){
            if(!next_data.get(i).contains("환승")){
                String[] split_data = next_data.get(i).split(",");
                list.add(new Page3_1_1_dargData(split_data[1] , number));
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

                //AddSpotBottomSheetDialog addSpotBottomSheetDialog = AddSpotBottomSheetDialog.getInstance();
                //addSpotBottomSheetDialog.show(getSupportFragmentManager(), "BottomSheet");

            }
        });

    }


    @Override
    public void onsetlist(String text) {
        list.add(new Page3_1_1_dargData(text , number));
        adapter.notifyDataSetChanged();
    }
}
