package br.edu.unicamp.sistema_manutencao.repositories;

import br.edu.unicamp.sistema_manutencao.entities.Item;
import br.edu.unicamp.sistema_manutencao.enums.StatusItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByPatrimonio(String patrimonio);
    List<Item> findByStatusAtual(StatusItem statusAtual);
    List<Item> findByNomeContainingIgnoreCaseOrPatrimonioContainingIgnoreCase(String nome, String patrimonio);
}