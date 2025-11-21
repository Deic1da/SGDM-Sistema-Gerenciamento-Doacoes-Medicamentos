package projetosgdm.sgdm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "entregas")
public class Entregas {
    @Id
    @ColumnDefault("nextval('entregas_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estoque_entidade")
    private EstoqueEntidade idEstoqueEntidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_farmaceutico_entregador")
    private Farmaceutico idFarmaceuticoEntregador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_receptor_pf")
    private Usuario idReceptorPf;

    @ColumnDefault("now()")
    @Column(name = "data_entrega", nullable = false)
    private OffsetDateTime dataEntrega;

/*
 TODO [Reverse Engineering] create field to map the 'status_entrega' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "status_entrega", columnDefinition = "status_entrega not null")
    private Object statusEntrega;
*/
}