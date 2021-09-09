package pe.com.appdeveloper.local.interfaces;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

public interface IValidacionesGenerales {
    Boolean validarCampoVacio(EditText editTex);
    Boolean validarCampoValido(EditText editTex, int cant);
}
