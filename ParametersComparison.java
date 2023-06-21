package home.rom;

import java.util.ArrayList;

public class ParametersComparison {
    long epochLength; // number of timeslots for simulation
    PraosStakeholders.StakeDistributionLaw stakeDistribution; // type of stake distribution
    int numberOfStakeholders;

    ArrayList<FixedPropagationTimeSimulation> paramsAndProperties = new ArrayList<>();

    public ParametersComparison() {
        this( 100000, PraosStakeholders.StakeDistributionLaw.FLAT,100 );
    }


    public ParametersComparison(long epochLength, PraosStakeholders.StakeDistributionLaw stakeDistributionLaw,
                                int numberOfStakeholders) {
        this.epochLength = epochLength; this.stakeDistribution = stakeDistributionLaw;
        this.numberOfStakeholders = numberOfStakeholders;
    }

    public void printComparison() {

        for(int i = 0; i < paramsAndProperties.size(); i++) {
            paramsAndProperties.get( i ).setParams();
            paramsAndProperties.get( i ).calculateProperties( epochLength, stakeDistribution, numberOfStakeholders );
            paramsAndProperties.get( i ).printResultsInTable();
        }

    }
}


class ParamsAndProperties {
    // parameters
    double f;
    int delta;

    double timeSlot; // in seconds

    // properties
    double realF;
    double realBlockTime;
    double orphanedBlockShare;
    int maxOrphanedForkLen;

    private ParamsAndProperties() {}
    public ParamsAndProperties( double f ) {
        this.f = f;
    }
}

class FixedPropagationTimeSimulation {
    int specifiedPropagationTime; // in seconds

    ArrayList<Integer> specifiedAverageBlockGenerationTime = new ArrayList<>(); // in seconds
    ArrayList<Double>  specifiedF = new ArrayList<>(); // active slot coefficient values to be considered

    ArrayList<ArrayList<ParamsAndProperties>> paramsAndProperties;

    private FixedPropagationTimeSimulation() {}

    public FixedPropagationTimeSimulation(int specifiedPropagationTime /* in seconds */ ) {
        this.specifiedPropagationTime = specifiedPropagationTime;
    }

    void setParams() {
        paramsAndProperties = new ArrayList<>();

        for(int i = 0; i < specifiedAverageBlockGenerationTime.size(); i++) {
            ArrayList<ParamsAndProperties> lst = new ArrayList<>();

            for(int j = 0; j < specifiedF.size(); j++){
                ParamsAndProperties praosParams = new ParamsAndProperties( specifiedF.get( j ) );
                praosParams.timeSlot = specifiedAverageBlockGenerationTime.get( i ) * praosParams.f;
                praosParams.delta = (int) Math.ceil( specifiedPropagationTime / (double) praosParams.timeSlot );

                lst.add( praosParams );
            }

            paramsAndProperties.add( lst );
        }
    }

    void calculateProperties(   long epochLength, // number of timeslots for simulation
                                PraosStakeholders.StakeDistributionLaw stakeDistribution, // type of stake distribution
                                int numberOfStakeholders) {

        for (int i = 0; i < specifiedAverageBlockGenerationTime.size(); i++) {
            for(int j = 0; j < specifiedF.size(); j++) {
                PraosForksSimulation pf = new PraosForksSimulation(  paramsAndProperties.get(i).get(j).f,   // f - active slot coefficient
                        paramsAndProperties.get(i).get(j).delta,        // delta
                        epochLength,   //epoch length in timeslots
                        stakeDistribution,
                        numberOfStakeholders       // number of stakeholders
                );
                pf.getForkStats();

                paramsAndProperties.get(i).get(j).realBlockTime =
                        paramsAndProperties.get(i).get(j).timeSlot * pf.staticEpochLen / (double)pf.successfulBlockCount;
                paramsAndProperties.get(i).get(j).orphanedBlockShare =
                        pf.orphanedBlockCount /(double)(pf.successfulBlockCount + pf.orphanedBlockCount);
                paramsAndProperties.get(i).get(j).maxOrphanedForkLen = pf.maxOrphanedForkLen;

            }
        }

    }

    void printResultsInTable() {
        System.out.println( "Block propagation time over the network: " + specifiedPropagationTime + " sec" );

        System.out.print("-----------------------------");
        for(int i = 0; i < specifiedF.size(); i++)
            System.out.print("------------------------------");
        System.out.println();

        System.out.print(" Specified block time (sec) |");
        for(int i = 0; i < specifiedF.size(); i++)
            System.out.printf("%29.3f|", specifiedF.get( i ) );
        System.out.println();

        System.out.print("-----------------------------");
        for(int i = 0; i < specifiedF.size(); i++)
            System.out.print("------------------------------");
        System.out.println();

        for (int i = 0; i < specifiedAverageBlockGenerationTime.size(); i++) {
            System.out.printf("%27d |", specifiedAverageBlockGenerationTime.get( i ) );
            for(int j = 0; j < specifiedF.size(); j++)
                System.out.printf("%22s %5.2f |", " Timeslot (sec):", paramsAndProperties.get(i)
                        .get(j).timeSlot  );

            System.out.println();

            System.out.printf("%27s |", "" );
            for(int j = 0; j < specifiedF.size(); j++)
                System.out.printf("%23s %4d |", " Delta:", paramsAndProperties.get(i)
                        .get(j).delta  );
            System.out.println();


            System.out.printf("%27s |", "" );
            for(int j = 0; j < specifiedF.size(); j++)
                System.out.printf("%21s %6.2f |", " Real avg block time:", paramsAndProperties.get(i)
                                                            .get(j).realBlockTime  );
            System.out.println();

            System.out.printf("%27s |", "" );
            for(int j = 0; j < specifiedF.size(); j++)
                System.out.printf("%23s %4.2f |", " Orphaned blocks:", paramsAndProperties.get(i)
                        .get(j).orphanedBlockShare  );
            System.out.println();

            System.out.printf("%27s |", "" );
            for(int j = 0; j < specifiedF.size(); j++)
                System.out.printf("%23s %4d |", " Max fork len:", paramsAndProperties.get(i)
                        .get(j).maxOrphanedForkLen  );
            System.out.println();


            System.out.print("-----------------------------");
            for(int t = 0; t < specifiedF.size(); t++)
                System.out.print("------------------------------");
            System.out.println();


        }

        System.out.println();

    }

}