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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status_estoque", nullable = false)
    private StatusEstoque statusEstoque;

    public enum StatusEstoque {
        Disponível, Reservado, Entregue, Descartado
    }
}
