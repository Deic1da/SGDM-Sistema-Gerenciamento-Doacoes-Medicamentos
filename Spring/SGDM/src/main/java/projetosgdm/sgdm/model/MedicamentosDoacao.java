package projetosgdm.sgdm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "medicamentos_doacao")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MedicamentosDoacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_doador")
    private Usuario idDoador;

    @Column(name = "nome_medicamento", nullable = false)
    private String nomeMedicamento;

    @Column(name = "forma_farmaceutica", nullable = false, length = 100)
    private String formaFarmaceutica;

    @Column(name = "condicao_embalagem", nullable = false, length = 100)
    private String condicaoEmbalagem;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'Cadastrado'")
    @Column(name = "status_doacao", nullable = false)
    private StatusDoacao statusDoacao;

    @ColumnDefault("now()")
    @Column(name = "data_cadastro", nullable = false)
    private OffsetDateTime dataCadastro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade_destino")
    private Entidade idEntidadeDestino;

    public enum StatusDoacao {
        Cadastrado, Em_validacao, Aprovado, Rejeitado, Descartado
    }
}
