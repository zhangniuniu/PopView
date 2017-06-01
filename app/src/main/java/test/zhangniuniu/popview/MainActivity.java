package test.zhangniuniu.popview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CustomPopView customPopView =new CustomPopView(MainActivity.this);

        final View moveView1 = LayoutInflater.from(this).inflate(R.layout.pop_move1, null, false);
        final View moveView2 = LayoutInflater.from(this).inflate(R.layout.pop_move2, null, false);

        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopView.setChildShowView(moveView1);
                customPopView.show();
            }
        });

        findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopView.setChildShowView(moveView2);
                customPopView.show();
            }
        });

        moveView1.findViewById(R.id.btn_unbind).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了解绑银行卡", Toast.LENGTH_SHORT).show();
                customPopView.dismiss();
            }
        });

        moveView2.findViewById(R.id.btn_item1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了条目1", Toast.LENGTH_SHORT).show();
                customPopView.dismiss();
            }
        });
    }
}
