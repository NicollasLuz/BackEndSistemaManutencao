package br.edu.unicamp.sistema_manutencao.controllers;

import br.edu.unicamp.sistema_manutencao.dtos.AuthenticationDTO;
import br.edu.unicamp.sistema_manutencao.dtos.LoginResponseDTO;
import br.edu.unicamp.sistema_manutencao.dtos.RegisterDTO;
import br.edu.unicamp.sistema_manutencao.entities.Usuario;
import br.edu.unicamp.sistema_manutencao.enums.StatusUsuario;
import br.edu.unicamp.sistema_manutencao.enums.UserRole;
import br.edu.unicamp.sistema_manutencao.repositories.UsuarioRepository;
import br.edu.unicamp.sistema_manutencao.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));

        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Sua conta está aguardando aprovação de um Administrador.");
        } catch (Exception e) {
            e.printStackTrace(); // <-- Imprime o erro real no terminal do VS Code!
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("E-mail ou senha inválidos: " + e.getMessage());
        }
    }   

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO data) {
        // Valida se o e-mail já existe no banco
        if (this.usuarioRepository.findByEmail(data.email()).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado!");
        }

        // Criptografa a senha antes de salvar no banco
        String senhaCriptografada = new BCryptPasswordEncoder().encode(data.senha());
        
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNomeCompleto(data.nomeCompleto());
        novoUsuario.setEmail(data.email());
        novoUsuario.setSenhaHash(senhaCriptografada);
        novoUsuario.setRole(UserRole.USER); // Nasce perfil USER
        novoUsuario.setStatus(StatusUsuario.PENDENTE); // Nasce PENDENTE de aprovação
        novoUsuario.setProviderLogin("local");

        this.usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok("Usuário cadastrado com sucesso! Aguarde a aprovação de um Administrador.");
    }
}