package br.edu.unicamp.sistema_manutencao.controllers;

import br.edu.unicamp.sistema_manutencao.dtos.ManutencaoResponseDTO;
import br.edu.unicamp.sistema_manutencao.entities.Item;
import br.edu.unicamp.sistema_manutencao.entities.Manutencao;
import br.edu.unicamp.sistema_manutencao.enums.StatusItem;
import br.edu.unicamp.sistema_manutencao.enums.StatusManutencao;
import br.edu.unicamp.sistema_manutencao.repositories.ItemRepository;
import br.edu.unicamp.sistema_manutencao.repositories.ManutencaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/manutencoes")
public class ManutencaoController {

    private final ManutencaoRepository manutencaoRepository;
    private final ItemRepository itemRepository;

    public ManutencaoController(ManutencaoRepository manutencaoRepository, ItemRepository itemRepository) {
        this.manutencaoRepository = manutencaoRepository;
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public List<ManutencaoResponseDTO> listarTodas() {
        return manutencaoRepository.findAll()
                .stream()
                .map(ManutencaoResponseDTO::fromEntity)
                .toList();
    }

    @PostMapping("/abrir/{itemId}")
    public ResponseEntity<Manutencao> abrirManutencao(@PathVariable Long itemId, @RequestBody Manutencao manutencao) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        
        if (itemOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Item item = itemOptional.get();
        
        // Associar o item à manutenção
        manutencao.setItem(item);
        
        // Atualizar os status conforme regra de negócio
        item.setStatusAtual(StatusItem.EM_MANUTENCAO);
        manutencao.setStatus(StatusManutencao.EM_ANDAMENTO);
        
        itemRepository.save(item);
        return ResponseEntity.ok(manutencaoRepository.save(manutencao));
    }

    @PutMapping("/concluir/{manutencaoId}")
    public ResponseEntity<Manutencao> concluirManutencao(@PathVariable Long manutencaoId, @RequestBody Manutencao dadosConclusao) {
        Optional<Manutencao> manutencaoOptional = manutencaoRepository.findById(manutencaoId);
        
        if (manutencaoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Manutencao manutencao = manutencaoOptional.get();
        
        // Atualizar os dados da manutenção com as informações recebidas
        manutencao.setDataConclusao(dadosConclusao.getDataConclusao());
        manutencao.setPecasTrocadas(dadosConclusao.getPecasTrocadas());
        manutencao.setServicoExecutado(dadosConclusao.getServicoExecutado());
        manutencao.setCusto(dadosConclusao.getCusto());
        
        manutencao.setStatus(StatusManutencao.CONCLUIDO);
        
        Item item = manutencao.getItem();
        item.setStatusAtual(StatusItem.DISPONIVEL);
        itemRepository.save(item);
        
        return ResponseEntity.ok(manutencaoRepository.save(manutencao));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Manutencao> alterarStatusManutencao(@PathVariable Long id, @RequestParam StatusManutencao novoStatus) {
        Optional<Manutencao> manutencaoOptional = manutencaoRepository.findById(id);

        if (manutencaoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Manutencao manutencao = manutencaoOptional.get();
        manutencao.setStatus(novoStatus);

        return ResponseEntity.ok(manutencaoRepository.save(manutencao));
    }
}