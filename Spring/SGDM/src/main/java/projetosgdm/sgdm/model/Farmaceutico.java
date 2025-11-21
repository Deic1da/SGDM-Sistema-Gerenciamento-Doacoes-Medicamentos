package projetosgdm.sgdm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "farmaceuticos")
public class Farmaceutico {
    @Id
    @ColumnDefault("nextval('farmaceuticos_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_pf")
    private Usuario idUsuarioPf;

    @Column(name = "num_crf", nullable = false, length = 20)
    private String numCrf;

/*
 TODO [Reverse Engineering] create field to map the 'uf_crf' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "uf_crf", columnDefinition = "estado_crf not null")
    private Object ufCrf;
*/
/*
 TODO [Reverse Engineering] create field to map the 'status_profissional' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "status_profissional", columnDefinition = "status_farmaceutico not null")
    private Object statusProfissional;
*/
}