package br.edu.ifpb.pweb2.Jar.repository;

import br.edu.ifpb.pweb2.Jar.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
}
