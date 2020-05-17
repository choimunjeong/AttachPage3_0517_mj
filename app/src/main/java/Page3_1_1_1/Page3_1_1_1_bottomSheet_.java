package Page3_1_1_1;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attachpage3_200428_mj.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import DB.DbOpenHelper;
import Page3_1_1.Page3_1_1_addConformDialog;

public class Page3_1_1_1_bottomSheet_ extends BottomSheetDialogFragment implements Page3_1_1_1_bottomSheetAdapter.onSetList{
    Context context = getContext();
    RecyclerView recyclerView;
    Page3_1_1_1_Main page3_1_1_1_main;
    Page3_1_1_1_bottomSheet_Adapter adapter;

    ListView listView;
    int postion2;

    String receiveMsg;
    String [] data_split;
    ArrayList<Page3_1_1_1_bottomSheetAdapter.Api_Item> completeList;             //정제된 리스트값
    ArrayList<Page3_1_1_1_bottomSheetAdapter.Api_Item> header_data;              //정제전 헤더값
    ArrayList<Page3_1_1_1_bottomSheetAdapter.Api_Item> child1_data;               //정제전 차일드 값(경유1
    ArrayList<Page3_1_1_1_bottomSheetAdapter.Api_Item> child2_data;               //정제전 차일드 값9경유2
    ArrayList<Page3_1_1_1_bottomSheetAdapter.Api_Item> child3_data;               //정제전 차일드 값(경유3
    String[] arr_line;
    String[] arr_all;
    String[] _name = new String[238];           //txt에서 받은 역이름
    String[] _code = new String[238];           //txt에서 받은 역코드
    String startCode, endCode, trainCode;
    String[] trainCodelist = {"01", "02", "03", "04", "08", "09", "15"};
    int isMiddle = 0;
    String date;
Page3_1_1_1_trainAdapter.ItemViewHolder itemViewHolder;


    public static Page3_1_1_1_bottomSheet_ getInstance() {
        return new  Page3_1_1_1_bottomSheet_();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        page3_1_1_1_main = (Page3_1_1_1_Main) getActivity();
        this.context =context;



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.page3_1_1_1_apisheet, container, false);

        settingList();

        listView = (ListView)rootview.findViewById(R.id.api_list);

        completeList = new ArrayList<Page3_1_1_1_bottomSheetAdapter.Api_Item>();
        header_data = new ArrayList<Page3_1_1_1_bottomSheetAdapter.Api_Item>();
        child1_data = new ArrayList<>();
        child2_data = new ArrayList<>();
        child3_data = new ArrayList<>();
        adapter = new Page3_1_1_1_bottomSheet_Adapter(completeList, context);
        listView.setAdapter(adapter);

        completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "11", "aa", "bb","cc"));
        completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, "22", "aa", "bb","cc"));
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("vhwltus?", String.valueOf(postion2));

                dismiss();
            }
        });
//        listView.setOnItemClickListener(onClickListener);
        return rootview;
    }






////    //바텀시트에서 시간을 선택하면
//    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            Log.i("포지션이뭐지", String.valueOf(position));
//            Log.i("id가 뭐지", String.valueOf(id));
//            TextView text = view.findViewById(R.id.searchTime_Page3_1_1);
//            text.setText("됐나");
////            if(onsetTrainTime != null){
////                Log.i("직행일때 시간 길이", String.valueOf(completeList.get(position).getDepTime().length()));
////                String[] startTIme = completeList.get(position).getDepTime().split("\n");
////                String[] endTime = completeList.get(position).getArrTime().split("\n");
////                onsetTrainTime.onset_traintime(startTIme[0]+"-"+endTime[endTime.length-1]);
////            }
//            //dismiss();
//        }
//
//    };
//
//    @Override
//    public void send(ArrayList<String> data, String date) {
//        data_split = data.get(0).split(",");
//        this.date = date;
//
//            for(int p =0; p < data_split.length-1; p++){
//
//                if(p != 0)
//                    isMiddle++;
//
//                compareStation(data_split[p], data_split[p+1]);
//
//                //열차 종류별 api 검색(1)
//                for(int i =0; i < trainCodelist.length; i++){
//                    trainCode = trainCodelist[i];
//                    try {
//                        new Task().execute().get();
//                        trianjsonParser(receiveMsg);
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//        }
//
//        //출발 시간 정렬
//        Collections.sort(header_data);
//
//        //직행이라면
//        if(isMiddle==0){
//            completeList = header_data;
//        }
//
//        //환승한다면
//        else{
//            switch (isMiddle){
//
//                //1회 환승
//                case 1:
//                    Collections.sort(child1_data);
//
//                    //첫번째 기차의 시간표이 기준
//                    for(int i =0; i < header_data.size(); i++){
//                        String [] header_time_split = header_data.get(i).getArrTime().split(":");
//
//                        //도착시간이 자정을 넘기면 스케줄에 포함하지 않음
//                        if(Integer.parseInt(header_time_split[0].replaceAll("[^0-9]", "")) < 3)
//                            continue;
//
//                        //도착시간과 환승역의 출발시간을 비교해서 리스트에 넣어줌
//                        for(int p=0; p < child1_data.size(); p++) {
//                            String [] child1_time_split = child1_data.get(p).getDepTime().split(":");
//                            if(Integer.parseInt(header_time_split[0]) <= Integer.parseInt(child1_time_split[0])
//                            && Integer.parseInt(header_time_split[1])+10 < Integer.parseInt(child1_time_split[1]) ){
//                                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(
//                                            Page3_1_1_1_bottomSheetAdapter.HEADER,
//                                            header_data.get(i).getDepTime()+"\n"+child1_data.get(p).getDepTime(),
//                                            header_data.get(i).getArrTime()+ "\n" + child1_data.get(p).getArrTime(),
//                                            header_data.get(i).getSpendTime() + "\n" + child1_data.get(p).getSpendTime(),
//                                            header_data.get(i).getTrainNumber() + "\n" + child1_data.get(p).getTrainNumber()
//                                    ));
//                                    break;
//                            }
//                        }
//                    }
//                    break;
//
//
//                //2회 환승
//                case 2:
//                    Collections.sort(child1_data);
//                    Collections.sort(child2_data);
//
//                    //첫번째 기차의 시간표이 기준
//                    for(int i =0; i < header_data.size(); i++){
//                        String [] header_time_split = header_data.get(i).getArrTime().split(":");
//
//                        //도착시간이 자정을 넘기면 스케줄에 포함하지 않음
//                        if(Integer.parseInt(header_time_split[0].replaceAll("[^0-9]", "")) < 3)
//                            continue;
//
//                        //환승(1)
//                        for(int p=0; p < child1_data.size(); p++) {
//                            String [] child1_Deptime_split = child1_data.get(p).getDepTime().split(":");
//                            String [] child1_Arrtime_split = child1_data.get(p).getArrTime().split(":");
//
//                            //도착시간이 자정을 넘기면 스케줄에 포함하지 않음
//                            if(Integer.parseInt(child1_Arrtime_split[0].replaceAll("[^0-9]", "")) < 3)
//                                continue;;
//
//                            //환승(2)
//                            //도착시간과 환승역의 출발시간을 비교해서 리스트에 넣어줌
//                            for(int t=0; t < child2_data.size(); t++){
//                                String [] child2_time_split = child2_data.get(t).getDepTime().split(":");
//
//                                if(Integer.parseInt(header_time_split[0]) <= Integer.parseInt(child1_Deptime_split[0])
//                                && Integer.parseInt(header_time_split[1])+10 < Integer.parseInt(child1_Deptime_split[1])
//                                && Integer.parseInt(child1_Arrtime_split[0]) <= Integer.parseInt(child2_time_split[0])
//                                && Integer.parseInt(child1_Arrtime_split[1])+10  < Integer.parseInt(child2_time_split[1])  ){
//
//                                    completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(
//                                            Page3_1_1_1_bottomSheetAdapter.HEADER,
//                                            header_data.get(i).getDepTime()+"\n"+child1_data.get(p).getDepTime() +"\n"+child2_data.get(t).getDepTime(),
//                                            header_data.get(i).getArrTime()+ "\n" + child1_data.get(p).getArrTime() + "\n" + child2_data.get(t).getArrTime(),
//                                            header_data.get(i).getSpendTime() + "\n" + child1_data.get(p).getSpendTime() + "\n" + child2_data.get(t).getSpendTime(),
//                                            header_data.get(i).getTrainNumber() + "\n" + child1_data.get(p).getTrainNumber() + "\n" + child2_data.get(t).getTrainNumber()
//                                    ));
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    break;
//
//
//                //3회 환승
//                case 3:
//                    Collections.sort(child1_data);
//                    Collections.sort(child2_data);
//                    Collections.sort(child3_data);
//
//                    //첫번째 기차의 시간표이 기준
//                    for(int i =0; i < header_data.size(); i++){
//                        String [] header_time_split = header_data.get(i).getArrTime().split(":");
//
//                        //도착시간이 자정을 넘기면 스케줄에 포함하지 않음
//                        if(Integer.parseInt(header_time_split[0].replaceAll("[^0-9]", "")) < 3)
//                            continue;
//
//                        //환승(1)
//                        for(int p=0; p < child1_data.size(); p++) {
//                            String [] child1_Deptime_split = child1_data.get(p).getDepTime().split(":");
//                            String [] child1_Arrtime_split = child1_data.get(p).getArrTime().split(":");
//
//                            //도착시간이 자정을 넘기면 스케줄에 포함하지 않음
//                            if(Integer.parseInt(child1_Arrtime_split[0].replaceAll("[^0-9]", "")) < 3)
//                                continue;
//
//                            //환승(2)
//                            for(int t=0; t < child2_data.size(); t++){
//                                String [] child2_Deptime_split = child2_data.get(t).getDepTime().split(":");
//                                String [] child2_Arrtime_split = child2_data.get(t).getArrTime().split(":");
//
//                                //도착시간이 자정을 넘기면 스케줄에 포함하지 않음
//                                if(Integer.parseInt(child2_Arrtime_split[0].replaceAll("[^0-9]", "")) < 3)
//                                    continue;
//
//                                //환승(3)
//                                //도착시간과 환승역의 출발시간을 비교해서 리스트에 넣어줌
//                                for(int g = 0; g < child3_data.size(); g++){
//                                    String [] child3_time_split = child3_data.get(g).getDepTime().split(":");
//
//                                    if(Integer.parseInt(header_time_split[0]) <= Integer.parseInt(child1_Deptime_split[0])
//                                            && Integer.parseInt(header_time_split[1])+10 < Integer.parseInt(child1_Deptime_split[1])
//                                            && Integer.parseInt(child1_Arrtime_split[0]) <= Integer.parseInt(child2_Deptime_split[0])
//                                            && Integer.parseInt(child1_Arrtime_split[1])+10  < Integer.parseInt(child2_Deptime_split[1])
//                                            && Integer.parseInt(child2_Arrtime_split[0]) <= Integer.parseInt(child3_time_split[0])
//                                            && Integer.parseInt(child2_Arrtime_split[1])+10  <  Integer.parseInt(child3_time_split[1])  ){
//
//                                        completeList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(
//                                                Page3_1_1_1_bottomSheetAdapter.HEADER,
//                                                header_data.get(i).getDepTime()+"\n"+child1_data.get(p).getDepTime() +"\n"+child2_data.get(t).getDepTime() +"\n"+child3_data.get(g).getDepTime(),
//                                                header_data.get(i).getArrTime()+ "\n" + child1_data.get(p).getArrTime() + "\n" + child2_data.get(t).getArrTime() +"\n"+child3_data.get(g).getArrTime(),
//                                                header_data.get(i).getSpendTime() + "\n" + child1_data.get(p).getSpendTime() + "\n" + child2_data.get(t).getSpendTime() +"\n"+child3_data.get(g).getSpendTime(),
//                                                header_data.get(i).getTrainNumber() + "\n" + child1_data.get(p).getTrainNumber() + "\n" + child2_data.get(t).getTrainNumber() +"\n"+child3_data.get(g).getTrainNumber()
//                                        ));
//                                        break;
//                                    }
//                                }
//
//
//                            }
//                        }
//                    }
//                    break;
//            }
//        }
//
//
//    }
//
//    @Override
//    public void send_position(int date) {
//        postion2 = date;
//        Log.i("포지션", String.valueOf(postion2));
//    }

    @Override
    public void onsetlist(String text) {

    }


    public  class  Task extends AsyncTask<String, Void, String> {
        private String str;

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try{
                url = new URL("http://openapi.tago.go.kr/openapi/service/TrainInfoService/" +
                        "getStrtpntAlocFndTrainInfo?serviceKey=7LT0Q7XeCAuzBmGUO7LmOnrkDGK2s7GZIJQdvdZ30lf7FmnTle%2BQoOqRKpjcohP14rouIrtag9KOoCZe%2BXuNxg%3D%3D&" +
                        "numOfRows=1" +
                        "&pageNo=1&" +
                        "depPlaceId=" + startCode +
                        "&arrPlaceId=" + endCode +
                        "&depPlandTime=" + date +
                        "&trainGradeCode=" + trainCode +
                        "&_type=json");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    Log.i("ㅗㅗㅗㅗㅗㅗ" , receiveMsg);
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }


    public String[] trianjsonParser(String jsonString){
        String arrplacename = null;
        String arrplandtime = null;
        String depplacename = null;
        String depplandtime = null;
        String traingradename = null;

        String[] arraysum = new String[100];
        String result = "";
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonObject1 = jsonObject.getJSONObject("response");
            JSONObject jsonObject3 = jsonObject1.getJSONObject("body");
            JSONObject jsonObject4 = jsonObject3.getJSONObject("items");
            JSONArray jsonArray = jsonObject4.getJSONArray("item");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.getJSONObject(i);
                arrplacename = jObject.getString("arrplacename");
                arrplandtime = jObject.getString("arrplandtime");
                depplacename = jObject.getString("depplacename");
                depplandtime = jObject.getString("depplandtime");
                traingradename = jObject.getString("traingradename");

                String depTime = depplandtime.substring(8, 12);
                String arrTime = arrplandtime.substring(8, 12);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmm", Locale.KOREA);
                Date dep = simpleDateFormat.parse(depTime);
                Date arr = simpleDateFormat.parse(arrTime);
                long time = (arr.getTime() - dep.getTime()) / 60000;


                switch (isMiddle) {
                    case 0:
                        header_data.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, depTime.substring(0,2)+":"+depTime.substring(2), arrTime.substring(0,2)+":"+arrTime.substring(2), "["+depplacename+"]", traingradename));
                        //arrplacenameList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.HEADER, depTime.substring(0,2)+":"+depTime.substring(2)+depplacename, arrplacename+arrTime.substring(0,2)+":"+arrTime.substring(2), "("+ String.valueOf(time)+"분)", traingradename));
                        break;
                    case 1:
                        child1_data.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.CHILD, depTime.substring(0,2)+":"+depTime.substring(2), arrTime.substring(0,2)+":"+arrTime.substring(2), "["+depplacename+"]", traingradename));
                        //arrplacenameList.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.CHILD, depTime.substring(0,2)+":"+depTime.substring(2)+depplacename, arrplacename+arrTime.substring(0,2)+":"+arrTime.substring(2), "("+ String.valueOf(time)+"분)", traingradename));
                        break;
                    case 2:
                        child2_data.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.CHILD, depTime.substring(0,2)+":"+depTime.substring(2), arrTime.substring(0,2)+":"+arrTime.substring(2), "["+depplacename+"]", traingradename));
                        break;
                    case 3:
                        child3_data.add(new Page3_1_1_1_bottomSheetAdapter.Api_Item(Page3_1_1_1_bottomSheetAdapter.CHILD, depTime.substring(0,2)+":"+depTime.substring(2), arrTime.substring(0,2)+":"+arrTime.substring(2), "["+depplacename+"]", traingradename));
                        break;
                    default:
                        break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  arraysum;

    }


    //txt 돌려 역 비교할 배열 만들기(이름 지역코드 동네코드)<-로 구성
    private void settingList(){
        String readStr = "";
        AssetManager assetManager = context.getResources().getAssets();
        InputStream inputStream = null;
        try{
            inputStream = assetManager.open("stationWithcode.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String str = null;
            while (((str = reader.readLine()) != null)){ readStr += str + "\n";}

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        arr_all = readStr.split("\n"); //txt 내용을 줄바꿈 기준으로 나눈다.

        //한 줄의 값을 띄어쓰기 기준으로 나눠서, 배열에 넣는다.

        for(int i=0; i <arr_all.length; i++) {
            arr_line = arr_all[i].split(",");

            _code[i] = arr_line[0];     //역코드
            _name[i] = arr_line[1];     //이름
        }
    }


    //앞 액티비티에서 선택된 역과 같은 역을 찾는다.
    private void compareStation(String start, String end){
        for(int i=0; i<_name.length; i++){
            if(start.equals(_name[i])){
                startCode = _code[i];
            }
            if(end.equals(_name[i])){
                endCode = _code[i];
            }
        }
        //Log.i("receiveMsg : ", startCode+"-"+ endCode);
    }


}


