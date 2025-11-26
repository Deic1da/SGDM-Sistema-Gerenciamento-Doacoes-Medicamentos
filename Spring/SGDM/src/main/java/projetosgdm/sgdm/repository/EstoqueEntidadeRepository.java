package projetosgdm.sgdm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projetosgdm.sgdm.model.EstoqueEntidade;

import java.util.List;
import java.util.Map;

public interface EstoqueEntidadeRepository extends JpaRepository<EstoqueEntidade, Integer> {

    @Query(value = "SELECT " +
            "ee.id, " +
            "md.nome_medicamento, " +
            "md.forma_farmaceutica, " +
            "TO_CHAR(md.data_validade, 'DD/MM/YYYY') as data_validade, " +
            "ee.status_estoque " +
            "FROM estoque_entidade ee " +
            "INNER JOIN validacoes v ON ee.id_validacao = v.id " +
            "INNER JOIN medicamentos_doacao md ON v.id_medicamento_doacao = md.id " +
            "WHERE ee.id_entidade = :entidadeId " +
            "ORDER BY md.data_validade ASC",
            nativeQuery = true)
    List<Map<String, Object>> findMedicamentosByEntidadeId(@Param("entidadeId") Integer entidadeId);
}
