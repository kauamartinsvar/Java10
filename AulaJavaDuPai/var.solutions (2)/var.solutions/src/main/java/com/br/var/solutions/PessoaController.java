package com.br.var.solutions;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("/pessoa")
@CrossOrigin(origins = "*")
@Slf4j
public class PessoaController {

private static  final String SECRET_KEY = "tokenApiVarSolutionsasoksdnvpasjdasm9a0scasckna0c9cweosd0ihsadlkksdhiihdsfoioiiiiiiiii";
    @GetMapping
    public ResponseEntity<Object> get() {
        PessoaRequest pessoaRequest1 = new PessoaRequest();
        pessoaRequest1.setNome("kaua");
        pessoaRequest1.setSobrenome("ferreira");
        pessoaRequest1.setEndereco("barueri");
        pessoaRequest1.setIdade(17);
        return ResponseEntity.ok(pessoaRequest1);
    }

    @GetMapping("/resumo")
    public ResponseEntity<Object> getPessoa(@RequestBody PessoaRequest pessoinha, @RequestParam(value = "valida_mundial") Boolean DesejaValidarMundial) {
     InformacoesImc imc = new InformacoesImc();
        int anoNascimento = 0;
        String impostoRenda = null;
        String validaMundial = null;
        String saldoEmDolar = null;

        if (!pessoinha.getNome().isEmpty()) {
            log.info("Iniciando o processo de calculo de pessoa", pessoinha);

            if (Objects.nonNull(pessoinha.getPeso()) && Objects.nonNull(pessoinha.getAltura())) {
                log.info("Iniciando o calculo de IMC");
                imc = calculaImc(pessoinha.getPeso(), pessoinha.getAltura());
            }

            if (Objects.nonNull(pessoinha.getIdade())) {
                log.info("Iniciando o calculo do ano de nascimento");
                anoNascimento = calculaAnoNascimento(pessoinha.getIdade());
            }

            if (Objects.nonNull(pessoinha.getSalario())) {
                log.info("Iniciando o calculo de imposto de renda");
                impostoRenda = calculaFaixaImpostoRenda(pessoinha.getSalario());
            }
            if (Boolean.TRUE.equals(DesejaValidarMundial)){
                if (Objects.nonNull(pessoinha.getTime()))
                {
                    log.info("Validando se o time de coração tem mundial");
                    validaMundial = calculaMundial(pessoinha.getTime());
                }
            }

            if (Objects.nonNull(pessoinha.getSaldo())) {
                log.info("Iniciando calculo para converter saldo em dolar");
                saldoEmDolar = converterSaldoEmDolar(pessoinha.getSaldo());
            }

            log.info("Montando Objeto de retorno para o Front-End");
            PessoaResponse resumo = montarRespostaFrontEnd(pessoinha,imc, anoNascimento, impostoRenda,validaMundial, saldoEmDolar);


            return ResponseEntity.ok(resumo);

        }
        return ResponseEntity.noContent().build();
    }

    private String converterSaldoEmDolar(double saldo) {
        return String.valueOf(saldo / 5.11);
    }

    private PessoaResponse montarRespostaFrontEnd(PessoaRequest pessoa, InformacoesImc imc, int anoNascimento, String impostoRenda, String validaMundial,String saldoEmDolar) {
       PessoaResponse response = new PessoaResponse();

       response.setNome(pessoa.getNome());
       response.setSobrenome(pessoa.getSobrenome());
       response.setIdade(pessoa.getIdade());
       response.setClassificacaoIMC(imc.getClassificao());
       response.setImc(imc.getImc());
       response.setSalario(impostoRenda);
       response.setAnoNascimento(anoNascimento);
       response.setMundialClubes(validaMundial);
       response.setSaldoEmDolar(saldoEmDolar);
       response.setTime(pessoa.getTime());
       response.setEndereco(pessoa.getEndereco());
       response.setPeso(pessoa.getPeso());
       response.setAltura(pessoa.getAltura());
       response.setSaldo(pessoa.getSaldo());

       return response;
    }

    private String calculaMundial(String time) {
        if(time.equalsIgnoreCase("Corinthians"))
        {
            return " Parabens, o seu time possui 2 mundiais de clubes conforme a FIFA";
        }
        else if (time.equalsIgnoreCase("São Paulo"))
        {
            return "Parabens, o seu time possui 3 mundiais pc";
        }
        else if (time.equalsIgnoreCase("Santos"))
        {
        return " A plmds né se não tiver mundial pode parar pra mim nem time é";
        }
        else
        {
            return "Irmão para de sofrer, para de torcer pra esse time de sofredor namoral mo desacerto esse time ai jão ";
        }
    }

    private String calculaFaixaImpostoRenda(double salario) {

        log.info("Iniciando o calculo do imposto de renda" + salario);
        String novoSalarioCalculado;
        if (salario < 1903.98)
        {
            return "Se ta passando dificuldade man ? Ta duro se fala que faço a sua rapaz, eu em slk tiu";
        }
        else if (salario > 1903.98 && salario < 2826.65)
        {
            double calculoIRF = 142.80 - ((salario * 0.075)/100);
            double novoSalario = salario - calculoIRF;
            novoSalarioCalculado = String.valueOf(novoSalario);
            return novoSalarioCalculado;
        }
        else if (salario > 2826.66 && salario <3075.05) {
            double calculoIRF = 142.80 - ((salario * 0.015)/100);
            double novoSalario = salario - calculoIRF;
            novoSalarioCalculado = String.valueOf(novoSalario);
            return novoSalarioCalculado;
        }
        else if (salario >= 3751.06 && salario <4664.68)
        {
            double calculoIRF = 636.13 - ((salario * 0.225)/100);
            double novoSalario = salario - calculoIRF;
            novoSalarioCalculado = String.valueOf(novoSalario);
            return novoSalarioCalculado;
        }
        else
        {
            double calculoIRF = 869.36 - ((salario * 275)/100);
            double novoSalario = salario - calculoIRF;
            novoSalarioCalculado = String.valueOf(novoSalario);
            return novoSalarioCalculado;
        }

    }

    private int calculaAnoNascimento(int idade) {
        LocalDate datalocal = LocalDate.now();
        int anoAtual = datalocal.getYear();
        return anoAtual - idade;
    }

    private InformacoesImc calculaImc(double peso, double altura) {

        double imc = peso / (altura * altura);

        InformacoesImc imcCaculado = new InformacoesImc();
        if (imc < 18.5) {
            imcCaculado.setImc(String.valueOf(imc));
            imcCaculado.setClassificao("abaixo do peso.");
            return imcCaculado;
        } else if (imc >= 18.5 && imc <= 24.9) {
            imcCaculado.setImc(String.valueOf(imc));
            imcCaculado.setClassificao("abaixo do peso.");
            return imcCaculado;
        } else if (imc > 29.9 && imc <= 29.9) {
            imcCaculado.setImc(String.valueOf(imc));
            imcCaculado.setClassificao(" abaixo do peso.");
            return imcCaculado;
        } else if (imc > 29.9 && imc <= 34.9) {
            imcCaculado.setImc(String.valueOf(imc));
            imcCaculado.setClassificao(" abaixo do peso.");
            return imcCaculado;
        }
       else if (imc <= 34.9 && imc <= 39.9) {
            imcCaculado.setImc(String.valueOf(imc));
            imcCaculado.setClassificao(" abaixo do peso.");
            return imcCaculado;
        }
       else {
            imcCaculado.setImc(String.valueOf(imc));
            imcCaculado.setClassificao(" abaixo do peso.");
            return imcCaculado;
        }
    }
 @PostMapping(path = "/authorization", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> authorization(@RequestParam("client_id") String clientId,
                                                @RequestParam("password") String password){
        Boolean validaUsuario = ValidaUsuario.validaUsuario(clientId, password);
        if (Boolean.FALSE.equals(validaUsuario)){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario ou senha invalidos");
        }

        long expirationDate = System.currentTimeMillis() + 1800000;

        String token = Jwts.builder()
                .setSubject(clientId)
                .setExpiration(new Date(expirationDate))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return ResponseEntity.ok(token);
 }

}

