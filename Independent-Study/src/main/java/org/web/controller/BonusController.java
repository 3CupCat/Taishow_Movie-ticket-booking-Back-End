package org.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.web.service.BonusService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bonus")
public class BonusController {

    @Autowired
    private BonusService bonusService;

    @GetMapping("/BonusPoints")
    public List<Map<String, Object>> getBonusPointDetails(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return bonusService.getBonusByToken(token);
    }
}
