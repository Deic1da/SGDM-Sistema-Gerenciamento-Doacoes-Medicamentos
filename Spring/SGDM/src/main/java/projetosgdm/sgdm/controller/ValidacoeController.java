package projetosgdm.sgdm.controller;

import org.springframework.web.bind.annotation.*;
import projetosgdm.sgdm.repository.ValidacoeRepository;
import projetosgdm.sgdm.model.Validacoe;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/validacoe")
public class ValidacoeController {
    private final ValidacoeRepository ValidacoeRepository;

    public ValidacoeController(ValidacoeRepository ValidacoeRepository) {
        this.ValidacoeRepository = ValidacoeRepository;
    }

    @PostMapping
    public Validacoe cadastrarValidacoe(@RequestBody Validacoe Validacoe) {
        return ValidacoeRepository.save(Validacoe);
    }

    @GetMapping
    public List<Validacoe> listarValidacoe() {
        return ValidacoeRepository.findAll();
    }
}
