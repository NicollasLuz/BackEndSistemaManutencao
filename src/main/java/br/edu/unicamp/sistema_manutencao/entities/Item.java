package br.edu.unicamp.sistema_manutencao.entities;

import br.edu.unicamp.sistema_manutencao.enums.StatusItem;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itens")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String fotoUrl;
    
    private String nomeLaboratorio;
    
    @Column(unique = true, nullable = false)
    private String patrimonio;
    
    private String nome;
    private String cor;
    
    @Enumerated(EnumType.STRING) // Salva como Texto no banco, não como Número
    private StatusItem statusAtual;
    
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Manutencao> historicoManutencoes = new ArrayList<>();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String getNomeLaboratorio() { return nomeLaboratorio; }
    public void setNomeLaboratorio(String nomeLaboratorio) { this.nomeLaboratorio = nomeLaboratorio; }

    public String getPatrimonio() { return patrimonio; }
    public void setPatrimonio(String patrimonio) { this.patrimonio = patrimonio; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public StatusItem getStatusAtual() { return statusAtual; }
    public void setStatusAtual(StatusItem statusAtual) { this.statusAtual = statusAtual; }
    
    public List<Manutencao> getHistoricoManutencoes() { return historicoManutencoes; }
    public void setHistoricoManutencoes(List<Manutencao> historicoManutencoes) { this.historicoManutencoes = historicoManutencoes; }
}