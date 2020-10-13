package br.com.nossobancodigital.repository;

import br.com.nossobancodigital.entity.PropostaInfosBasicas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropostaInfosBasicasRepository extends JpaRepository<PropostaInfosBasicas, Long> {
}
