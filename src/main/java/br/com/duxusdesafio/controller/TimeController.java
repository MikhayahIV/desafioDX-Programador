package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.ApiService;
import javassist.NotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeController {

    private final ApiService service;
    private final TimeRepository repository;


    public TimeController(ApiService service, TimeRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping
    public Time cadastrarTime(@RequestBody Time time) throws NotFoundException {
        return service.cadastrarTime(time);
    }

    @GetMapping
    public ResponseEntity<Time> buscarTimePorData(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) throws NotFoundException{
        List<Time> todosOsTimes = repository.findAll();
        Time time = service.timeDaData(data, todosOsTimes);
        return ResponseEntity.ok(time);
    }

    @GetMapping("/mais-recorrente/integrantes")
    public List<String> integrantesDoTimeMaisRecorrente(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal){
        List<Time> todosOstimes = repository.findAll();
        return service.integrantesDoTimeMaisRecorrente(dataInicial,dataFinal,todosOstimes);
    }
}
