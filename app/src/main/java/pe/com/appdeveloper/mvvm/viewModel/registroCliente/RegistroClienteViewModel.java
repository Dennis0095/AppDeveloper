package pe.com.appdeveloper.mvvm.viewModel.registroCliente;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pe.com.appdeveloper.local.model.BaseResponse;
import pe.com.appdeveloper.local.model.Cliente;
import pe.com.appdeveloper.mvvm.repository.registroCliente.RegistroClienteRepository;

public class RegistroClienteViewModel extends ViewModel {
    private RegistroClienteRepository repository;
    private MutableLiveData<BaseResponse> listenerRegistro;

    public RegistroClienteViewModel(){
        repository = new RegistroClienteRepository();
    }

    public void registrarUsuario(Cliente cliente){
        repository.registrarUsuario(cliente);
        listenerRegistro = repository.getRegistrarUsuario();
    }

    public MutableLiveData<BaseResponse> getListenerRegistro(){
        return listenerRegistro;
    }

}
