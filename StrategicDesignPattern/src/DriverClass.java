import utility.strategy.StrategyConditionClass;
import utility.interfaces.Student;

public class DriverClass {

    private static final StrategyConditionClass strategyConditionclass = new StrategyConditionClass();

    public static void decide(String someCondition) {
        try{
            Student student = strategyConditionclass.getStrategy(someCondition)
                    .orElseThrow(IllegalArgumentException::new);
            student.apply();

        }catch (IllegalArgumentException e)
        {
            System.out.println("Enter correct condition for "+someCondition);
            e.printStackTrace();
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
