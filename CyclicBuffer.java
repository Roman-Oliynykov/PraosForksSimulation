package home.rom;

public class CyclicBuffer {
    int [] buffer;
    int currentSlot = 0;
    int branches = 0;
    public int successfulBlockCnt = 0; // total blocks added to the longest chain

    int slotsAfterLastSuccessfulBlock = 0;

    boolean forksPresent = false;
    int orphanedForkLen = 0;
    public int maxOrphanedForkLen = 0;
    public int forksHappened = 0;

    public CyclicBuffer (){
        buffer = new int[PraosForksSimulation.delta + 1];
    }

    public int getNumberOfBranchesWithSlotAdvance(int numberOfSlotLeaders) {

        int nextSlot = (currentSlot + 1) % ( PraosForksSimulation.delta + 1 );
        branches -= buffer[ nextSlot ];
        buffer[ nextSlot ] = 0;
        buffer[ currentSlot ] = numberOfSlotLeaders;
        branches += numberOfSlotLeaders;
        currentSlot = nextSlot;

        ++slotsAfterLastSuccessfulBlock;

        boolean longestChainExtended = false;
        if ( (numberOfSlotLeaders > 0) && (slotsAfterLastSuccessfulBlock > PraosForksSimulation.delta) ) {
            ++successfulBlockCnt;
            longestChainExtended = true;
            slotsAfterLastSuccessfulBlock = 0;
        }

        if ( branches < 2 ) {
            if ( maxOrphanedForkLen < orphanedForkLen )
                maxOrphanedForkLen = orphanedForkLen;
            orphanedForkLen = 0;
            forksPresent = false;
        } else forksPresent = true;

// Worst case scenario for forks:
// if there are several multiple slot leaders, all of them extend only one branch and create as many forks as can happen
        if ( numberOfSlotLeaders > 1 )
            forksHappened += ( longestChainExtended ? (numberOfSlotLeaders - 1) : numberOfSlotLeaders );

// Counting the length of the current longest orphaned branch
        if ( forksPresent ) {
            if ( numberOfSlotLeaders > 1 ) orphanedForkLen++;
            else if ( (numberOfSlotLeaders == 1) && !longestChainExtended ) orphanedForkLen++;
        }

        if ( branches < 2 ) return 1;
        else return branches;
    }
}
