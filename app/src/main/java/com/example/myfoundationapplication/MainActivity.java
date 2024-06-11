package com.example.myfoundationapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.rw.Analyze;
import com.example.rw.CantileverRetainingWall;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edtWallTotalHeight = findViewById(R.id.wallTotalHeight);
        EditText edtToeSoilHeight = findViewById(R.id.toeSoilHeight);
        EditText edtPlateSlopeHeight = findViewById(R.id.plateSlopeHeight);
        EditText edtPlateEndpointHeight = findViewById(R.id.plateEndpointHeight);
        EditText edtWallTopBreath = findViewById(R.id.wallTopBreath);
        EditText edtToeBreadth = findViewById(R.id.toeBreadth);
        EditText edtBottomBreadth = findViewById(R.id.bottomBreadth);
        EditText edtHeelBreath = findViewById(R.id.heelBreath);
        EditText edtWallFrontSlope = findViewById(R.id.wallFrontSlope);
        EditText edtCb = findViewById(R.id.cb);
        EditText edtFb = findViewById(R.id.fb);
        EditText edtWb = findViewById(R.id.wb);
        EditText edtSurfaceSlope = findViewById(R.id.surfaceSlope);
        EditText edtWrc = findViewById(R.id.wrc);

        Button btnStartCheck = (Button) findViewById(R.id.startCheckButton);

        btnStartCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CheckActivity.class);
                double tb = Double.parseDouble(edtToeBreadth.getText().toString());
                double hb = Double.parseDouble(edtHeelBreath.getText().toString());
                double bb = Double.parseDouble(edtBottomBreadth.getText().toString());

                CantileverRetainingWall crw = new CantileverRetainingWall(
                    Double.parseDouble(edtWallTotalHeight.getText().toString()),
                    Double.parseDouble(edtToeSoilHeight.getText().toString()),
                    Double.parseDouble(edtPlateSlopeHeight.getText().toString()),
                    Double.parseDouble(edtPlateEndpointHeight.getText().toString()),
                    Double.parseDouble(edtWallTopBreath.getText().toString()),
                    tb,
                    hb,
                    tb+hb+bb,
                    Double.parseDouble(edtWallFrontSlope.getText().toString()),
                    Double.parseDouble(edtCb.getText().toString()),
                    Double.parseDouble(edtFb.getText().toString()),
                    Double.parseDouble(edtWb.getText().toString()),
                    Double.parseDouble(edtSurfaceSlope.getText().toString())
                );
                Analyze analyze = new Analyze();
                String tmp = "";
                double fsOver = analyze.overTurnCheck(crw);
                tmp = (fsOver > 2)?">":"<";
                String resFsOver = String.valueOf(fsOver) + tmp + "2";
                double fsSlide = analyze.slideCheck(crw);
                tmp = (fsSlide > 1.5)?">":"<";
                String resFsSlide = String.valueOf(fsSlide) + tmp + "1.5";
                double fsBreak = analyze.breakCheck(crw);
                tmp = (fsBreak > 3.)?">":"<";
                String resFsBreak = String.valueOf(fsBreak) + tmp + "3.0";

                intent.putExtra("fsOver",resFsOver);
                intent.putExtra("fsSlide",resFsSlide);
                intent.putExtra("fsBreak",resFsBreak);

                startActivity(intent);
            }
        });

        Button btnClear = (Button) findViewById(R.id.clearButton);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("清除欄位!");
                edtWallTotalHeight.setText("");
                edtToeSoilHeight.setText("");
                edtPlateSlopeHeight.setText("");
                edtPlateEndpointHeight.setText("");
                edtWallTopBreath.setText("");
                edtToeBreadth.setText("");
                edtBottomBreadth.setText("");
                edtHeelBreath.setText("");
                edtWallFrontSlope.setText("");
                edtCb.setText("");
                edtFb.setText("");
                edtWb.setText("");
                edtSurfaceSlope.setText("");
                edtWrc.setText("");
            }
        });
    }
}