package br.edu.ifpb.pweb2.Jar.repository;

import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.model.StatusOfertaEstagio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfertaEstagioRepository extends JpaRepository<OfertaEstagio, Long> {

    List<OfertaEstagio> findByStatus(Integer status);
}
