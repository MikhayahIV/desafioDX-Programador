package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    private final ApiService service;
    private final TimeRepository repository;

    public ClubeController(ApiService service, TimeRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping("/mais-recorrente")
    public String clubeMaisRecorrente(@RequestParam LocalDate dataIncial, @RequestParam LocalDate dataFinal){
        List<Time> todosOsTimes = repository.findAll();
        return service.clubeMaisRecorrente(dataIncial,dataIncial,todosOsTimes);
    }

    @GetMapping("/contagem")
    public Map<String,Long> contagemClubesNoPeriodo(@RequestParam LocalDate dataIncial, @RequestParam LocalDate dataFinal){
        List<Time> todosOsTimes = repository.findAll();
        return service.contagemDeClubesNoPeriodo(dataIncial,dataIncial,todosOsTimes);
    }
}
