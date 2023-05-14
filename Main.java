package home.rom;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        PraosForksSimulation pf = new PraosForksSimulation();
        ArrayList<Integer> forkStats = pf.getForkStats();

        System.out.printf("f = %.5f, delta = %d\n", PraosForksSimulation.f, PraosForksSimulation.delta);

        for(int i = 1; i < forkStats.size(); i++)
            System.out.printf( "%d : %.6f\n", i, forkStats.get( i ) / (double)PraosForksSimulation.staticEpochLen );

    }
}





