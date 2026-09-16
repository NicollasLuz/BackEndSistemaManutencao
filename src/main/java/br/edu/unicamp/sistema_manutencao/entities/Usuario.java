package br.edu.unicamp.sistema_manutencao.entities;

import br.edu.unicamp.sistema_manutencao.enums.StatusUsuario;
import br.edu.unicamp.sistema_manutencao.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nomeCompleto;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @JsonIgnore // Segurança/LGPD: Nunca envia a senha no JSON
    @Column(nullable = false)
    private String senhaHash;
    
    private Boolean emailVerificado = false;
    
    private String providerLogin; // Ex: "local" ou "google"
    
    @Column(updatable = false)
    private LocalDateTime criadoEm;

    // --- NOVOS CAMPOS DE SEGURANÇA E NEGÓCIO ---
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private StatusUsuario status = StatusUsuario.PENDENTE; // Todo usuário nasce pendente!
    
    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
    }
    
    // --- Getters e Setters Originais ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public Boolean getEmailVerificado() { return emailVerificado; }
    public void setEmailVerificado(Boolean emailVerificado) { this.emailVerificado = emailVerificado; }

    public String getProviderLogin() { return providerLogin; }
    public void setProviderLogin(String providerLogin) { this.providerLogin = providerLogin; }
    
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public StatusUsuario getStatus() { return status; }
    public void setStatus(StatusUsuario status) { this.status = status; }

    // ====================================================================
    // --- MÉTODOS OBRIGATÓRIOS DO SPRING SECURITY (Interface UserDetails) ---
    // ====================================================================

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Se for ADMIN, ele ganha poder de ADMIN e USER. Se for USER, só de USER.
        if(this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.senhaHash; // Avisamos ao Spring que a senha é o campo "senhaHash"
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.email; // Avisamos ao Spring que o login é feito pelo E-mail
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        // A TRAVA DE SEGURANÇA: Só deixa logar se for ATIVO
        return this.status == StatusUsuario.ATIVO; 
    }
}