package home.rom;

public class CyclicBuffer {
    int [] buffer;
    int currentSlot = 0;
    int branches = 0;
    public int successfulBlockCnt = 0; // total blocks added to the longest chain

    int slotsAfterLastSuccessfulBlock = PraosForksSimulation.delta; // to make the first block join the longest chain, not a fork

    boolean forksPresent = false;
    int orphanedForkLen = 0;
    public int maxOrphanedForkLen = 0;
    public int forksHappened = 0;

    public CyclicBuffer (){
        buffer = new int[PraosForksSimulation.delta + 1];
    }

    public int getNumberOfBranchesWithSlotAdvance(int numberOfSlotLeaders) {

        int seenBranches = (branches == 0 ? 1 : branches);

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

// Scenario for forks:
// if there are several multiple slot leaders, each of them extend a separate existing chain
// or create a new fork if the number of slot leaders exceed the number of the currently existing chains
        if ( (numberOfSlotLeaders > 0) && (numberOfSlotLeaders >= seenBranches) )
            forksHappened += ( longestChainExtended ?
// all branches, including the longest (final) branch have been extended; extra leaders create new forks
                    (numberOfSlotLeaders - seenBranches) :
// the longest (final) branch has not been extended, all slot leaders create new forks if they cannot extend the existing ones
                    (numberOfSlotLeaders - seenBranches + 1 )
                    );


// Counting the length of the current longest orphaned branch
        if ( forksPresent ) {
            if ( numberOfSlotLeaders > 1 ) orphanedForkLen++;
            else if ( (numberOfSlotLeaders == 1) && !longestChainExtended ) orphanedForkLen++;
        }

        if ( branches < 2 ) return 1;
        else return branches;
    }
}
