package home.rom;

import java.util.ArrayList;

public class PraosForksSimulation {
    public static int delta = 1;
    public static double f = 2.0/3;
    public static long staticEpochLen = 100000;

    int numberOfStakeholders = 100;

    PraosStakeholders.StakeDistributionLaw stakeDistribution = PraosStakeholders.StakeDistributionLaw.FLAT;

    public int maxLeaders = 0;
    public int successfulBlockCount = 0;
    public int maxOrphanedForkLen = 0;
    public int forksHappened = 0;
    public int orphanedBlockCount = 0;

    CyclicBuffer cb;

    int [] forkStats;

    public PraosForksSimulation( double f, // active slot coefficient
                                 int delta, // block propagation delay (measured in timeslots)
                                 long epochLength, // number of timeslots for simulation
                                 PraosStakeholders.StakeDistributionLaw stakeDistribution, // type of stake distribution
                                 int numberOfStakeholders
                                ) {
        this.f = f;
        this.delta = delta;
        this.staticEpochLen =epochLength;
        this.stakeDistribution = stakeDistribution;
        this.numberOfStakeholders = numberOfStakeholders;

        forkStats = new int[ numberOfStakeholders*(delta + 1) ];
        cb = new CyclicBuffer();
    }

//    public PraosForksSimulation() {
//        forkStats = new int[ 100*(delta + 1) ];
//        cb = new CyclicBuffer();
//    }

    public ArrayList<Integer> getForkStats() {
        PraosStakeholders stakeHolders = new PraosStakeholders( stakeDistribution,
                                                                f,
                                                                numberOfStakeholders );

        for(long i = 0; i < staticEpochLen; i++)
            forkStats[ cb.getNumberOfBranchesWithSlotAdvance( stakeHolders.getNumberOfSlotLeaders() ) ]++;

        this.maxLeaders = stakeHolders.maxLeaders;
        this.successfulBlockCount = cb.successfulBlockCnt;
        this.maxOrphanedForkLen = cb.maxOrphanedForkLen;
        this.forksHappened = cb.forksHappened;
        this.orphanedBlockCount = cb.orphanedBlockCount;


        int nonZeroElementCnt = 1;
        for(int i = forkStats.length - 1; i > 0; i--) {
            if ( forkStats[ i ] == 0 ) continue;
            nonZeroElementCnt = i + 1;
            break;
        }

        ArrayList<Integer> result = new ArrayList<>();
        for(int i = 0; i < nonZeroElementCnt; i++)
            result.add( forkStats[ i ] );

        return result;
    }
}

