package pe.com.appdeveloper.mvvm.view.activity.autenticacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import pe.com.appdeveloper.R;
import pe.com.appdeveloper.databinding.ActivityLoginBinding;
import pe.com.appdeveloper.local.interfaces.ILoad;
import pe.com.appdeveloper.local.model.BaseResponse;
import pe.com.appdeveloper.local.util.UtilDisenio;
import pe.com.appdeveloper.mvvm.view.activity.cliente.CreacionClienteActivity;
import pe.com.appdeveloper.mvvm.viewModel.autenticacion.AutenticacionClienteViewModel;
import pe.com.appdeveloper.mvvm.viewModel.registroCliente.RegistroClienteViewModel;

public class LoginActivity extends AppCompatActivity implements ILoad {

    private ActivityLoginBinding binding;
    private UtilDisenio utilDisenio;
    private Boolean solicitarCodigo=false;
    private Boolean solicitarIngreso=false;
    private ValidacionesLogin validacionesLogin;
    private final int DISTANCIA_A_MOVER = 20;
    private final int CANTIDAD = 8;
    private AutenticacionClienteViewModel autenticacionClienteViewModel;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private String mVerificationId;
    //private PhoneAuthProvider.ForceResendingToken mResendToken;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view_ = binding.getRoot();
        setContentView(view_);
        iniciarElementos();
    }

    @Override
    public void iniciarElementos() {
        validacionesLogin = new ValidacionesLogin();
        utilDisenio = new UtilDisenio(this);
        utilDisenio.colorProgressbar(binding.pbCargando, R.color.white);
        validacionesLogin.listenerEditText(binding.etTelefono, binding.tvMensaje);
        validacionesLogin.listenerEditText(binding.etCodigo, binding.tvMensaje2);
        mAuth = FirebaseAuth.getInstance();
        autenticacionClienteViewModel = ViewModelProviders.of(this).get(AutenticacionClienteViewModel.class);
        autenticacionClienteViewModel.init(mAuth, LoginActivity.this);
    }

    public void iniciarSesion(View v){

        if(mVerificationId == null){
            if(!validacionesLogin.validarCampoVacio(binding.etTelefono)) {
                binding.tvMensaje.setVisibility(View.VISIBLE);
                utilDisenio.animacion(binding.tvMensaje, CANTIDAD, DISTANCIA_A_MOVER);
                binding.tvMensaje.setText(getString(R.string.telefonoRequerido));return;
            }

            if(!validacionesLogin.validarCampoValido(binding.etTelefono, 9)) {
                binding.tvMensaje.setVisibility(View.VISIBLE);
                utilDisenio.animacion(binding.tvMensaje, CANTIDAD, DISTANCIA_A_MOVER);
                binding.tvMensaje.setText(getString(R.string.telefonoInvalido));return;
            }

            if(!solicitarCodigo){
                solicitarCodigo = true;
                binding.pbCargando.setVisibility(View.VISIBLE);
                solicitarCodigo(binding.etTelefono.getText().toString().trim());
            }
        }else{

            if(!validacionesLogin.validarCampoVacio(binding.etCodigo)) {
                binding.tvMensaje2.setVisibility(View.VISIBLE);
                utilDisenio.animacion(binding.tvMensaje2, CANTIDAD, DISTANCIA_A_MOVER);
                binding.tvMensaje2.setText(getString(R.string.codigoRequerido));return;
            }

            if(!validacionesLogin.validarCampoValido(binding.etCodigo, 4)) {
                binding.tvMensaje2.setVisibility(View.VISIBLE);
                utilDisenio.animacion(binding.tvMensaje2, CANTIDAD, DISTANCIA_A_MOVER);
                binding.tvMensaje2.setText(getString(R.string.codigoInvalido));return;
            }

            if(!solicitarIngreso){
                solicitarIngreso = true;
                binding.etCodigo.setEnabled(false);
                binding.pbCargando.setVisibility(View.VISIBLE);
                verificarCodigo(binding.etCodigo.getText().toString());
            }
        }
    }

    private void solicitarCodigo(String telefono){
        autenticacionClienteViewModel.solicitarCodigo(telefono);
        autenticacionClienteViewModel.getSolicitarCodigo().observe(this, new Observer<BaseResponse>() {
            @Override
            public void onChanged(BaseResponse baseResponse) {
                solicitarCodigo = false;
                binding.pbCargando.setVisibility(View.GONE);
                if(baseResponse.getEstado().equals("0")){
                    showTast(baseResponse.getMessage(), Toast.LENGTH_LONG);
                }else{
                    binding.clCodigo.setVisibility(View.VISIBLE);
                    binding.etTelefono.setEnabled(false);
                    mVerificationId = baseResponse.getIdSolicitudCodigo();
                }
            }
        });
    }

    private void verificarCodigo(String codigo){
        autenticacionClienteViewModel.verificarCodigo(codigo, mVerificationId);
        autenticacionClienteViewModel.getVerificarCodigo().observe(this, new Observer<BaseResponse>() {
            @Override
            public void onChanged(BaseResponse baseResponse) {
                binding.pbCargando.setVisibility(View.GONE);
                solicitarIngreso = false;
                binding.etCodigo.setEnabled(true);
                if(baseResponse.getEstado().equals("0")){
                    showTast(baseResponse.getMessage(), Toast.LENGTH_LONG);
                }else
                    iniciarActividad();
            }
        });
    }



    private void iniciarActividad(){
        Intent i = new Intent(this, CreacionClienteActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.animation, R.anim.animations2);
    }

    public void showTast(String mensaje, int duracion){
        Toast.makeText(LoginActivity.this, mensaje, duracion).show();
    }

}