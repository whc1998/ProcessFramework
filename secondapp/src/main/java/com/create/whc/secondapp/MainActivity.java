package com.create.whc.secondapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.create.whc.baselib.ProcessManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProcessManager.getInstance().connect(this,"com.create.whc.processframework");

        findViewById(R.id.getPerson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IUserManager userManager = ProcessManager.getInstance().getInstance(IUserManager.class);
                Toast.makeText(MainActivity.this,"---->"+userManager.getPerson(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
