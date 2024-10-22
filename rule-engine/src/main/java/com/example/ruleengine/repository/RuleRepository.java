package com.example.ruleengine.repository;

import com.example.ruleengine.model.Rule; // Assuming Rule is the entity class representing a rule
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {
    // JpaRepository provides built-in methods like save(), findById(), deleteById(), etc.
    
    // You can add custom query methods here if needed, like finding a rule by name or rule string.
}
