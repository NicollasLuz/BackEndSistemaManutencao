package br.edu.unicamp.sistema_manutencao.controllers;

import br.edu.unicamp.sistema_manutencao.entities.Usuario;
import br.edu.unicamp.sistema_manutencao.enums.StatusUsuario;
import br.edu.unicamp.sistema_manutencao.repositories.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Listar usuários pendentes de aprovação
    @GetMapping("/pendentes")
    public ResponseEntity<List<Usuario>> listarPendentes() {
        List<Usuario> pendentes = usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getStatus() == StatusUsuario.PENDENTE)
                .toList();
        return ResponseEntity.ok(pendentes);
    }

    // Aprovar usuário
    @PutMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovarUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setStatus(StatusUsuario.ATIVO);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok("Usuário " + usuario.getEmail() + " aprovado!");
        }).orElse(ResponseEntity.notFound().build());
    }

    // Bloquear/Recusar usuário
    @PutMapping("/{id}/bloquear")
    public ResponseEntity<?> bloquearUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setStatus(StatusUsuario.BLOQUEADO);
            usuarioRepository.save(usuario);
            return ResponseEntity.ok("Usuário " + usuario.getEmail() + " bloqueado!");
        }).orElse(ResponseEntity.notFound().build());
    }
}