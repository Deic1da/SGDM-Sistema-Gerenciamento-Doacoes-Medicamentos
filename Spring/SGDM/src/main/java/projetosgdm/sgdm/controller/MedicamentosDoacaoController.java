package projetosgdm.sgdm.controller;

import org.springframework.web.bind.annotation.*;
import projetosgdm.sgdm.repository.MedicamentosDoacaoRepository;
import projetosgdm.sgdm.model.MedicamentosDoacao;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/MedicamentosDoacao")
public class MedicamentosDoacaoController {
    private final MedicamentosDoacaoRepository MedicamentosDoacaoRepository;

    public MedicamentosDoacaoController(MedicamentosDoacaoRepository MedicamentosDoacaoRepository) {
        this.MedicamentosDoacaoRepository = MedicamentosDoacaoRepository;
    }

    @PostMapping
    public MedicamentosDoacao cadastrarMedicamentosDoacao(@RequestBody MedicamentosDoacao MedicamentosDoacao) {
        return MedicamentosDoacaoRepository.save(MedicamentosDoacao);
    }

    @GetMapping
    public List<MedicamentosDoacao> listarMedicamentosDoacaos() {
        return MedicamentosDoacaoRepository.findAll();
    }
}
