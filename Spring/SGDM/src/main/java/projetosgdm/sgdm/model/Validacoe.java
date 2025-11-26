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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medicamento_doacao")
    private MedicamentosDoacao idMedicamentoDoacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_farmaceutico_validante")
    private Farmaceutico idFarmaceuticoValidante;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_validacao", nullable = false)
    private StatusValidacao statusValidacao;

    @Column(name = "motivo_rejeicao", length = 255)
    private String motivoRejeicao;

    @ColumnDefault("now()")
    @Column(name = "data_validacao", nullable = false)
    private OffsetDateTime dataValidacao;

    public enum StatusValidacao {
        Aprovado, Rejeitado
    }
}
