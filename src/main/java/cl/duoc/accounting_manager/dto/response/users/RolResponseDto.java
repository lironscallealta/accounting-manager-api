package cl.duoc.accounting_manager.dto.response.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolResponseDto {

    private Long id;
    private String nombreRol;
    private String descripcion;
}
