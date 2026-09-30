package decisions.example.decision.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import decisions.example.decision.entity.Decision;
import decisions.example.decision.repository.DecisionRepository;

@RestController
@RequestMapping("/api/decisions")
public class DecisionController {

    private final DecisionRepository decisionRepository;

    public DecisionController(DecisionRepository decisionRepository) {
        this.decisionRepository = decisionRepository;
    }

    @GetMapping
    public List<Decision> getAllDecisions() {
        return decisionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Decision getDecisionById(@PathVariable UUID id) {
        return decisionRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Decision createDecision(@RequestBody Decision decision) {
        return decisionRepository.save(decision);
    }
}