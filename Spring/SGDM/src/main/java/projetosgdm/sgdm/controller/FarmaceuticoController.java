package projetosgdm.sgdm.controller;

import org.springframework.web.bind.annotation.*;
import projetosgdm.sgdm.repository.FarmaceuticoRepository;
import projetosgdm.sgdm.model.Farmaceutico;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/Farmaceutico")
public class FarmaceuticoController {
    private final FarmaceuticoRepository FarmaceuticoRepository;

    public FarmaceuticoController(FarmaceuticoRepository FarmaceuticoRepository) {
        this.FarmaceuticoRepository = FarmaceuticoRepository;
    }

    @PostMapping
    public Farmaceutico cadastrarFarmaceutico(@RequestBody Farmaceutico Farmaceutico) {
        return FarmaceuticoRepository.save(Farmaceutico);
    }

    @GetMapping
    public List<Farmaceutico> listarFarmaceutico() {
        return FarmaceuticoRepository.findAll();
    }
}

