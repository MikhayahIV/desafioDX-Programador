package br.com.duxusdesafio.service;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.CompTimeRepository;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import javassist.NotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    private boolean estaNoPeriodo(Time time, LocalDate dataInicial, LocalDate dataFinal){
        return (dataInicial == null || !time.getData().isBefore(dataInicial)) && (dataFinal == null || !time.getData().isAfter(dataFinal));
    }

    /**
     * Vai retornar um Time, com a composição do time daquela data
     */
    public Time timeDaData(LocalDate data, List<Time> todosOsTimes) throws NotFoundException {
        // TODO Implementar método seguindo as instruções!
        return todosOsTimes.stream()
                .filter(time -> time.getData().equals(data))
                .findFirst()
                .orElseThrow(() ->  new NotFoundException("Nenhum time com a data informada"));
    }

    /**
     * Vai retornar o integrante que estiver presente na maior quantidade de times
     * dentro do período
     */
    public Integrante integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        Map<Integrante, Integer> contagem = new HashMap<>();
        for (Time time : todosOsTimes){
            if (!estaNoPeriodo(time,dataInicial,dataFinal)){
                continue;
                }
            Set<Integrante> integranteDoTime = time.getComposicaoTime()
                    .stream()
                    .map(ComposicaoTime::getIntegrante)
                    .collect(Collectors.toSet());

            for(Integrante integrante : integranteDoTime){
                contagem.merge(integrante,1,Integer::sum);
            }
        }
        return contagem.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Vai retornar uma lista com os nomes dos integrantes do time mais recorrente dentro do período.
     * OBS: Time é o clube + composição em determinada data
     */
    public List<String> integrantesDoTimeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!

        Map<String, Integer> contagemClubes = new LinkedHashMap<>();
        for(Time time: todosOsTimes){
            if(!estaNoPeriodo(time,dataInicial,dataFinal)) {
                continue;
            }
            String chaveTime = gerarChaveDoTime(time);
            contagemClubes.merge(chaveTime, 1, Integer::sum);
        }

        String chaveTimeMaisRecorrente = null;
        int maiorContagem = 0;

        for(Map.Entry<String, Integer> entry:contagemClubes.entrySet()){
            if(entry.getValue() > maiorContagem){
                maiorContagem = entry.getValue();
                chaveTimeMaisRecorrente = entry.getKey();
            }
        }

        if(chaveTimeMaisRecorrente == null){
            return Collections.emptyList();
        }
        for (Time time : todosOsTimes) {
            if (!estaNoPeriodo(time, dataInicial, dataFinal)) {
                continue;
            }

            if (gerarChaveDoTime(time).equals(chaveTimeMaisRecorrente)) {
                return time.getComposicaoTime()
                        .stream()
                        .map(composicaoTime ->
                                composicaoTime.getIntegrante().getNome())
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    private String gerarChaveDoTime(Time time){
        String integrantes = time.getComposicaoTime()
                .stream()
                .map(composicaoTime -> String.valueOf(composicaoTime.getIntegrante().getId()))
                .sorted().collect(Collectors.joining(","));

        return time.getNomeDoClube() + "|" + integrantes;
    }

    /**
     * Vai retornar a função mais recorrente nos times dentro do período
     */
    public String funcaoMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        Map<String, Integer> contagemFuncoes = new HashMap<>();
        for(Time time: todosOsTimes){
            if(!estaNoPeriodo(time,dataInicial,dataFinal)){
                continue;
                }
            for(ComposicaoTime composicaoTime: time.getComposicaoTime()){
                String funcao = composicaoTime.getIntegrante().getFuncao();
                contagemFuncoes.merge(funcao,1,Integer::sum);
            }
        }
        return contagemFuncoes.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Vai retornar o nome do Clube mais comum dentro do período
     */
    public String clubeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // TODO Implementar método seguindo as instruções!

        Map<String, Integer> contagemClubes = new LinkedHashMap<>();
        for(Time time: todosOsTimes){
            if(!estaNoPeriodo(time,dataInicial,dataFinal)){
                continue;
            }
            String clube = time.getNomeDoClube();
            contagemClubes.merge(clube,1,Integer::sum);
        }

        String clubeMaisRecorrente = null;
        int maiorContagem = 0;

        for(Map.Entry<String, Integer> entry: contagemClubes.entrySet()){
            if(entry.getValue()> maiorContagem){
                maiorContagem = entry.getValue();
                clubeMaisRecorrente = entry.getKey();
            }
        }
        return clubeMaisRecorrente;
    }


    /**
     * Vai retornar o número (quantidade) de aparições de cada Clube participante no período
     */
    public Map<String, Long> contagemDeClubesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return todosOsTimes.stream()
                .filter(time -> estaNoPeriodo(time,dataInicial,dataFinal))
                .collect(Collectors.groupingBy(Time::getNomeDoClube,Collectors.counting()));
    }

    /**
     * Vai retornar o número (quantidade) de Funções dentro do período.
     * Dica - pense sobre repetições!
     */
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return todosOsTimes.stream()
                .filter(time -> estaNoPeriodo(time,dataInicial,dataFinal))
                .flatMap(time -> time.getComposicaoTime().stream()).map(composicaoTime -> composicaoTime.getIntegrante().getFuncao())
                        .collect(Collectors.groupingBy(funcao ->funcao,Collectors.counting()
                        ));
    }

    public Integrante cadastraIntegrante(Integrante integrante){
        return integranteRepository.save(integrante);
    }

    public Time cadastrarTime(Time time) throws NotFoundException{
        if(time.getComposicaoTime() != null){
            for(ComposicaoTime composicao: time.getComposicaoTime()){
                Long integranteId = composicao.getIntegrante().getId();
                Integrante integrante = integranteRepository.findById(integranteId).orElseThrow(() -> new NotFoundException("Integrante não encontrado: "+integranteId));

                composicao.setIntegrante(integrante);
                composicao.setTime(time);
            }
        }
        return timeRepository.save(time);
    }

}
