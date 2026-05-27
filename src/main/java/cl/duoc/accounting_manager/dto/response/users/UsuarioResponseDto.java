package cl.duoc.accounting_manager.dto.response.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDto {

    private Long id;
    private String nombreCompleto;
    private String rut;
    private String email;
    private int edad;
    private String telefonoCelular;
    private RolResponseDto rol;
    private boolean activo;
}
