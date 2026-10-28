package br.edu.unicamp.sistema_manutencao.repositories;

import br.edu.unicamp.sistema_manutencao.entities.Manutencao;
import br.edu.unicamp.sistema_manutencao.enums.StatusManutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {
    List<Manutencao> findByItemId(Long itemId);
    List<Manutencao> findByStatus(StatusManutencao status);
    List<Manutencao> findByStatusNot(StatusManutencao status); // Útil para buscar manutenções não concluídas
}