package projetosgdm.sgdm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefone", length = 15)
    private String telefone;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;



    @ColumnDefault("now()")
    @Column(name = "data_cadastro", nullable = false)
    private OffsetDateTime dataCadastro;

    @ColumnDefault("now()")
    @Column(name = "ultimo_acesso", nullable = false)
    private OffsetDateTime ultimoAcesso;

    @PrePersist
    public void prePersist() {
        if (this.dataCadastro == null) {
            this.dataCadastro = OffsetDateTime.now();
        }
        if (this.ultimoAcesso == null) {
            this.ultimoAcesso = OffsetDateTime.now();
        }
    }


    @Column(name = "cep", nullable = false, length = 9)
    private String cep;

    @Column(name = "logradouro", nullable = false)
    private String logradouro;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Column(name = "bairro", nullable = false, length = 100)
    private String bairro;

    @Column(name = "municipio", nullable = false, length = 100)
    private String municipio;
    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

/*
 TODO [Reverse Engineering] create field to map the 'status' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @ColumnDefault("'Ativo'")
    @Column(name = "status", columnDefinition = "ativo_inativo")
    private Object status;
*/
}