package Page3_1_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.attachpage3_200428_mj.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import Page3_1.Page3_1_fragment1;


public class Page3_1_1_addConformDialog extends BottomSheetDialogFragment {

    Page3_1_1_Main page3_1_1_main;
    GoAlgorithPage goAlgorithPage;   //인터페이스
    String name;



    public static Page3_1_1_addConformDialog getInstance(String name){
        Page3_1_1_addConformDialog dialog = new Page3_1_1_addConformDialog();
        Bundle args = new Bundle();
        args.putString("name", name);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        page3_1_1_main = (Page3_1_1_Main) getActivity();

        if(context instanceof Page3_1_1_addConformDialog.GoAlgorithPage){
            goAlgorithPage = (Page3_1_1_addConformDialog.GoAlgorithPage) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "야 오류났다.");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.page3_1_1_addconfirm, container, false);

        //액티비티에서 값을 전달 받음
        Bundle extra = getArguments();
        name = extra.getString("name");

        TextView stationName = rootview.findViewById(R.id.page3_1_1_addStation);
        stationName.setText(name);

        //새로운 경로 찾기 버튼을 누르면
        RelativeLayout newRoute_btn = rootview.findViewById(R.id.page3_1_1_newroute_btn);
        newRoute_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAlgorithPage.go_algorithm_page();
            }
        });


        return rootview;
    }



    //인터페이스 구현
    public interface GoAlgorithPage {
        void go_algorithm_page();
    }
}
