package projetosgdm.sgdm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "validacoes")
public class Validacoe {
    @Id
    @ColumnDefault("nextval('validacoes_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medicamento_doacao")
    private MedicamentosDoacao idMedicamentoDoacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_farmaceutico_validante")
    private Farmaceutico idFarmaceuticoValidante;

    @Column(name = "motivo_rejeicao")
    private String motivoRejeicao;
    @ColumnDefault("now()")
    @Column(name = "data_validacao", nullable = false)
    private OffsetDateTime dataValidacao;

/*
 TODO [Reverse Engineering] create field to map the 'status_validacao' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "status_validacao", columnDefinition = "status_validacao not null")
    private Object statusValidacao;
*/
}