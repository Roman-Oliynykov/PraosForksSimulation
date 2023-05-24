package home.rom;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        PraosForksSimulation pf = new PraosForksSimulation( 1.0/20,   // f - active slot coefficient
                                                         5,        // delta
                                                   100000,   //epoch length in timeslots
                                                              PraosStakeholders.StakeDistributionLaw.FLAT,
                                            100       // number of stakeholders
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
//                "  Ratio of the number of forks happened per one block in the final longest chain: %f \n" +
                "  Ratio of the orphaned blocks to all blocks produced: %f " +
                " (total orphaned blocks: %d)\n\n",
                pf.maxOrphanedForkLen,
                pf.forksHappened,
                pf.successfulBlockCount,
//                pf.forksHappened / (double)pf.successfulBlockCount,
                pf.orphanedBlockCount /(double)(pf.successfulBlockCount + pf.orphanedBlockCount),
                pf.orphanedBlockCount
        );

        System.out.println("The number of forks : Observed share of all timeslots with such a number of forks");

        for(int i = 1; i < forkStats.size(); i++)
            System.out.printf( "%d : %.6f\n", i, forkStats.get( i ) / (double)PraosForksSimulation.staticEpochLen );

    }
}





