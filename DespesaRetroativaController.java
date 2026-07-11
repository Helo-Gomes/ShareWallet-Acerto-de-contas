// DespesaRetroativaController.java
package com.bradesco.sharewallet.Controller;


import com.bradesco.sharewallet.DTO.DespesaRetroativaResponse;
import com.bradesco.sharewallet.DTO.RegistrarDespesaRetroativaRequest;
import com.bradesco.sharewallet.Service.DespesaRetroativaService;
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
            @RequestBody @Valid RegistrarDespesaRetroativaRequest request) {
        DespesaRetroativaResponse response = despesaRetroativaService.registrar(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}