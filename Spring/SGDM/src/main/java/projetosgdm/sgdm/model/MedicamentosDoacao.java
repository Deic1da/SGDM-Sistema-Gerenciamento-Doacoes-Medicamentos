package projetosgdm.sgdm.model;

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
public class MedicamentosDoacao {
    @Id
    @ColumnDefault("nextval('medicamentos_doacao_id_seq')")
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

    @ColumnDefault("now()")
    @Column(name = "data_cadastro", nullable = false)
    private OffsetDateTime dataCadastro;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade_destino")
    private Entidade idEntidadeDestino;

/*
 TODO [Reverse Engineering] create field to map the 'status_doacao' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'Cadastrado'")
    @Column(name = "status_doacao", columnDefinition = "status_doacao not null")
    private Object statusDoacao;
*/
}