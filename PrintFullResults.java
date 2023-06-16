package home.rom;

import java.util.ArrayList;

public class PrintFullResults {

    double f; // active slot coefficient
    int delta; // block propagation delay (measured in timeslots)
    long epochLength; // number of timeslots for simulation
    PraosStakeholders.StakeDistributionLaw stakeDistribution; // type of stake distribution
    int numberOfStakeholders;

    public PrintFullResults() {
        this( 1.0/20,   // f - active slot coefficient
                10,        // delta
                100000,   //epoch length in timeslots
                PraosStakeholders.StakeDistributionLaw.FLAT,
                100       // number of stakeholders
                );
    }

    public PrintFullResults( double f, // active slot coefficient
                                 int delta, // block propagation delay (measured in timeslots)
                                 long epochLength, // number of timeslots for simulation
                                 PraosStakeholders.StakeDistributionLaw stakeDistribution, // type of stake distribution
                                 int numberOfStakeholders
    ) {
        this.f = f; this.delta = delta; this.epochLength = epochLength; this.stakeDistribution = stakeDistribution;
        this. numberOfStakeholders = numberOfStakeholders;
    }

    void printResults() {
        PraosForksSimulation pf = new PraosForksSimulation( f,   // f - active slot coefficient
                delta,        // delta
                epochLength,   //epoch length in timeslots
                stakeDistribution,
                numberOfStakeholders       // number of stakeholders
        );
        ArrayList<Integer> forkStats = pf.getForkStats();

        System.out.printf("f = %.5f, delta = %d\n\n", pf.f, pf.delta);
        System.out.printf( "Maximal observed number of leaders per one slot: %d\n", pf.maxLeaders );
        System.out.printf( "For the worst network delays happening for the longest chain:\n" +
                        "  Actual observed number of active slot coefficient (f): %f\n  Slots per a block: %f " +
                        "(while the ideal average case with no forks: %f slots per a block)\n" +
                        "  Reached %2.1f%% of the ideal average throughput\n\n",
                pf.successfulBlockCount / (double )pf.staticEpochLen,
                pf.staticEpochLen / (double)pf.successfulBlockCount,
                1/pf.f,
                100 * (double)pf.successfulBlockCount / ( pf.f * pf.staticEpochLen)
        );

        System.out.printf( "For the prompt network sync for the forks happened:\n" +
                        "  Maximal observed fork length: %d\n" +
                        "  Total forks happened: %d (total blocks produced in the final longest chain: %d) \n" +
                        "  Ratio of the orphaned blocks to all blocks produced: %f " +
                        " (total orphaned blocks: %d)\n\n",
                pf.maxOrphanedForkLen,
                pf.forksHappened,
                pf.successfulBlockCount,
                pf.orphanedBlockCount /(double)(pf.successfulBlockCount + pf.orphanedBlockCount),
                pf.orphanedBlockCount
        );

        System.out.println("The number of forks : Observed share of all timeslots with such a number of forks");

        for(int i = 1; i < forkStats.size(); i++)
            System.out.printf( "%d : %.6f\n", i, forkStats.get( i ) / (double)PraosForksSimulation.staticEpochLen );
    }

}
