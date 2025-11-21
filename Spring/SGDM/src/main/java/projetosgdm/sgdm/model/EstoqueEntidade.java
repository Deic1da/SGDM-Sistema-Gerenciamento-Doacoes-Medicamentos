package projetosgdm.sgdm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "estoque_entidade")
public class EstoqueEntidade {
    @Id
    @ColumnDefault("nextval('estoque_entidade_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade")
    private Entidade idEntidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_validacao")
    private Validacoe idValidacao;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @ColumnDefault("now()")
    @Column(name = "data_entrada", nullable = false)
    private OffsetDateTime dataEntrada;

/*
 TODO [Reverse Engineering] create field to map the 'status_estoque' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "status_estoque", columnDefinition = "status_estoque not null")
    private Object statusEstoque;
*/
}