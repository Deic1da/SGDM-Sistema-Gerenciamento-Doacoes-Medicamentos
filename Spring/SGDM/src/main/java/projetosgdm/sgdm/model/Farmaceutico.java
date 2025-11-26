package projetosgdm.sgdm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "farmaceuticos")
public class Farmaceutico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_pf")
    private Usuario idUsuarioPf;

    @Column(name = "num_crf", nullable = false, length = 20)
    private String numCrf;

    @Enumerated(EnumType.STRING)
    @Column(name = "uf_crf", nullable = false)
    private EstadoCrf ufCrf;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_profissional", nullable = false)
    private StatusProfissional statusProfissional;

    public enum EstadoCrf {
        AC, AL, AP, AM, BA, CE, DF, ES, GO, MA, MT, MS, MG, PA, PB, PR, PE, PI, RJ, RN, RS, RO, RR, SC, SP, SE, TO
    }

    public enum StatusProfissional {
        Ativo, Suspenso, Inativo
    }
}
