package projetosgdm.sgdm.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "descarte")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Descarte {
    @Id
    @ColumnDefault("nextval('descarte_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medicamento_doacao")
    private MedicamentosDoacao idMedicamentoDoacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade")
    private Entidade idEntidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_farmaceutico_responsavel")
    private Farmaceutico idFarmaceuticoResponsavel;

    @Column(name = "motivo_descarte", nullable = false)
    private String motivoDescarte;

    @ColumnDefault("now()")
    @Column(name = "data_descarte", nullable = false)
    private OffsetDateTime dataDescarte;

/*
 TODO [Reverse Engineering] create field to map the 'destino_final' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "destino_final", columnDefinition = "destinos_finais not null")
    private Object destinoFinal;
*/
}