package utility.strategy;

import utility.*;
import utility.interfaces.Student;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StrategyConditionClass {
        private final Map<String, Student> conditions = new HashMap<>();

        public StrategyConditionClass() {
            conditions.put("Ujjwal", new Ujjwal());
            conditions.put("Mehul", new Mehul());
            conditions.put("Saurabh", new Saurabh());
            conditions.put("Nirala", new Nirala());
            conditions.put("Satvik", new Satvik());
            conditions.put("Nishant", new Nishant());
            conditions.put("Gargi", new Gargi());

        }
        public Optional<Student> getStrategy(String condition) {
            return Optional.ofNullable(conditions.get(condition));
        }

    }
