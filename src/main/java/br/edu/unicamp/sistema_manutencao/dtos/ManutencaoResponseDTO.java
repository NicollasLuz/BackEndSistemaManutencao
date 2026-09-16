package br.edu.unicamp.sistema_manutencao.dtos;

import br.edu.unicamp.sistema_manutencao.entities.Manutencao;
import br.edu.unicamp.sistema_manutencao.enums.StatusManutencao;
import br.edu.unicamp.sistema_manutencao.enums.TipoManutencao;
import java.math.BigDecimal;

public record ManutencaoResponseDTO(
    Long id,
    String defeito,
    TipoManutencao tipo,
    StatusManutencao status,
    BigDecimal custo,
    String servicoExecutado,
    ItemResumidoDTO item
) {
    public static ManutencaoResponseDTO fromEntity(Manutencao m) {
        return new ManutencaoResponseDTO(
            m.getId(),
            m.getDefeito(),
            m.getTipo(),
            m.getStatus(),
            m.getCusto(),
            m.getServicoExecutado(),
            ItemResumidoDTO.fromEntity(m.getItem())
        );
    }
}