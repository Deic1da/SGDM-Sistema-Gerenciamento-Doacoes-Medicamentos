package projetosgdm.sgdm.controller;

import org.springframework.web.bind.annotation.*;
import projetosgdm.sgdm.repository.VinculosFarmaceuticoRepository;
import projetosgdm.sgdm.model.VinculosFarmaceutico;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/VinculosFarmaceutico")
public class VinculosFarmaceuticoController {
    private final VinculosFarmaceuticoRepository VinculosFarmaceuticoRepository;

    public VinculosFarmaceuticoController(VinculosFarmaceuticoRepository VinculosFarmaceuticoRepository) {
        this.VinculosFarmaceuticoRepository = VinculosFarmaceuticoRepository;
    }

    @PostMapping
    public VinculosFarmaceutico cadastrarVinculosFarmaceutico(@RequestBody VinculosFarmaceutico VinculosFarmaceutico) {
        return VinculosFarmaceuticoRepository.save(VinculosFarmaceutico);
    }

    @GetMapping
    public List<VinculosFarmaceutico> listarVinculosFarmaceutico() {
        return VinculosFarmaceuticoRepository.findAll();
    }
}
