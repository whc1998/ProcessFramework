package com.create.whc.processframework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.create.whc.baselib.ProcessManager;

public class SecondActivity extends AppCompatActivity {

    private IUserManager userManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondview);
        ProcessManager.getInstance().connect(this);


        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userManager=ProcessManager.getInstance().getInstance(IUserManager.class);
                Log.i("whc", "onClick: "+userManager);
            }
        });


        findViewById(R.id.getResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Person person = userManager.getPerson();
//                Toast.makeText(SecondActivity.this, "--->"+person.toString(), Toast.LENGTH_SHORT).show();
                    userManager.setPerson(new Person("吴洪春","2018"));
            }
        });

    }
}
