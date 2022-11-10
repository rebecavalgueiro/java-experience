package com.ola.mundo.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ola.mundo.model.Saudacao;
import com.ola.mundo.repository.SaudacaoRepository;

@RestController
public class OlaMundoController {
    
    @GetMapping("/ola")
    public Saudacao digaOla(@RequestParam(name = "id", defaultValue = "0") int id, 
                            @RequestParam(name = "mensagem", defaultValue = "null ") String mensagem){
        return new Saudacao(id, mensagem);
    }

    @PostMapping("/saudacao")
    public Saudacao salvarSaudacao(@RequestBody Saudacao saudacao){
        return saudacaoRepository.save(saudacao);
    }

    @GetMapping("/saudacao")
    public Iterable<Saudacao> obterTidasSaudacoes(){
        return saudacaoRepository.findAll();
    }

    @GetMapping("/saudacao/{id}")
    public Saudacao obterSaudacaoPorID(@PathVariable String id){
        return saudacaoRepository.findById(Integer.valueOf(id)).get();
    }

    @DeleteMapping("/saudacao/{id}")
    public String excluirSaudacaoPeloId(@PathVariable("id") String id){
        saudacaoRepository.deleteById(Integer.valueOf(id));
        return "Registro deletado com sucesso";
    }
    
    @PutMapping("/saudacao/{id}")
    public Saudacao atualizaSaudacao(@RequestBody Saudacao novaSaudacao, @PathVariable String id) {
      
      return saudacaoRepository.findById(Integer.valueOf(id))
        .map(saudacao -> {
          saudacao.setMensagem(novaSaudacao.getMensagem());
          return saudacaoRepository.save(saudacao);
        })
        .orElseGet(() -> {
            novaSaudacao.setId(Integer.valueOf(id));
          return saudacaoRepository.save(novaSaudacao);
        });
    }

    @PutMapping("/saudacao")
    public Object atualizaSaudacao(@RequestBody Saudacao novaSaudacao) {
        try{
            Saudacao saudacao = saudacaoRepository.findById(novaSaudacao.getId()).get();
            saudacao.setMensagem(novaSaudacao.getMensagem());
            return saudacaoRepository.save(saudacao);

        }catch(NoSuchElementException e){
            return "Não há saudação com ID " + String.valueOf(novaSaudacao.getId());
        }

    }
  


    @Autowired
    private SaudacaoRepository saudacaoRepository;

}
