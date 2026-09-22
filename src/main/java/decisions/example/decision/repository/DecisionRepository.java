package decisions.example.decision.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import decisions.example.decision.entity.Decision;

public interface DecisionRepository extends JpaRepository<Decision, UUID> {

}