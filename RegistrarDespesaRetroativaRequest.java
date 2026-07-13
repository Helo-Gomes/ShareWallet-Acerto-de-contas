package com.bradesco.sharewallet.DTO;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RegistrarDespesaRetroativaRequest(
        @NotBlank(message = "Descrição obrigatória")
        @Size(max = 150, message = "Descrição deve ter no máximo 150 caracteres")
        String descricao,

        @NotNull @Positive(message = "Valor deve ser maior que zero")
        BigDecimal valor,

        @NotEmpty @Size(min = 2, message = "Informe pelo menos 2 participantes")
        List<Long> participantesIds,

        @NotNull(message = "Meio de pagamento obrigatório")
        MeioPagamento meioPagamento,

        @NotNull(message = "Data/horário obrigatório")
        LocalDateTime dataHorario
) {}
