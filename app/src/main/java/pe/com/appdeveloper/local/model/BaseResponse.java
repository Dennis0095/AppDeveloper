package pe.com.appdeveloper.local.model;

public class BaseResponse {
    private String estado;
    private String message;
    private String idSolicitudCodigo;

    public BaseResponse(String estado_, String message_) {
        estado = estado_;
        message = message_;
    }

    public BaseResponse(String estado_, String message_, String idSolicitudCodigo_) {
        estado = estado_;
        message = message_;
        idSolicitudCodigo = idSolicitudCodigo_;
    }

    public String getEstado() {
        return estado;
    }

    public String getMessage() {
        return message;
    }

    public String getIdSolicitudCodigo(){
        return idSolicitudCodigo;
    }

}
