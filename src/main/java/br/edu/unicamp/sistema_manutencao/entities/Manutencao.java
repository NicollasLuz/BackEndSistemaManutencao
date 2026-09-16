package br.edu.unicamp.sistema_manutencao.entities;

import br.edu.unicamp.sistema_manutencao.enums.StatusManutencao;
import br.edu.unicamp.sistema_manutencao.enums.TipoManutencao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "manutencoes")
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "item_id") 
    @JsonIgnore
    private Item item;
    
    @Enumerated(EnumType.STRING)
    private TipoManutencao tipo;
    
    private String defeito;
    private LocalDate dataDefeito;
    private LocalDate dataInicio;
    private LocalDate prazo;
    private LocalDate dataConclusao;
    private String pecasTrocadas;
    private String servicoExecutado;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario responsavel;
    
    @Enumerated(EnumType.STRING)
    private StatusManutencao status;

    private BigDecimal custo;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public TipoManutencao getTipo() { return tipo; }
    public void setTipo(TipoManutencao tipo) { this.tipo = tipo; }

    public String getDefeito() { return defeito; }
    public void setDefeito(String defeito) { this.defeito = defeito; }

    public LocalDate getDataDefeito() { return dataDefeito; }
    public void setDataDefeito(LocalDate dataDefeito) { this.dataDefeito = dataDefeito; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getPrazo() { return prazo; }
    public void setPrazo(LocalDate prazo) { this.prazo = prazo; }

    public LocalDate getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDate dataConclusao) { this.dataConclusao = dataConclusao; }

    public String getPecasTrocadas() { return pecasTrocadas; }
    public void setPecasTrocadas(String pecasTrocadas) { this.pecasTrocadas = pecasTrocadas; }

    public String getServicoExecutado() { return servicoExecutado; }
    public void setServicoExecutado(String servicoExecutado) { this.servicoExecutado = servicoExecutado; }

    public Usuario getResponsavel() { return responsavel; }
    public void setResponsavel(Usuario responsavel) { this.responsavel = responsavel; }

    public StatusManutencao getStatus() { return status; }
    public void setStatus(StatusManutencao status) { this.status = status; }

    public BigDecimal getCusto() { return custo; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }
}