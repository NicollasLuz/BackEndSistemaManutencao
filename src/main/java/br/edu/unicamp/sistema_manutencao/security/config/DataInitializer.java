package br.edu.unicamp.sistema_manutencao.security.config;

import br.edu.unicamp.sistema_manutencao.entities.Usuario;
import br.edu.unicamp.sistema_manutencao.enums.StatusUsuario;
import br.edu.unicamp.sistema_manutencao.enums.UserRole;
import br.edu.unicamp.sistema_manutencao.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        String adminEmail = "admin@unicamp.br";
        Optional<Usuario> adminExistente = usuarioRepository.findByEmail(adminEmail);

        if (adminExistente.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNomeCompleto("Administrador Geral");
            admin.setEmail(adminEmail);
            admin.setSenhaHash(new BCryptPasswordEncoder().encode("admin123"));
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(StatusUsuario.ATIVO);
            admin.setProviderLogin("local");

            usuarioRepository.save(admin);
        } else {
            // Se já existia no banco, atualiza para garantir que está ATIVO e é ADMIN
            Usuario admin = adminExistente.get();
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(StatusUsuario.ATIVO);
            admin.setSenhaHash(new BCryptPasswordEncoder().encode("admin123"));
            usuarioRepository.save(admin);
        }
    }
}