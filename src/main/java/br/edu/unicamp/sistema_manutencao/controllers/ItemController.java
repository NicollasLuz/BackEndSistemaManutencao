package br.edu.unicamp.sistema_manutencao.controllers;

import br.edu.unicamp.sistema_manutencao.entities.Item;
import br.edu.unicamp.sistema_manutencao.repositories.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/itens")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ResponseEntity<List<Item>> listarTodos() {
        List<Item> itens = itemRepository.findAll();
        return ResponseEntity.ok(itens);
    }

    @PostMapping
    public ResponseEntity<Item> cadastrar(@RequestBody Item item) {
        Item novoItem = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> editarItem(@PathVariable Long id, @RequestBody Item dadosAtualizados) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Item itemExistente = itemOptional.get();
        
        // Atualizando apenas os dados permitidos
        itemExistente.setNome(dadosAtualizados.getNome());
        itemExistente.setPatrimonio(dadosAtualizados.getPatrimonio());
        itemExistente.setNomeLaboratorio(dadosAtualizados.getNomeLaboratorio());
        itemExistente.setFotoUrl(dadosAtualizados.getFotoUrl());
        itemExistente.setCor(dadosAtualizados.getCor());

        // O statusAtual e historicoManutencoes permanecem intocados
        
        return ResponseEntity.ok(itemRepository.save(itemExistente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirItem(@PathVariable Long id) {
        Optional<Item> itemOptional = itemRepository.findById(id);

        if (itemOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        itemRepository.delete(itemOptional.get());
        return ResponseEntity.noContent().build();
    }

}
