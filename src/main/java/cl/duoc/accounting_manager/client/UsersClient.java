package cl.duoc.accounting_manager.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import cl.duoc.accounting_manager.dto.response.users.UsuarioResponseDto;
import cl.duoc.accounting_manager.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UsersClient {

    private final WebClient webClientUsers;

    public UsuarioResponseDto findUserById(Long id) {
        return webClientUsers.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        response -> Mono.error(new ResourceNotFoundException("Cliente no existe con id: " + id)))
                .bodyToMono(UsuarioResponseDto.class)
                .block();
    }
}
