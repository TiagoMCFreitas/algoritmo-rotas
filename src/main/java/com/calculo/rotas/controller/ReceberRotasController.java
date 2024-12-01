package com.calculo.rotas.controller;

import com.calculo.rotas.Dtos.ReceberRotasDto;
//import com.calculo.rotas.service.ReceberRotas;
import com.calculo.rotas.EnvioDtos.EnvioRotasDto;
import com.calculo.rotas.service.ReceberRotasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rotas")


public class ReceberRotasController {

        @Autowired
        private ReceberRotasService receberRotasService;
        @PostMapping("/")
        public EnvioRotasDto CalcularRotas(@RequestBody ReceberRotasDto receberRotasDto) throws Exception {
                return receberRotasService.receberRotas(receberRotasDto);
        }

}
