package Page3_1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attachpage3_200428_mj.R;

public class Page3_1_fragment2 extends Fragment {

    public Page3_1_fragment2(){}

    public static Page3_1_fragment2 newInstance(){
        Bundle args = new Bundle();
        Page3_1_fragment2 fragment2 = new Page3_1_fragment2();
        fragment2.setArguments(args);
        return fragment2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page3_1_viewpager_map, container, false);

        WebView web = (WebView)v.findViewById(R.id.page3_1_map);

        //웹뷰 자바스크립트 사용가능하도록 선언
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setDisplayZoomControls(false);  //웹뷰 돋보기 없앰
        web.setScaleX(1.15f);
        web.setScaleY(1.2f);


        //웹뷰 줌기능
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setSupportZoom(true);



        //웹뷰를 로드함
        web.setWebViewClient(new WebViewClient());
        web.loadUrl("file:///android_asset/map.html");


        return v;
    }
}
