package projetosgdm.sgdm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "log_auditoria")
public class LogAuditoria {
    @Id
    @ColumnDefault("nextval('log_auditoria_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ator_responsavel")
    private Usuario idAtorResponsavel;

    @Column(name = "registro_afetado", nullable = false, length = 50)
    private String registroAfetado;

    @Column(name = "tabela_afetada", nullable = false, length = 50)
    private String tabelaAfetada;

    @Column(name = "descricao_alteracao", length = Integer.MAX_VALUE)
    private String descricaoAlteracao;
    @ColumnDefault("now()")
    @Column(name = "data_hora", nullable = false)
    private OffsetDateTime dataHora;

/*
 TODO [Reverse Engineering] create field to map the 'tipo_operacao' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'Validacao'")
    @Column(name = "tipo_operacao", columnDefinition = "tipo_operacao not null")
    private Object tipoOperacao;
*/
}