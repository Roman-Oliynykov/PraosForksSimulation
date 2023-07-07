package home.rom;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

 //       new PrintFullResults().printResults();

        ParametersComparison pc = new ParametersComparison( 100000,
                PraosStakeholders.StakeDistributionLaw.FLAT,
                100 );


        FixedPropagationTimeSimulation fpts = new FixedPropagationTimeSimulation( 5 );
        fpts.specifiedAverageBlockGenerationTime.add( 15 ); // 15 sec
        fpts.specifiedAverageBlockGenerationTime.add( 20 );
        fpts.specifiedAverageBlockGenerationTime.add( 50 );
        fpts.specifiedAverageBlockGenerationTime.add( 90 );

        fpts.specifiedF.add( 1.0/20 );
        fpts.specifiedF.add( 1.0/4 );
        fpts.specifiedF.add( 1.0/3 );
        fpts.specifiedF.add( 1.0/2 );

        pc.paramsAndProperties.add( fpts );

        fpts = new FixedPropagationTimeSimulation( 15 );
        fpts.specifiedAverageBlockGenerationTime.add( 25 );
        fpts.specifiedAverageBlockGenerationTime.add( 35 );
        fpts.specifiedAverageBlockGenerationTime.add( 50 );
        fpts.specifiedAverageBlockGenerationTime.add( 90 );

        fpts.specifiedF.add( 1.0/20 );
        fpts.specifiedF.add( 1.0/4 );
        fpts.specifiedF.add( 1.0/3 );
        fpts.specifiedF.add( 1.0/2 );

        pc.paramsAndProperties.add( fpts );

        pc.printComparison();
    }
}





