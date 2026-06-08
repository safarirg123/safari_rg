package com.example.safariproject;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        randomSplash();

        //perms setup
        List<String> PermsLST = new ArrayList<>();
        for (String permission : new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                PermsLST.add(permission);
        int REQUEST_CODE = 123;
        if (!PermsLST.isEmpty())
            ActivityCompat.requestPermissions(this, PermsLST.toArray(new String[0]), REQUEST_CODE);
        else nextScreen();
    }

    public void randomSplash() {

        Field[] fields = R.drawable.class.getFields();
        int splashCount = 0;
        for (Field field : fields)
            if (field.getName().startsWith("splash_"))
                splashCount++;
        Field[] splashFields = new Field[splashCount];
        int index = 0;
        for (Field field : fields)
            if(field.getName().startsWith("splash_"))
            {
                splashFields[index] = field;
                index++;
            }

        Random rand = new Random();
        int splashIndex = rand.nextInt(splashFields.length);
        try {
            findViewById(R.id.main).setBackgroundResource(splashFields[splashIndex].getInt(null));
        }
        catch (IllegalAccessException e) {
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        nextScreen();
    }

    public void nextScreen() {
        handler.postDelayed(() -> {
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
            finish();
        }, 3000);
    }
}
