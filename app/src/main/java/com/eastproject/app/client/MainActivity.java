package com.eastproject.app.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.eastproject.app.R;

public class MainActivity extends AppCompatActivity {
    private Button mLoginBtn;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mText = (TextView) findViewById(R.id.text);

//        Call call = new EASTClient().getCall();
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                mText.setText(response.body().string());
//            }
//        });
    }
}
