package br.edu.ifpb.pweb2.Jar.seeder;

import br.edu.ifpb.pweb2.Jar.model.Authority;
import br.edu.ifpb.pweb2.Jar.model.Coordenador;
import br.edu.ifpb.pweb2.Jar.repository.AuthorityRepository;
import br.edu.ifpb.pweb2.Jar.repository.CoordenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {
        try {
            seedCoordenador();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void seedCoordenador() {
        if (coordenadorRepository.count() == 0) {
            Coordenador coordinator = new Coordenador();
            coordinator.setUsername("coordenador");
            coordinator.setNome("Candido");
            coordinator.setEmail("coordenador@gmail.com");
            coordinator.setPassword(passwordEncoder.encode("admin"));

            Authority authority = new Authority();
            authority.setId(new Authority.AuthorityId(coordinator.getUsername(), "ROLE_ADMIN"));
            authority.setUsername(coordinator);
            authority.setAuthority("ROLE_ADMIN");

            coordinator.setAuthorities(List.of(authority));

            coordenadorRepository.save(coordinator);
            authorityRepository.save(authority);
        }
    }
}
