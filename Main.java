package home.rom;

public class Main {

    public static void main(String[] args) {
        PraosForksSimulation pf = new PraosForksSimulation();
        int[] forkStats = pf.getForkStats();

        System.out.printf("f = %.5f, delta = %d\n", PraosForksSimulation.f, PraosForksSimulation.delta);

        for(int i = 1; i < forkStats.length; i++)
            System.out.printf( "%d : %.6f\n", i, forkStats[ i ] / (double)PraosForksSimulation.staticEpochLen );
    }
}





