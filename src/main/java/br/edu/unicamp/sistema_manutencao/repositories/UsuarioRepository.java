package br.edu.unicamp.sistema_manutencao.repositories;

import br.edu.unicamp.sistema_manutencao.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}