package Page3_1_1_1;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.attachpage3_200428_mj.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import DB.DbOpenHelper;
import Page3_1_1.Page3_1_1_Main;
import Page3_1_1.Page3_1_1_addBottomAdapter;

public class Page3_1_1_1_addCityBottomSheet extends BottomSheetDialogFragment {
    Page3_1_1_1_Main page3_1_1_1_main;

    private ListView listView;          // 검색을 보여줄 리스트변수
    private Page3_1_1_1_addCityBottomAdapter adapter;      // 리스트뷰에 연결할 아답터
    private onSetList listener;             //page3_1_1에 값을 전달하기 위한

    //데이터베이스 관련
    long nowIndex;
    String sort = "userid";
    ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    static ArrayList<String> arrayCityName = new ArrayList<String>();
    private DbOpenHelper mDbOpenHelper;


    public static Page3_1_1_1_addCityBottomSheet getInstance() {
        return new Page3_1_1_1_addCityBottomSheet();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        page3_1_1_1_main = (Page3_1_1_1_Main) getActivity();

        if(context instanceof onSetList){
            listener = (onSetList) context;
        } else {
            throw new RuntimeException(context.toString()
            + "야 오류났다.");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.page3_1_1_1_addcity, container, false);

        adapter = new Page3_1_1_1_addCityBottomAdapter(arrayData, page3_1_1_1_main);
        listView = (ListView) rootview.findViewById(R.id.api_list_city);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onClickListener);

        mDbOpenHelper = new DbOpenHelper(page3_1_1_1_main);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        showDatabase(sort);


        //바틈시트 특성상 아래로 드래그하면 바로 다이얼로그가 사라져버림
        //아래로 드래그 했을대 listview가 드래그 되도록 하기 위한 메소드
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


        return rootview;
    }


    //바텀시트에서 관광지를 선택하면
    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(listener != null){
                    listener.onsetlist(arrayData.get(position), arrayCityName.get(position));
                }
                 dismiss();
            }
        };




    //데이터베이스 가져오는 부분
    public void showDatabase(String sort){
        Cursor iCursor = mDbOpenHelper.selectColumns();
        //iCursor.moveToFirst();
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();

        while(iCursor.moveToNext()){
            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
            String tempCityname = iCursor.getString(iCursor.getColumnIndex("cityname"));

            String Result = "(" + tempCityname + ")" + tempName;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
            arrayCityName.add(tempCityname);
        }

        adapter.notifyDataSetChanged();
    }


    //인터페이스 구현-바텀시트 아이템 선택하면 리사이클러뷰에 넣어주기 위함
    public interface onSetList {
        void onsetlist(String text, String cityname);
    }

}

