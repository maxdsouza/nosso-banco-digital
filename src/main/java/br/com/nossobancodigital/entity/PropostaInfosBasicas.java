package br.com.nossobancodigital.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class PropostaInfosBasicas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Nome do cliente é obrigatório.")
    @Column(nullable = false)
    private String nome;

    @NotEmpty(message = "Sobrenome do cliente é obrigatório.")
    @Column(nullable = false)
    private String sobrenome;

    @Email(message = "Email com formato incorreto.")
    @NotEmpty(message = "Email é obrigatório.")
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull(message = "Data de nascimento é obrigatório.")
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy",
            locale = "pt-BR", timezone = "Brazil/East")
    @Column(nullable = false)
    private Date dataNascimento;

    @CPF(message = "CPF é obrigatório e precisa respeitar o formato.")
    @Column(unique = true, nullable = false)
    private String cpf;

    @OneToOne(mappedBy = "propostaInfosBasicas", orphanRemoval = true)
    @JsonBackReference
    private PropostaEndereco propostaEndereco;
}
