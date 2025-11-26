package projetosgdm.sgdm.controller;

import org.springframework.web.bind.annotation.*;
import projetosgdm.sgdm.repository.EntregasRepository;
import projetosgdm.sgdm.model.Entregas;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/Entregas")
public class EntregasController {
    private final EntregasRepository EntregasRepository;

    public EntregasController(EntregasRepository EntregasRepository) {
        this.EntregasRepository = EntregasRepository;
    }

    @PostMapping
    public Entregas cadastrarEntregas(@RequestBody Entregas Entregas) {
        return EntregasRepository.save(Entregas);
    }

    @GetMapping
    public List<Entregas> listarEntregas() {
        return EntregasRepository.findAll();
    }
}

