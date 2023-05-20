package home.rom;

public class CyclicBuffer {
    int [] buffer;
    int currentSlot = 0;
    int branches = 0;
    public int successfulBlockCnt = 0; // total blocks added to the longest chain

    int slotsAfterLastSuccessfulBlock = 0;

    boolean forksPresent = false;
//    int slotsAfterLastFork = 0;
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

            if ( forksPresent ) ++forksHappened;

            forksPresent = false;
        } else forksPresent = true;

        if ( forksPresent && ( numberOfSlotLeaders > 0) && !longestChainExtended ) {
            orphanedForkLen++;
//            slotsAfterLastFork++;
        }

        if ( branches < 2 ) return 1;
        else return branches;
    }
}
