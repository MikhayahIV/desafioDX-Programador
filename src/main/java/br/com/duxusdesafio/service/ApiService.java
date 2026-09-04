package br.com.duxusdesafio.service;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.CompTimeRepository;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service que possuirá as regras de negócio para o processamento dos dados
 * solicitados no desafio!
 *
 * OBS ao candidato: PREFERENCIALMENTE, NÃO ALTERE AS ASSINATURAS DOS MÉTODOS!
 * Trabalhe com a proposta pura.
 *
 * @author carlosau
 */
@Service
public class ApiService {

    private final CompTimeRepository compTimeRepository;
    private final IntegranteRepository integranteRepository;
    private final TimeRepository timeRepository;

    public ApiService(CompTimeRepository compTimeRepository, IntegranteRepository integranteRepository, TimeRepository timeRepository) {
        this.compTimeRepository = compTimeRepository;
        this.integranteRepository = integranteRepository;
        this.timeRepository = timeRepository;
    }

    /**
     * Vai retornar um Time, com a composição do time daquela data
     */
    public Time timeDaData(LocalDate data, List<Time> todosOsTimes) throws NotFoundException {
        // TODO Implementar método seguindo as instruções!
        /*return timeRepository.findByData(data)
                .orElseThrow(() ->  new NotFoundException("Nenhum time com a data informada")); */
        return todosOsTimes.stream()
                .filter(time -> time.getData().equals(data))
                .findFirst().orElseThrow(() ->  new NotFoundException("Nenhum time com a data informada"));
    }

    /**
     * Vai retornar o integrante que estiver presente na maior quantidade de times
     * dentro do período
     */
    public Integrante integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        Map<Integrante, Integer> contagem = new HashMap<>();
        for (Time time : todosOsTimes){
            if (!time.getData().isBefore(dataInicial)&& !time.getData().isAfter(dataFinal)){
                for(ComposicaoTime composicaoTime : time.getComposicaoTime()){
                    Integrante integrante = composicaoTime.getIntegrante();
                    contagem.merge(integrante,1,Integer::sum);
                }
            }
        }
        return contagem.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null)
                ;
    }

    /**
     * Vai retornar uma lista com os nomes dos integrantes do time mais recorrente dentro do período.
     * OBS: Time é o clube + composição em determinada data
     */
    public List<String> integrantesDoTimeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return null;
    }

    /**
     * Vai retornar a função mais recorrente nos times dentro do período
     */
    public String funcaoMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return null;
    }

    /**
     * Vai retornar o nome do Clube mais comum dentro do período
     */
    public String clubeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // TODO Implementar método seguindo as instruções!

        Map<String, Integer> contagemClubes = new HashMap<>();
        for(Time time: todosOsTimes){
            if(!time.getData().isBefore(dataInicial)&&!time.getData().isAfter(dataFinal)){
                String clube = time.getNomeDoClube();

                contagemClubes.merge(clube,1,Integer::sum);
            }
        }
        return contagemClubes.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }


    /**
     * Vai retornar o número (quantidade) de aparições de cada Clube participante no período
     */
    public Map<String, Long> contagemDeClubesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return null;
    }

    /**
     * Vai retornar o número (quantidade) de Funções dentro do período.
     * Dica - pense sobre repetições!
     */
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return null;
    }

}
