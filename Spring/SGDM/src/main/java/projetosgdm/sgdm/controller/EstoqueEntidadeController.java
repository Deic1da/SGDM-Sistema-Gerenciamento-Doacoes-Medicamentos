package projetosgdm.sgdm.controller;

import org.springframework.web.bind.annotation.*;
import projetosgdm.sgdm.repository.EstoqueEntidadeRepository;
import projetosgdm.sgdm.model.EstoqueEntidade;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/EstoqueEntidade")
public class EstoqueEntidadeController {
    private final EstoqueEntidadeRepository EstoqueEntidadeRepository;

    public EstoqueEntidadeController(EstoqueEntidadeRepository EstoqueEntidadeRepository) {
        this.EstoqueEntidadeRepository= EstoqueEntidadeRepository;
    }

    @PostMapping
    public EstoqueEntidade cadastrarEstoqueEntidade(@RequestBody EstoqueEntidade EstoqueEntidade) {
        return EstoqueEntidadeRepository.save( EstoqueEntidade);
    }

    @GetMapping
    public List<EstoqueEntidade> listarEstoqueEntidade() {
        return EstoqueEntidadeRepository.findAll();
    }
}



