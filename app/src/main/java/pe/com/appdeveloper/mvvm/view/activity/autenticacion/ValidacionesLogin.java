package pe.com.appdeveloper.mvvm.view.activity.autenticacion;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import pe.com.appdeveloper.R;
import pe.com.appdeveloper.local.interfaces.IValidacionesGenerales;

public class ValidacionesLogin implements IValidacionesGenerales {

    @Override
    public Boolean validarCampoVacio(EditText editText) {
        if(editText.getText().toString().length()==0)
            return false;
        else
            return true;
    }

    @Override
    public Boolean validarCampoValido(EditText editText, int cant) {
        if(editText.getText().toString().length()<cant)
            return false;
        else
            return true;
    }

    public void listenerEditText(EditText editText, TextView textView){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    textView.setVisibility(View.GONE);
                }
            }
        });
    }
}
