package pe.com.appdeveloper.mvvm.repository.autenticacion;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import pe.com.appdeveloper.local.model.BaseResponse;
import pe.com.appdeveloper.mvvm.view.activity.autenticacion.LoginActivity;

public class AutenticacionClienteRepository {
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private Activity act;
    private static final String TAG = "PhoneAuthActivity";
    private static final String CODIGO = "+51";
    private final String MENSAJE_ERROR = "Ocurrió un error al enviar el código.";

    private MutableLiveData<BaseResponse> solicitarCodigo;
    private MutableLiveData<BaseResponse> verificarCodigo;

    public AutenticacionClienteRepository(FirebaseAuth mAuth_, Activity act_){
        mAuth = mAuth_;
        act = act_;
        callbackEnviarCodigo();
    }

    // PASO 1
    public void solicitarCodigo(String telefono){
        solicitarCodigo = new MutableLiveData<>();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(CODIGO+telefono)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(act)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void callbackEnviarCodigo(){
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // DMA cuando se hace una corroboracion de credencial automático
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                solicitarCodigo.postValue(new BaseResponse("0", MENSAJE_ERROR));
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // DMA cuando se envió el codigo
                Log.d(TAG, "onCodeSent:" + verificationId);
                // Save verification ID and resending token so we can use them later
                //mVerificationId = verificationId;
                solicitarCodigo.postValue(new BaseResponse("1", "Código enviado", verificationId));
                //mResendToken = token;
            }
        };
        // [END phone_auth_callbacks]
    }

    public MutableLiveData<BaseResponse> getSolicitarCodigo(){
        return solicitarCodigo;
    }

    // PASO 2
    public void verificarCodigo(String code, String verificationID) {
        verificarCodigo = new MutableLiveData<>();
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        //PhoneAuthCredential credential = PhoneAuthProvider.getCredential("+51935190762", "123456");
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }

    // [START sign_in_with_phone] DMA PASO 2 valicar credencial
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        if(verificarCodigo != null){
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                //FirebaseUser user = task.getResult().getUser();
                                verificarCodigo.postValue(new BaseResponse("1", "Código verificado"));
                            } else {
                                // Sign in failed, display a message and update the UI
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                verificarCodigo.postValue(new BaseResponse("0", "Código inválido"));
                            }
                        }
                    });
        }
    }

    public MutableLiveData<BaseResponse> getVerificarCodigo(){
        return verificarCodigo;
    }
    // [END sign_in_with_phone]
}
