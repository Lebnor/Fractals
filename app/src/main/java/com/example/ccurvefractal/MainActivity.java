package com.example.ccurvefractal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private Resources resources;
    private Button buttonIncrease, buttonReduce;
    private ImageView imageView;
    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;

    private TextView textView;
    private Drawer drawer;

    private Point a, b, c;
    final float Y1 = 225f;
    final float X1 = 100f;
    final float X2 = 300f;
    final float Y2 = 200f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Sierpiński triangle");
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        resources = getResources();

        drawer = new Drawer(Drawer.Fractal.TRIANGLE);

        buttonIncrease = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        bitmap = Bitmap.createBitmap(400, 800, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        paint.setShader(new LinearGradient(50, 50, 125, 50, Color.rgb(138, 43, 226), Color.MAGENTA, Shader.TileMode.MIRROR));
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        buttonReduce = findViewById(R.id.buttonReduce);
        buttonReduce.setEnabled(false);
        textView = findViewById(R.id.textView2);

        a = new Point(0, 400);
        b = new Point(400, 400);
        c = new Point(200, 175);

        buttonIncrease.setOnClickListener(view -> {
            clearCanvas();
            drawer.increaseOrder();
            int currentOrder = drawer.getOrder();
            draw(currentOrder);
            checkBoundary();
        });

        buttonReduce.setOnClickListener(view -> {
            clearCanvas();
            drawer.reduceOrder();
            draw(drawer.getOrder());
            checkBoundary();
        });
    }

    private void checkBoundary() {
        int currentOrder = drawer.getOrder();
        if (currentOrder <= 1) {
            buttonReduce.setEnabled(false);
        } else if (currentOrder >= drawer.getMaxOrder()) {
            drawer.setOrder(drawer.getMaxOrder());
            buttonIncrease.setEnabled(false);
        } else {
            buttonReduce.setEnabled(true);
            buttonIncrease.setEnabled(true);
        }
        textView.setText("Order: " + drawer.getOrder());
    }

    private void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void clearCanvas() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // clear the canvas
    }

    private void draw(int order) {
        if (order < 1)
            return;
        if (drawer.getFractal() == Drawer.Fractal.CCURVE) {
            drawCCurve(order, X1, Y1, X2, Y1);
        } else if (drawer.getFractal() == Drawer.Fractal.TRIANGLE) {
            drawTriangle(order, a, b, c);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Ccurve:
                clearCanvas();
                drawer.setFractal(Drawer.Fractal.CCURVE);
                draw(drawer.getOrder());
                setTitle("C curve");
                checkBoundary();
                break;

            default:
                clearCanvas();
                drawer.setFractal(Drawer.Fractal.TRIANGLE);
                draw(drawer.getOrder());
                setTitle("Sierpiński triangle");
                checkBoundary();
                break;

        }
        return true;
    }

    private void drawTriangle(int order, Point a, Point b, Point c) {
        if (order == 1) {
            Path path = new Path();
            path.setFillType(Path.FillType.WINDING);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.close();
            canvas.drawPath(path, paint);
            imageView.setImageBitmap(bitmap);
        } else {

            Point halfAB = new Point((a.x + b.x) / 2, a.y);
            Point halfAC = new Point((a.x + c.x) / 2, (a.y + c.y) / 2);
            Point halfBC = new Point((b.x + c.x) / 2, (b.y + c.y) / 2);

            drawTriangle(order - 1, a, halfAB, halfAC);
            drawTriangle(order - 1, halfAB, b, halfBC);
            drawTriangle(order - 1, halfAC, halfBC, c);

        }
    }

    private void drawCCurve(float order, float x1, float y1, float x3, float y3) {

        if (order == 1) {
            canvas.drawLine(x1, y1, x3, y3, paint);
            imageView.setImageBitmap(bitmap);
        } else {

            float x2 = (x1 + x3 + y1 - y3) / 2;
            float y2 = (x3 + y1 + y3 - x1) / 2;

            drawCCurve(order - 1, x1, y1, x2, y2);
            drawCCurve(order - 1, x2, y2, x3, y3);

        }
    }


}