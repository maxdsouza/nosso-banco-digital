package br.com.nossobancodigital.controller;

import br.com.nossobancodigital.entity.PropostaEndereco;
import br.com.nossobancodigital.entity.PropostaInfosBasicas;
import br.com.nossobancodigital.repository.PropostaEnderecoRepository;
import br.com.nossobancodigital.repository.PropostaInfosBasicasRepository;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class PropostaController {

    private final PropostaInfosBasicasRepository propostaInfosBasicasRepository;
    private final PropostaEnderecoRepository propostaEnderecoRepository;
    private List<URI> uriListInfosBasicas = new ArrayList<>();

    PropostaController(PropostaInfosBasicasRepository propostaInfosBasicasRepository,
                       PropostaEnderecoRepository propostaEnderecoRepository) {
        this.propostaInfosBasicasRepository = propostaInfosBasicasRepository;
        this.propostaEnderecoRepository = propostaEnderecoRepository;
    }

    @ApiIgnore
    @RequestMapping(path = "/")
    public void redirecionaSwagger(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

    @ApiOperation(value = "Cadastro de informações básicas do cliente")
    @PostMapping(path = "/proposta/informacoes-basicas")
    public ResponseEntity<PropostaInfosBasicas> salvarInfosBasicas(@Valid @RequestBody PropostaInfosBasicas propostaInfosBasicas) {
        PropostaInfosBasicas propostaInfosBasicasPersistida = propostaInfosBasicasRepository.save(propostaInfosBasicas);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(propostaInfosBasicasPersistida.getId()).toUri();

        uriListInfosBasicas.add(location);

        return ResponseEntity.created(location).build();
    }

    @ApiOperation(value = "Retorna todas as propostas de informações básicas cadastradas")
    @GetMapping(path = "/proposta/informacoes-basicas")
    public List<PropostaInfosBasicas> buscarTodasInfosBasicas() {
        return propostaInfosBasicasRepository.findAll();
    }

    @ApiOperation(value = "Retorna uma proposta de informações básicas cadastrada de acordo com o id digitado")
    @GetMapping(path = {"/proposta/informacoes-basicas/{id}"})
    public ResponseEntity<PropostaInfosBasicas> buscarInfosBasicasPorId(@PathVariable long id) {
        return propostaInfosBasicasRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Atualiza as informações básicas do cliente cadastrado")
    @PutMapping(path = "/proposta/informacoes-basicas/{id}")
    public ResponseEntity<PropostaInfosBasicas> atualizarInfosBasicas(
            @PathVariable("id") long id,
            @RequestBody @Valid PropostaInfosBasicas propostaInfosBasicas
    ) {
        return propostaInfosBasicasRepository.findById(id)
                .map(record -> {
                    record.setNome(propostaInfosBasicas.getNome());
                    record.setSobrenome(propostaInfosBasicas.getSobrenome());
                    record.setEmail(propostaInfosBasicas.getEmail());
                    record.setDataNascimento(propostaInfosBasicas.getDataNascimento());
                    record.setCpf(propostaInfosBasicas.getCpf());
                    PropostaInfosBasicas atualizada = propostaInfosBasicasRepository.save(record);
                    return ResponseEntity.ok().body(atualizada);
                }).orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Deleta um cadastro informações básicas do cliente de acordo com o id digitado")
    @DeleteMapping(path = {"/proposta/informacoes-basicas/{id}"})
    public ResponseEntity<Object> deletarInfosBasicas(@PathVariable long id) {
        return propostaInfosBasicasRepository.findById(id)
                .map(record -> {
                    propostaInfosBasicasRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Cadastro do endereço cliente da proposta")
    @PostMapping(path = "/proposta/endereco")
    @Transactional
    public ResponseEntity<PropostaEndereco> salvarEndereco(@Valid @RequestBody PropostaEndereco propostaEndereco) {

        String caminho = uriListInfosBasicas.get(0).getPath();

        Optional<PropostaInfosBasicas> propostaInfosBasicasEncontrada = propostaInfosBasicasRepository.findById(Long.parseLong(caminho.substring(30)));
        propostaInfosBasicasEncontrada.ifPresent(propostaInfosBasicas -> propostaEndereco.setPropostaInfosBasicas(propostaInfosBasicas));
        PropostaEndereco propostaEnderecoPersistida = propostaEnderecoRepository.save(propostaEndereco);

        uriListInfosBasicas.clear();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(propostaEnderecoPersistida.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
