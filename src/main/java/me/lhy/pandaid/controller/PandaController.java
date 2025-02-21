package me.lhy.pandaid.controller;


import me.lhy.pandaid.service.PandaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/panda")
public class PandaController {

    private final PandaService pandaService;

    public PandaController(PandaService pandaService) {
        this.pandaService = pandaService;
    }
}
