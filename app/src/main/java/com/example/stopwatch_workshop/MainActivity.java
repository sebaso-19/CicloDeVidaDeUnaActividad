package com.example.stopwatch_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Chronometer chronometer;
    boolean running = false;
    long pause;
    int lapCount = 0;
    TextView lapCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chronometer = findViewById(R.id.chronometer);
        lapCountTextView = findViewById(R.id.lapCountTextView);
    }

    public void OnClickStart(View view) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pause);
            chronometer.start();
            running = true;

            // Inicia el controlador para verificar las vueltas cada 5 segundos
            handler.postDelayed(updateLapsRunnable, 3000);
        }
    }

    public void OnClickPause(View view) {
        if (running) {
            chronometer.stop();
            pause = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;

            // Detén el controlador cuando se pausa
            handler.removeCallbacks(updateLapsRunnable);

            // Verifica si se han completado las 5 vueltas al pausar
            checkLapCompletion();
        }
    }

    public void OnClickReset(View view) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pause = 0;
        lapCount = 0;
        lapCountTextView.setText("Vueltas: 0");
    }


    private final Handler handler = new Handler();
    private final Runnable updateLapsRunnable = new Runnable() {
        @Override
        public void run() {
            lapCount++;
            lapCountTextView.setText("Vueltas: " + lapCount);
            handler.postDelayed(this, 3000);
            checkLapCompletion();
        }
    };

    private void checkLapCompletion() {
        if (lapCount >= 5) {
            chronometer.stop();
            running = false;
            handler.removeCallbacksAndMessages(null);
        }
    }
}