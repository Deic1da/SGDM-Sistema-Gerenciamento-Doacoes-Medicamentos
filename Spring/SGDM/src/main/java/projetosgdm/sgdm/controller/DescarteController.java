package projetosgdm.sgdm.controller;

import org.springframework.web.bind.annotation.*;
import projetosgdm.sgdm.repository.DescarteRepository;
import projetosgdm.sgdm.model.Descarte;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/Descarte")
public class DescarteController {
    private final DescarteRepository DescarteRepository;

    public DescarteController(DescarteRepository DescarteRepository) {
        this.DescarteRepository = DescarteRepository;
    }

    @PostMapping
    public Descarte cadastrarEntidade(@RequestBody Descarte Descarte) {
        return DescarteRepository.save(Descarte);
    }

    @GetMapping
    public List<Descarte> listarDescarte() {
        return DescarteRepository.findAll();
    }
}

