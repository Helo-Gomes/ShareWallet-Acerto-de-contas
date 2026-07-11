package com.bradesco.sharewallet.DTO;

import com.bradesco.sharewallet.Entities.DespesaRetroativa;
import com.bradesco.sharewallet.Enums.StatusDespesaRetroativa;

import java.math.BigDecimal;

public record DespesaRetroativaResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        StatusDespesaRetroativa statusDespesa,
        Integer votosNecessarios,
        Integer votosRecebidos
) {
    public static DespesaRetroativaResponse fromEntity(DespesaRetroativa d, int votosRecebidos) {
        return new DespesaRetroativaResponse(
                d.getId(), d.getDescricao(), d.getValor(),
                d.getStatusDespesa(), d.getVotosNecessarios(), votosRecebidos
        );
    }
}
