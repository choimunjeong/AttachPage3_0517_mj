package com.example.attachpage3_200428_mj;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidtagview.ColorFactory;
import com.example.androidtagview.TagContainerLayout;
import com.example.androidtagview.TagView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Page3_1.Page3_1_Main;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class Page3_Main extends AppCompatActivity  {

    //태그 관련 변수
    private TagContainerLayout mTagContainerLayout1;
    boolean startOk = false;
    boolean endOk = false;
    boolean middleOk = false;
    boolean dateOk = false;

    //svg 지도
    WebView page3_svg;
    RelativeLayout page3_svg_bg;
    ScrollView scrollView;

    //이용권
    Button dayPass_3, dayPass_5, dayPass_7;
    String dayPass = "";

    // 날짜 관련 변수들
    Calendar myCalendar = Calendar.getInstance();
    EditText editDate;
    SimpleDateFormat sdf;
    String time;

    //txt 관련 변수
    int i = 0;
    String readStr = "";
    private List<String> list;  //데이터를 넣을 리스트 변수
    Handler handler = new Handler();
    String[] code_name = null;
    String[] code = new String[237];
    String[] name = new String[237];


    //날짜 값을 받아온다.
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };


    //다음 페이지로 값을 전달할 list
    ArrayList <send_data> send_list = new ArrayList<send_data>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3_main);

        //이용권 버튼,텍스트
        dayPass_3 = (Button) findViewById(R.id.dayPass_3);
        dayPass_5 = (Button) findViewById(R.id.dayPass_5);
        dayPass_7 = (Button) findViewById(R.id.dayPass_7);
        editDate = (EditText) findViewById(R.id.page3_date);


        //이용권 버튼 클릭시 테두리와 글자색 바꾸기
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dayPass_3:
                        dateOk = true;
                        dayPass = "3일권";
                        dayPass_3.setSelected(true);
                        dayPass_5.setSelected(false);
                        dayPass_7.setSelected(false);
                        dayPass_3.setTextColor(getResources().getColorStateList(R.color.btn_ticket_text));
                        break;
                    case R.id.dayPass_5:
                        dateOk = true;
                        dayPass = "5일권";
                        dayPass_5.setSelected(true);
                        dayPass_3.setSelected(false);
                        dayPass_7.setSelected(false);
                        dayPass_5.setTextColor(getResources().getColorStateList(R.color.btn_ticket_text));
                        break;
                    case R.id.dayPass_7:
                        dateOk = true;
                        dayPass = "7일권";
                        dayPass_7.setSelected(true);
                        dayPass_3.setSelected(false);
                        dayPass_5.setSelected(false);
                        dayPass_7.setTextColor(getResources().getColorStateList(R.color.btn_ticket_text));
                        break;
                }
            }
        };

        dayPass_3.setOnClickListener(onClickListener);
        dayPass_5.setOnClickListener(onClickListener);
        dayPass_7.setOnClickListener(onClickListener);


        // 현재 날짜 출력
        String myFormat = String.format("%s%18s%18s", "yyyy년", "MM월  ", "dd일  ");
        sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        time = sdf.format(myCalendar.getTime());
        editDate.setText(time);


        // 출발 날짜 클릭 시 DatePicker 보여주기
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Page3_Main.this, myDatePicker,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //자동완성-검색 리스트 구현 부분
        list = new ArrayList<String>();            //리스트를 생성
        settingList();                             //리스트에 검색될 단어를 추가한다


        //자동완성
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.searchStation_page3);    //객체 연결
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list));   //아답터에 연결


        //자동입력 누르면 스크롤이 맨 아래로 내려감
        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i("눌렀는디", "왜 아무런,");
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                return false;
            }
        });


        //자동입력에서 항목을 터치했을 때, 키보드가 바로 내려감 + 웹뷰에서 해당역에 출경도 버튼 띄워짐
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (autoCompleteTextView.getText().toString() != null) {
                    page3_svg.loadUrl("javascript:setMessage('" + autoCompleteTextView.getText().toString() + "')");
                }
                //키보드 내림
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
            }
        });


        // svg 지도
        page3_svg = (WebView) findViewById(R.id.page3_svg);
        page3_svg_bg = (RelativeLayout) findViewById(R.id.page3_svg_bg);
        scrollView = (ScrollView) findViewById(R.id.page3_scrollView) ;


        //지도를 줌인 할 때, 스크롤뷰(부모뷰)의 영향을 받지 않는다. + 테두리가 생긴다.
        page3_svg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                page3_svg_bg.setSelected(true);
                return false;
            }
        });


        //지도가 아닌 배경을 눌렀을때 지도의 테두리를 없애줌 + 키보드 내림
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                page3_svg_bg.setSelected(false);
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
                return false;
            }
        });


        //웹뷰 자바스크립트 사용가능하도록 선언
        page3_svg.getSettings().setJavaScriptEnabled(true);
        page3_svg.getSettings().setLoadWithOverviewMode(true);
        page3_svg.getSettings().setDisplayZoomControls(false);  //웹뷰 돋보기 없앰
        page3_svg.getSettings().setUseWideViewPort(true);
        page3_svg.getSettings().setBuiltInZoomControls(true);   //웹뷰 줌기능
        page3_svg.getSettings().setSupportZoom(true);
        page3_svg.setWebViewClient(new WebViewClient());
        page3_svg.setHorizontalScrollBarEnabled(false);         //스크롤바 안보이게함
        page3_svg.setVerticalScrollBarEnabled(false);

        //웹뷰를 로드함
        page3_svg.loadUrl("file:///android_asset/index.html");


        //태그뷰 관련
        final List<String> list1 = new ArrayList<String>();
        mTagContainerLayout1 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout1);


        //태그뷰를 커스텀
        mTagContainerLayout1.setDragEnable(false);                                           //태그뷰에서 드래그 안씀
        mTagContainerLayout1.setTheme(ColorFactory.NONE);                                    //테마를 커스텀
        mTagContainerLayout1.setTagBorderRadius(100f);                                       //태그 아이템의 모서리를 둥글게
        mTagContainerLayout1.setTagBorderColor(Color.parseColor("#FE9D0D"));     //태그 아이템의 테두리 색
        mTagContainerLayout1.setTagBackgroundColor(Color.parseColor("#FE9D0D")); //태그 아이템의 배경 색
        mTagContainerLayout1.setCrossAreaPadding(45f);                                       // X<-의 크기를 조절
        mTagContainerLayout1.setCrossColor(Color.parseColor("#FE9D0D"));         // X<-의 색
        mTagContainerLayout1.setBackgroundColor(Color.parseColor("#FFFFFF"));    //태그뷰 배경 색
        mTagContainerLayout1.setBorderColor(Color.parseColor("#FFFFFF"));        //태그뷰의 모서리를 둥글게
        //박스 생기는 거 나중에  애니메이션 주자.


        //태그뷰 글꼴
        Typeface typeface = Typeface.createFromAsset(getAssets(), "notosans_regular.ttf");
        mTagContainerLayout1.setTagTypeface(typeface);
        mTagContainerLayout1.setTagTextColor(Color.parseColor("#ffffff"));
        mTagContainerLayout1.setTagTextSize(55);
        mTagContainerLayout1.setTagBdDistance(5f);      //태그 아이템의 밑위 간격
        mTagContainerLayout1.setTags(list1);            //위에서 커스텀한 태그뷰를 set


        //태그뷰를 선택했을 때, 메소드들
        mTagContainerLayout1.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) { }         //태그뷰를 단순 터치

            @Override
            public void onTagLongClick(int position, String text) { }     //태그뷰를 롱터치

            @Override
            public void onSelectedTagDrag(int position, String text) { }   //태그뷰를 드래그

            @Override
            public void onTagCrossClick(int position) {                    // X <- 눌러서 지우는 메소드
                if (position < mTagContainerLayout1.getChildCount()) {

                    //경유만 삭제할 수 있도록 함
                    if(position != 0 & position != mTagContainerLayout1.size()){
                        mTagContainerLayout1.removeTag(position);
                        page3_svg.loadUrl("javascript:deletePin_middle('"+"2_"+list1.get(position)+"')");   //svg 지도에서 경유 아이콘 삭제
                        list1.remove(position);

                        //경유역이 모두 삭제되면
                        if(list1.size() == 2) middleOk = false;
                    }
                }
            }
        });


        //지도에서 역 누르면 태그뷰에 추가되는 부분
        page3_svg.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void send(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run(){

                        //번호_역이름  <-이렇게 전달되는 걸 '_' 단위로 쪼개서 isStart에 넣는다.
                        String[] isStart = msg.split("_");

                        //태그뷰에 아이템이 하나도 없을 때, 출발역이 먼저 선택되어야 한다.
                        if(list1.size() == 0){

                            //출발역인 경우에
                            if (msg.contains("1")) {
                                //태그 아이템의 색을 지정, 태그뷰에 추가
                                mTagContainerLayout1.setTagBackgroundColor(Color.parseColor("#FE9D0D"));
                                mTagContainerLayout1.setTagBorderColor(Color.parseColor("#FE9D0D"));
                                mTagContainerLayout1.addTag(isStart[1]);
                                list1.add(isStart[1]);
                                startOk = true;
                            }

                            //경유역일 때
                            else if (msg.contains("2")){
                                page3_svg.loadUrl("javascript:deletePin_middle('"+"2_"+isStart[1]+"')");
                                startFirst_Show();
                            }

                            //도착역일 때
                            else {
                                page3_svg.loadUrl("javascript:deletePin_end()");
                                startFirst_Show();
                            }
                        }

                        else{
                            for(i=0;i < list1.size(); i++) {

                                //추가할 자리가 없을 때 (태그뷰 아이템 개수 제한 ( 최대 역 개수 = 10개)
                                if (list1.size() == 10 && msg.contains("2")) {
                                    no_Show();
                                    break;
                                }

                                //출발역인 경우에 기존의 출발역 정보를 지우고 새로 갱신한다.
                                if (msg.contains("1")) {
                                    mTagContainerLayout1.removeTag(0);
                                    list1.remove(0);
                                    mTagContainerLayout1.setCrossColor(Color.parseColor("#FE9D0D"));
                                    mTagContainerLayout1.setTagBackgroundColor(Color.parseColor("#FE9D0D"));
                                    mTagContainerLayout1.setTagBorderColor(Color.parseColor("#FE9D0D"));
                                    mTagContainerLayout1.addTag(isStart[1], 0);    //첫번째 자리에 추가 또는 갱신
                                    list1.add(0,isStart[1]);                         //첫번째 자리에 추가 또는 갱신
                                    startOk = true;
                                    break;
                                }


                                //경유역인 경우에, 출발역과 도착역 사이에 계속 추가가 되어야 한다.
                                else if (msg.contains("2")) {
                                    middleOk = true;

                                    //도착역이 선택 되어있으면, 도착역을 뒤로 밀어준다.
                                    if(endOk) {
                                        //도착역의 색상
                                        mTagContainerLayout1.setTagBackgroundColor(Color.parseColor("#FE800D"));
                                        mTagContainerLayout1.setTagBorderColor(Color.parseColor("#FE800D"));

                                        //도착역을 뒤로 밀고, 기존 도착역을 삭제한다.
                                        mTagContainerLayout1.addTag(list1.get(list1.size()-1), mTagContainerLayout1.size());
                                        list1.add(list1.size(), list1.get(list1.size()-1));
                                        mTagContainerLayout1.removeTag(mTagContainerLayout1.size()-1);
                                        list1.remove(list1.size()-1);

                                        //경유역을 도착역 앞에 생성한다.
                                        mTagContainerLayout1.setCrossColor(Color.parseColor("#1B503D"));         // X<-의 색
                                        mTagContainerLayout1.setTagBackgroundColor(Color.parseColor("#4DD9A9"));
                                        mTagContainerLayout1.setTagBorderColor(Color.parseColor("#4DD9A9"));
                                        mTagContainerLayout1.addTag(isStart[1], mTagContainerLayout1.size()-1);
                                        list1.add(list1.size()-1, isStart[1]);
                                    } else {
                                        mTagContainerLayout1.setCrossColor(Color.parseColor("#1B503D"));         // X<-의 색
                                        mTagContainerLayout1.setTagBackgroundColor(Color.parseColor("#4DD9A9"));
                                        mTagContainerLayout1.setTagBorderColor(Color.parseColor("#4DD9A9"));
                                        mTagContainerLayout1.addTag(isStart[1]);
                                        list1.add(isStart[1]);
                                    }
                                    break;
                                }


                                //도착역인 경우에, 항상 뒤에 추가가 된다.
                                else if (msg.contains("3")) {

                                    //기존의 도착역을 삭제하고 갱신한다.
                                    if(endOk){
                                        mTagContainerLayout1.removeTag(mTagContainerLayout1.size()-1);
                                        list1.remove(list1.size()-1);
                                    }
                                    mTagContainerLayout1.setCrossColor(Color.parseColor("#FE800D"));         // X<-의 색
                                    mTagContainerLayout1.setTagBackgroundColor(Color.parseColor("#FE800D"));
                                    mTagContainerLayout1.setTagBorderColor(Color.parseColor("#FE800D"));
                                    mTagContainerLayout1.addTag(isStart[1], mTagContainerLayout1.size());         //항상 뒤에 추가 됨
                                    list1.add(list1.size(),isStart[1]);                                           //항상 뒤에 추가 됨
                                    endOk = true;
                                    break;
                                }
                            }
                        }

                    }
                });
            }
        }, "android");



        //다음 페이지에 값을 넘김
        final Button send_btn = (Button)findViewById(R.id.page3_search_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //출발, 도착, 경유역이 선택이 안되어 있으면 오류메시지
                if(startOk==false || endOk == false || middleOk ==false || dateOk == false){
                    err_show();
                } else {
                    //선택된 역 이름을 번호와 함께 넘겨준다.
                    for(int i=0; i<list1.size(); i++){
                        for(int j=0; j<237; j++){
                            if(list1.get(i).equals(name[j])){
                                send_list.add(new send_data(code[j],name[j]) );
                            }
                        }
                    }

                    Intent intent = new Intent(getApplicationContext(), Page3_1_Main.class);
                    intent.putExtra("list", (Serializable) send_list);           //추가된 역
                    intent.putExtra("date", editDate.getText().toString());  //날짜
                    intent.putExtra("dayPass", dayPass);                     //일권
                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
    }


    //화면이 꺼지면 리스트를 삭제
    @Override
    protected void onPause() {
        super.onPause();
        send_list.clear();
    }


    //선택된 날짜를 edittext에 적용
    private void updateLabel() {
        String myFormat = String.format("%s%18s%18s", "yyyy년", "MM월  ", "dd일  ");
        sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        EditText editDate = (EditText) findViewById(R.id.page3_date);
        time = sdf.format(myCalendar.getTime());
        editDate.setText(time);
    }


    //경유 추가하는데 자리가 없으면 없다고 다이얼로그 띄움
    void no_Show(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("추가할 수 있는 개수를 초과했습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }


    //태그뷰의 첫번재의 입력값은 출발역이어야 함
    void startFirst_Show(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("출발역을 먼저 선택해 주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }


    //입력되지 않은 항목이 있으면 다이얼로그 띄움
    void err_show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("입력되지 않은 항목이 있습니다. 한번 더 확인 해 주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


    //리스트에 검색될 단어를 추가한다. txt파일을 for문으로 쪼개서 넣음
    private void settingList() {
        AssetManager am = getResources().getAssets();
        InputStream is = null;
        try {
            is = am.open("station3.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String str = null;
            while (((str = reader.readLine()) != null)) {
                readStr += str + "\n";
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] arr = readStr.split("\n");  //한 줄씩 자른다.

        //code,name으로 되어있는 line을 ','를 기준으로 다시 자른다.
        for (int i = 0; i < arr.length; i++) {
            code_name = arr[i].split(",");

            code[i] = code_name[0];
            name[i] = code_name[1];

            list.add(name[i]);
        }
    }


}


