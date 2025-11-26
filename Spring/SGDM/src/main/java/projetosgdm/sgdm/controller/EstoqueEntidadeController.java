package projetosgdm.sgdm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetosgdm.sgdm.repository.EstoqueEntidadeRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estoque-entidade")
@CrossOrigin(origins = "http://localhost:4200")
public class EstoqueEntidadeController {

    @Autowired
    private EstoqueEntidadeRepository estoqueEntidadeRepository;

    @GetMapping("/entidade/{entidadeId}")
    public ResponseEntity<List<Map<String, Object>>> listarMedicamentosPorEntidade(
            @PathVariable Integer entidadeId) {

        System.out.println("🔍 Buscando medicamentos da entidade ID: " + entidadeId);

        List<Map<String, Object>> medicamentos =
                estoqueEntidadeRepository.findMedicamentosByEntidadeId(entidadeId);

        System.out.println("📦 Encontrados " + medicamentos.size() + " medicamentos");

        return ResponseEntity.ok(medicamentos);
    }
}
