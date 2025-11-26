package projetosgdm.sgdm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroEntidadeDTO {
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String nomeFarmaceutico;
    private String cpfFarmaceutico;
    private String numCrf;
    private String horarioFuncionamento;
    private Boolean aceitaValidadeCurta;
    private Double latitude;
    private Double longitude;
    private Integer idDonoEntidade;
}
