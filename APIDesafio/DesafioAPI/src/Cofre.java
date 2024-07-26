package com.example.cofre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;

// Application class
@SpringBootApplication
public class CofreApplication {
    public static void main(String[] args) {
        SpringApplication.run(CofreApplication.class, args);
    }
}

// OpenAPI configuration class
@Configuration
class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cofre API")
                        .version("1.0")
                        .description("API para gerenciamento de cofres digitais e físicos"));
    }
}

// Abstract Cofre class
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class Cofre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private String metodoAbertura;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMetodoAbertura() {
        return metodoAbertura;
    }

    public void setMetodoAbertura(String metodoAbertura) {
        this.metodoAbertura = metodoAbertura;
    }
}

// CofreDigital class
@Entity
@DiscriminatorValue("Digital")
class CofreDigital extends Cofre {
    private int senha;

    public CofreDigital() {
        this.setTipo("Cofre Digital");
        this.setMetodoAbertura("Senha");
    }

    public int getSenha() {
        return senha;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }

    public boolean validarSenha(int confirmacaoSenha) {
        return confirmacaoSenha == this.senha;
    }
}

// CofreFisico class
@Entity
@DiscriminatorValue("Fisico")
class CofreFisico extends Cofre {
    public CofreFisico() {
        this.setTipo("Cofre Fisico");
        this.setMetodoAbertura("Chave");
    }
}

// Repository interface
interface CofreRepository extends JpaRepository<Cofre, Long> {
}

// Service class
@Service
class CofreService {

    private final CofreRepository cofreRepository;

    public CofreService(CofreRepository cofreRepository) {
        this.cofreRepository = cofreRepository;
    }

    public Cofre saveCofre(Cofre cofre) {
        return cofreRepository.save(cofre);
    }

    public List<Cofre> getAllCofres() {
        return cofreRepository.findAll();
    }

    public Optional<Cofre> getCofreById(Long id) {
        return cofreRepository.findById(id);
    }

    public void deleteCofre(Long id) {
        cofreRepository.deleteById(id);
    }
}

// Controller class
@RestController
@RequestMapping("/api/cofres")
class CofreController {

    private final CofreService cofreService;

    public CofreController(CofreService cofreService) {
        this.cofreService = cofreService;
    }

    @PostMapping("/digital")
    public CofreDigital createCofreDigital(@RequestBody CofreDigital cofreDigital) {
        return (CofreDigital) cofreService.saveCofre(cofreDigital);
    }

    @PostMapping("/fisico")
    public CofreFisico createCofreFisico() {
        CofreFisico cofreFisico = new CofreFisico();
        return (CofreFisico) cofreService.saveCofre(cofreFisico);
    }

    @GetMapping
    public List<Cofre> getAllCofres() {
        return cofreService.getAllCofres();
    }

    @GetMapping("/{id}")
    public Optional<Cofre> getCofreById(@PathVariable Long id) {
        return cofreService.getCofreById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCofre(@PathVariable Long id) {
        cofreService.deleteCofre(id);
    }
}
