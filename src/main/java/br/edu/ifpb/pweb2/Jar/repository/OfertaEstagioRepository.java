package br.edu.ifpb.pweb2.Jar.repository;

import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertaEstagioRepository extends JpaRepository<OfertaEstagio, Long> {
}
