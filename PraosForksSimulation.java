package home.rom;

public class PraosForksSimulation {
    static final int delta = 2;
    static final double f = 2.0/3;
    static final long staticEpochLen = 100000;
    CyclicBuffer cb;

    int [] forkStats;

    public PraosForksSimulation() {
        forkStats = new int[ delta + 1 ];
        cb = new CyclicBuffer();
    }

    public int[] getForkStats() {
        for(long i = 0; i < staticEpochLen; i++)
            if ( Math.random() < f ) forkStats[ cb.getNumberOfBranchesWithSlotAdvance( 1 ) ]++;
            else forkStats[ cb.getNumberOfBranchesWithSlotAdvance( 0 ) ]++;

        return forkStats;
    }


}

class CyclicBuffer {
    int [] buffer;
    int currentSlot = 0;
    int branches = 0;

    public CyclicBuffer (){
        buffer = new int[PraosForksSimulation.delta + 1];
    }

    public int getNumberOfBranchesWithSlotAdvance(int isActiveSlot) {
        int nextSlot = (currentSlot + 1) % ( PraosForksSimulation.delta + 1 );
        branches -= buffer[ nextSlot ];
        buffer[ nextSlot ] = 0;
        buffer[ currentSlot ] = isActiveSlot;
        branches += isActiveSlot;
        currentSlot = nextSlot;

        if ( branches < 2 ) return 1;
        else return branches;
    }
}