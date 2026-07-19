// DespesaRetroativaController.java
package com.bradesco.sharewallet.Controllers;

import com.bradesco.sharewallet.Services.DespesaRetroativaService;
import com.bradesco.sharewallet.dto.DespesaRetroativaResponse;
import com.bradesco.sharewallet.dto.RegistrarDespesaRetroativaRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DespesaRetroativaController {

    private final DespesaRetroativaService despesaRetroativaService;

    public DespesaRetroativaController(DespesaRetroativaService despesaRetroativaService) {
        this.despesaRetroativaService = despesaRetroativaService;
    }

    @PostMapping("/grupos/{id}/despesas/retroativas")
    public ResponseEntity<DespesaRetroativaResponse> registrar(
            @PathVariable Long id,
            @RequestParam Long criadorParticipanteId,
            @RequestBody @Valid RegistrarDespesaRetroativaRequest request) {

        DespesaRetroativaResponse response =
            despesaRetroativaService.registrar(id, criadorParticipanteId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
