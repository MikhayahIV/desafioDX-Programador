package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/integrantes")
public class IntegranteController {

    private final ApiService service;
    private final TimeRepository repository;

    public IntegranteController(ApiService service, TimeRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping
    public Integrante cadastrarIntegrante(@RequestBody Integrante integrante){
        return  service.cadastraIntegrante(integrante);
    }

    @GetMapping("/mais-usado")
    public Integrante integranteMaisUsado(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        List<Time> todosOsTimes = repository.findAll();
        return service.integranteMaisUsado(dataInicial,dataFinal,todosOsTimes);
    }

    @GetMapping("/funcao/mais-recorrente")
    public Map<String, String> funcaoMaisRecorrente(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        List<Time> todosOsTimes = repository.findAll();
        return Collections.singletonMap("Funcao",service.funcaoMaisRecorrente(dataInicial,dataFinal,todosOsTimes));
    }

    @GetMapping("/funcao/contagem")
    public Map<String,Long> contagemPorFuncao(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        List<Time> todosOsTimes = repository.findAll();
        return service.contagemPorFuncao(dataInicial,dataFinal,todosOsTimes);
    }
}
