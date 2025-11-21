package projetosgdm.sgdm.controller;

import org.springframework.web.bind.annotation.*;
import projetosgdm.sgdm.repository.EntidadeRepository;
import projetosgdm.sgdm.model.Entidade;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/entidades")
public class EntidadeController {
    private final EntidadeRepository entidadeRepository;

    public EntidadeController(EntidadeRepository entidadeRepository) {
        this.entidadeRepository = entidadeRepository;
    }

    @PostMapping
    public Entidade cadastrarEntidade(@RequestBody Entidade entidade) {
        return entidadeRepository.save(entidade);
    }

    @GetMapping
    public List<Entidade> listarEntidades() {
        return entidadeRepository.findAll();
    }
}
