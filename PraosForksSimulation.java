package home.rom;

import java.util.ArrayList;

public class PraosForksSimulation {
    static final int delta = 3;
    static final double f = 1.0/20;
    static final long staticEpochLen = 100000;

    public int maxLeaders = 0;
    public int successfulBlockCount = 0;
    public int maxOrphanedForkLen = 0;
    public int forksHappened = 0;

    CyclicBuffer cb;

    int [] forkStats;

    public PraosForksSimulation() {
        forkStats = new int[ 100*(delta + 1) ];
        cb = new CyclicBuffer();
    }

    public ArrayList<Integer> getForkStats() {
        PraosStakeholders stakeHolders = new PraosStakeholders( PraosStakeholders.StakeDistributionLaw.FLAT,
                                                                f,
                                                                PraosStakeholders.defaultNumberOfStakeholders );

        for(long i = 0; i < staticEpochLen; i++)
            forkStats[ cb.getNumberOfBranchesWithSlotAdvance( stakeHolders.getNumberOfSlotLeaders() ) ]++;

        this.maxLeaders = stakeHolders.maxLeaders;
        this.successfulBlockCount = cb.successfulBlockCnt;
        this.maxOrphanedForkLen = cb.maxOrphanedForkLen;
        this.forksHappened = cb.forksHappened;


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

