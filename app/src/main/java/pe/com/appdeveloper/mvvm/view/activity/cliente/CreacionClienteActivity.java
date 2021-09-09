package pe.com.appdeveloper.mvvm.view.activity.cliente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import pe.com.appdeveloper.R;
import pe.com.appdeveloper.databinding.ActivityCreacionClienteBinding;
import pe.com.appdeveloper.databinding.ActivityLoginBinding;
import pe.com.appdeveloper.local.interfaces.ILoad;
import pe.com.appdeveloper.local.model.BaseResponse;
import pe.com.appdeveloper.local.model.Cliente;
import pe.com.appdeveloper.mvvm.viewModel.registroCliente.RegistroClienteViewModel;

public class CreacionClienteActivity extends AppCompatActivity implements ILoad {

    private static ActivityCreacionClienteBinding binding;
    private RegistroClienteViewModel registroClienteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreacionClienteBinding.inflate(getLayoutInflater());
        View view_ = binding.getRoot();
        setContentView(view_);
        iniciarElementos();
    }


    @Override
    public void iniciarElementos() {
        binding.tietFecha.setOnClickListener(v -> {
            DatePickerFragment newFrawment = new DatePickerFragment();
            newFrawment.show(getSupportFragmentManager(), "datePicker");
        });
        binding.tvRegistrar.setOnClickListener(v -> registrarCliente());

        registroClienteViewModel = ViewModelProviders.of(this).get(RegistroClienteViewModel.class);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getContext(),this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    binding.tietFecha.setText(year + "-" + (month+1) + "-" + day);
            }
        }

        private void registrarCliente(){
            String nombre = binding.tietNombre.getText().toString().trim();
            String apellido = binding.tietApellido.getText().toString().trim();
            String edad = binding.tietEdad.getText().toString().trim();
            String fecha = binding.tietFecha.getText().toString().trim();
            registroClienteViewModel.registrarUsuario(new Cliente(nombre, apellido, edad, fecha));
            registroClienteViewModel.getListenerRegistro().observe(this, new Observer<BaseResponse>() {
                @Override
                public void onChanged(BaseResponse baseResponse) {
                    if(baseResponse.getEstado().equals("1")){
                        Toast.makeText(CreacionClienteActivity.this, baseResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
}