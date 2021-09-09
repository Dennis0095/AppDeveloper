package pe.com.appdeveloper.local.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UtilDisenio {
    private Context ctx;

    public UtilDisenio(Context ctx_) {
        ctx = ctx_;
    }
    public UtilDisenio(){
    }
    public void colorProgressbar(ProgressBar progressBar, int color){
        progressBar.getIndeterminateDrawable().setColorFilter(ctx.getResources().getColor(color), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    // Siempre la cantidad debe ser un número par
    public void animacion(View v, int cantidad, int distancia){
        if(cantidad!=0){
            if(cantidad % 2 == 0) {
                v.animate().translationX(-distancia).setDuration(100);
                retrasarAnimacion(v, cantidad-1, distancia);
            } else {
                v.animate().translationX(distancia).setDuration(100);
                retrasarAnimacion(v, cantidad-1, distancia);
            }
        }
    }

    private void retrasarAnimacion(View v, int cantidad, int distancia){
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                animacion(v, cantidad, distancia);
            }
        }, 100);
    }
}
