package text;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attachpage3_200428_mj.Page3_Main;
import com.example.attachpage3_200428_mj.R;

import com_page2_1.Page2_1_MainActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*
     * subject_1 = 자연
     * subject_2 = 역사
     * subject_3 = 휴양
     * subject_4 = 체험
     * subject_5 = 산업
     * subject_6 = 건축/조형
     * subject_7 = 문화
     * subject_8 = 레포츠
     */

    int[] button_id = {R.id.subject_1, R.id.subject_2, R.id.subject_3, R.id.subject_4, R.id.subject_5, R.id.subject_6, R.id.subject_7, R.id.subject_8};
    String[] subject_name = {"자연", "역사", "휴양", "체험", "산업", "건축/조형", "문화", "레포츠"};
    Button[] subject = new Button[button_id.length];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = 0; i<button_id.length; i++){
            subject[i] = (Button) findViewById(button_id[i]);
            subject[i].setOnClickListener(this);
        }

        Button go_page = (Button) findViewById(R.id.go_page_btn);
        go_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Page3_Main.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View v) {
        for(int i =0; i < subject.length; i++) {

            if(subject[i].getId() == v.getId()) {
                Intent intent = new Intent(MainActivity.this, Page2_1_MainActivity.class);
                intent.putExtra("subject_name", subject_name[i]);
                startActivity(intent);

            }
        }
    }
}
