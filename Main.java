package home.rom;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

 //       new PrintFullResults().printResults();

        ParametersComparison pc = new ParametersComparison();

        FixedPropagationTimeSimulation fpts = new FixedPropagationTimeSimulation( 5 );
        fpts.specifiedAverageBlockGenerationTime.add( 10 ); // 10 seconds
        fpts.specifiedAverageBlockGenerationTime.add( 15 ); // 12 seconds
        fpts.specifiedAverageBlockGenerationTime.add( 20 ); // 24 seconds
        fpts.specifiedAverageBlockGenerationTime.add( 50 ); // 24 seconds

        fpts.specifiedF.add( 1.0/50 );
        fpts.specifiedF.add( 1.0/20 );
        fpts.specifiedF.add( 1.0/4 );
        fpts.specifiedF.add( 1.0/3  );

        pc.paramsAndProperties.add( fpts );

        fpts = new FixedPropagationTimeSimulation( 15 );
        fpts.specifiedAverageBlockGenerationTime.add( 20 ); // 10 seconds
        fpts.specifiedAverageBlockGenerationTime.add( 30 ); // 12 seconds
        fpts.specifiedAverageBlockGenerationTime.add( 60 ); // 24 seconds


        fpts.specifiedF.add( 1.0/50 );
        fpts.specifiedF.add( 1.0/20 );
        fpts.specifiedF.add( 1.0/12 );
        fpts.specifiedF.add( 1.0/3  );

        pc.paramsAndProperties.add( fpts );

        pc.printComparison();

    }
}





