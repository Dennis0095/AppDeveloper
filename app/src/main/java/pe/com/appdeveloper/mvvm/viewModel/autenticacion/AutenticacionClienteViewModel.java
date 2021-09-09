package pe.com.appdeveloper.mvvm.viewModel.autenticacion;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import pe.com.appdeveloper.local.model.BaseResponse;
import pe.com.appdeveloper.mvvm.repository.autenticacion.AutenticacionClienteRepository;

public class AutenticacionClienteViewModel extends ViewModel {
    private MutableLiveData<BaseResponse> solicitarCodigo;
    private MutableLiveData<BaseResponse> verificarCodigo;

    private AutenticacionClienteRepository repository;

    public AutenticacionClienteViewModel(){}

    public void init(FirebaseAuth mAuth_, Activity act_){
        repository = new AutenticacionClienteRepository(mAuth_, act_);
    }

    public void solicitarCodigo(String telefono){
        repository.solicitarCodigo(telefono);
        solicitarCodigo = repository.getSolicitarCodigo();
    }

    public MutableLiveData<BaseResponse> getSolicitarCodigo(){
        return solicitarCodigo;
    }

    public void verificarCodigo(String code, String verificationID){
        repository.verificarCodigo(code, verificationID);
        verificarCodigo = repository.getVerificarCodigo();
    }

    public MutableLiveData<BaseResponse> getVerificarCodigo(){
        return verificarCodigo;
    }
}
