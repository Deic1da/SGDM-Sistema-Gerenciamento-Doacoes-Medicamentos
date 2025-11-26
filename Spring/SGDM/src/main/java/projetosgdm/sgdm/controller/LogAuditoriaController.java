package projetosgdm.sgdm.controller;

import org.springframework.web.bind.annotation.*;
import projetosgdm.sgdm.repository.LogAuditoriaRepository;
import projetosgdm.sgdm.model.LogAuditoria;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/logauditoria")
public class LogAuditoriaController {
    private final LogAuditoriaRepository LogAuditoriaRepository;

    public LogAuditoriaController(LogAuditoriaRepository LogAuditoriaRepository) {
        this.LogAuditoriaRepository = LogAuditoriaRepository;
    }

    @PostMapping
    public LogAuditoria cadastrarLogAuditoria(@RequestBody LogAuditoria LogAuditoria) {
        return LogAuditoriaRepository.save(LogAuditoria);
    }

    @GetMapping
    public List<LogAuditoria> listarLogAuditoria() {
        return LogAuditoriaRepository.findAll();
    }
}
