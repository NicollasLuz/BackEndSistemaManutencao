package br.edu.unicamp.sistema_manutencao.dtos;

import br.edu.unicamp.sistema_manutencao.entities.Item;

public record ItemResumidoDTO(Long id, String nome, String patrimonio, String fotoUrl) {
    public static ItemResumidoDTO fromEntity(Item item) {
        if (item == null) return null;
        return new ItemResumidoDTO(item.getId(), item.getNome(), item.getPatrimonio(), item.getFotoUrl());
    }
}