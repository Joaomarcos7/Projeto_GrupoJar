package br.edu.ifpb.pweb2.Jar.repository;

import br.edu.ifpb.pweb2.Jar.model.Candidatura;
import br.edu.ifpb.pweb2.Jar.model.EstadoCandidatura;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfertaEstagioRepository extends JpaRepository<OfertaEstagio, Long> {

    Page<OfertaEstagio> findByStatus(Integer status, Pageable pageable);

    Page<OfertaEstagio> findAll(Pageable pageable);

    Page<OfertaEstagio> findByEmpresaId(Long empresaId, Pageable pageable);
}
