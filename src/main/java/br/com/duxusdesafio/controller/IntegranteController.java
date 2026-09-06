package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public Integrante integranteMaisUsado(@RequestParam LocalDate dataInicial, @RequestParam LocalDate dataFinal){
        List<Time> todosOsTimes = repository.findAll();
        return service.integranteMaisUsado(dataInicial,dataFinal,todosOsTimes);
    }

    @GetMapping("/funcao/mais-recorrente")
    public String funcaoMaisRecorrente(@RequestParam LocalDate dataInicial, @RequestParam LocalDate dataFinal){
        List<Time> todosOsTimes = repository.findAll();
        return service.funcaoMaisRecorrente(dataInicial,dataFinal,todosOsTimes);
    }

    @GetMapping("/funcao/contagem")
    public Map<String,Long> contagemPorFuncao(@RequestParam LocalDate dataInicial, @RequestParam LocalDate dataFinal){
        List<Time> todosOsTimes = repository.findAll();
        return service.contagemPorFuncao(dataInicial,dataFinal,todosOsTimes);
    }
}
