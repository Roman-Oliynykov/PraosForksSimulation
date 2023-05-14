package home.rom;

public class CyclicBuffer {
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
