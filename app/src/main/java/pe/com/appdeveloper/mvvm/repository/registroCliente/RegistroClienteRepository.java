package pe.com.appdeveloper.mvvm.repository.registroCliente;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pe.com.appdeveloper.local.model.BaseResponse;
import pe.com.appdeveloper.local.model.Cliente;

public class RegistroClienteRepository {

    private MutableLiveData<BaseResponse> deudaDetalle;
    private FirebaseDatabase db;
    private DatabaseReference myRef;

    public RegistroClienteRepository(){
        db = FirebaseDatabase.getInstance();
        deudaDetalle = new MutableLiveData<>();
        myRef = db.getReference("CLIENTES");
    }

    public void registrarUsuario(Cliente cliente){
        db = FirebaseDatabase.getInstance();
        DatabaseReference refCliente = myRef.push();
        refCliente.setValue(cliente);
        deudaDetalle.postValue(new BaseResponse("1", "¡Felicidades! El usuario ha sido registrado."));
    }

    public MutableLiveData<BaseResponse> getRegistrarUsuario(){
        return deudaDetalle;
    }
}
