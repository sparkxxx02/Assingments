import utility.strategy.StrategyConditionClass;
import utility.interfaces.Student;

public class DriverClass {

    private static final StrategyConditionClass strategyConditionclass = new StrategyConditionClass();

    public static String decide(String someCondition) {
        try{
            Student student = strategyConditionclass.getStrategy(someCondition)
                    .orElseThrow(IllegalArgumentException::new);
            return student.apply();

        }catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            return ("Enter correct condition for "+someCondition);
        }

    }
    public static void main(String []args)
    {
        decide("Ujjwal");
        //decide("Gargi");
        //decide("Mehul");
        decide("Nirala");
        //decide("Nishant");
        //decide("Satvik");
        //decide("Saurabh");

    }
}
