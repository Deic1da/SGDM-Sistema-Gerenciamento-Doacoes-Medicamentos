package projetosgdm.sgdm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "vinculos_farmaceutico")
public class VinculosFarmaceutico {
    @Id
    @ColumnDefault("nextval('vinculos_farmaceutico_id_vinculo_farmaceutico_seq')")
    @Column(name = "id_vinculo_farmaceutico", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade")
    private Entidade idEntidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_farmaceutico")
    private Farmaceutico idFarmaceutico;

    @Column(name = "date_inicio", nullable = false)
    private LocalDate dateInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

/*
 TODO [Reverse Engineering] create field to map the 'tipo_vinculo' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'Responsavel_Tecnico'")
    @Column(name = "tipo_vinculo", columnDefinition = "vinculo not null")
    private Object tipoVinculo;
*/
/*
 TODO [Reverse Engineering] create field to map the 'status_vinculo' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'Pendente'")
    @Column(name = "status_vinculo", columnDefinition = "status_vinculo not null")
    private Object statusVinculo;
*/
}