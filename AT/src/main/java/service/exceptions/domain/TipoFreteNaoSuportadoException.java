package service.exceptions.domain;

public class TipoFreteNaoSuportadoException extends DomainException {
  public TipoFreteNaoSuportadoException(String tipo) {
    super("Tipo de frete não suportado: " + tipo);
  }
}
