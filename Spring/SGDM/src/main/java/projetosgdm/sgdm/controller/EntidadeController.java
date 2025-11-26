package projetosgdm.sgdm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projetosgdm.sgdm.dto.CadastroEntidadeDTO;
import projetosgdm.sgdm.repository.EntidadeRepository;
import projetosgdm.sgdm.repository.UsuarioRepository;
import projetosgdm.sgdm.model.Entidade;
import projetosgdm.sgdm.model.Usuario;

import java.time.OffsetDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/entidades")
public class EntidadeController {
    private final EntidadeRepository entidadeRepository;
    private final UsuarioRepository usuarioRepository;

    public EntidadeController(
            EntidadeRepository entidadeRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.entidadeRepository = entidadeRepository;
        this.usuarioRepository = usuarioRepository;
    }
    @GetMapping("/usuario-tem-entidade/{usuarioId}")
    public ResponseEntity<Boolean> usuarioTemEntidade(@PathVariable Long usuarioId) {
        boolean temEntidade = entidadeRepository.usuarioTemEntidade(usuarioId);
        System.out.println("🔍 Verificando se usuário " + usuarioId + " tem entidade: " + temEntidade);
        return ResponseEntity.ok(temEntidade);
    }
    @GetMapping("/usuario-eh-rt/{usuarioId}")
    public ResponseEntity<Boolean> usuarioEhRT(@PathVariable Long usuarioId) {
        boolean ehRT = entidadeRepository.usuarioEhRT(usuarioId);
        System.out.println("🔍 Verificando se usuário " + usuarioId + " é RT: " + ehRT);
        return ResponseEntity.ok(ehRT);
    }
    @GetMapping("/entidades-rt/{usuarioId}")
    public ResponseEntity<List<Entidade>> listarEntidadesDoRT(@PathVariable Long usuarioId) {
        List<Entidade> entidades = entidadeRepository.findEntidadesByRT(usuarioId);
        System.out.println("📋 Entidades onde usuário " + usuarioId + " é RT: " + entidades.size());
        return ResponseEntity.ok(entidades);
    }


    @PostMapping
    public ResponseEntity<Entidade> cadastrarEntidade(@RequestBody CadastroEntidadeDTO dto) {
        // Cria a entidade
        Entidade entidade = new Entidade();
        entidade.setRazaoSocial(dto.getRazaoSocial());
        entidade.setNomeFantasia(dto.getNomeFantasia());
        entidade.setCnpj(dto.getCnpj());
        entidade.setHorarioFuncionamento(dto.getHorarioFuncionamento());
        entidade.setAceitaValidadeCurta(dto.getAceitaValidadeCurta());
        entidade.setLatitude(dto.getLatitude());
        entidade.setLongitude(dto.getLongitude());
        entidade.setDataCadastro(OffsetDateTime.now());

        // Busca e associa o dono
        if (dto.getIdDonoEntidade() != null) {
            Usuario dono = usuarioRepository.findById(dto.getIdDonoEntidade().longValue())

                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.getIdDonoEntidade()));
            entidade.setIdDonoEntidade(dono);
        }

        // TODO: Processar farmacêutico (nomeFarmaceutico, cpfFarmaceutico, numCrf)
        // Isso você faz depois quando criar a tabela de farmacêuticos

        Entidade saved = entidadeRepository.save(entidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Entidade> listarEntidades() {
        return entidadeRepository.findAll();
    }
}
