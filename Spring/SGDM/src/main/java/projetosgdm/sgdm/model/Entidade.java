package projetosgdm.sgdm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "entidades")
public class Entidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    @Column(name = "cnpj", nullable = false, length = 18)
    private String cnpj;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dono_entidade")
    @JsonIgnore // Só para evitar erro ao serializar GET!
    private Usuario idDonoEntidade;

    @Column(name = "horario_funcionamento", nullable = false, length = 100)
    private String horarioFuncionamento;

    @Column(name = "aceita_validade_curta", nullable = false)
    private Boolean aceitaValidadeCurta = false;

    @ColumnDefault("now()")
    @Column(name = "data_cadastro", nullable = false)
    private OffsetDateTime dataCadastro;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmaceutico_rt")
    @JsonIgnore // só para serialização; pode usar normalmente no cadastro
    private Farmaceutico farmaceuticoRt;

    /*
     TODO [Reverse Engineering] create field to map the 'status' column
     Available actions: Define target Java type | Uncomment as is | Remove column mapping
        @ColumnDefault("'Pendente'")
        @Column(name = "status", columnDefinition = "status_entidade not null")
        private Object status;
    */
}
