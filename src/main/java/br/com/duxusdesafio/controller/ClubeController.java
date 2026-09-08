package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
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
    public Map<String,String> clubeMaisRecorrente(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataIncial, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        List<Time> todosOsTimes = repository.findAll();
        return Collections.singletonMap("Clube",service.clubeMaisRecorrente(dataIncial,dataFinal,todosOsTimes));
    }

    @GetMapping("/contagem")
    public Map<String,Long> contagemClubesNoPeriodo(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataIncial, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        List<Time> todosOsTimes = repository.findAll();
        return service.contagemDeClubesNoPeriodo(dataIncial,dataFinal,todosOsTimes);
    }
}
