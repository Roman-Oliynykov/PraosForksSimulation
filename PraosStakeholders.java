package home.rom;

public class PraosStakeholders {
    public enum StakeDistributionLaw { FLAT, NORMAL, WHALE_NORMAL }
    static final int defaultNumberOfStakeholders = 100;

    double f;

    int numberOfStakeholders = defaultNumberOfStakeholders;

    double [] stakeDistribution;
    double [] phiDistribution;

    public int maxLeaders = 0;

    public PraosStakeholders() {
        this( StakeDistributionLaw.FLAT, (double)0.05, defaultNumberOfStakeholders );
    }

    public PraosStakeholders(StakeDistributionLaw sd, double fInit, int numberOfStakehldrs) {
        numberOfStakeholders = numberOfStakehldrs;
        f = fInit;

        stakeDistribution = new double[ numberOfStakeholders ];

        switch ( sd ){
            case FLAT: {
                for(int i = 0; i < numberOfStakeholders; i++)
                    stakeDistribution[ i ] = 1/(double) numberOfStakeholders;

                break;
            }
            case NORMAL: case WHALE_NORMAL: {
                double gaussianExpectation = numberOfStakeholders / 2;
                double gaussianDeviation;
                if ( sd == StakeDistributionLaw.NORMAL ) gaussianDeviation = numberOfStakeholders / 6.0;
                else gaussianDeviation = numberOfStakeholders / 20;

                for(int i = 0; i < numberOfStakeholders; i++)
                    stakeDistribution[ i ] = Gaussian.pdf( i, gaussianExpectation, gaussianDeviation );

                break;
            }
        }

        phiDistribution = new double[ numberOfStakeholders ];

        for(int i = 0; i < numberOfStakeholders; i++)
            phiDistribution[ i ] = phi( stakeDistribution[ i ] );
    }

    double phi(double alpha) {
        return 1 - Math.pow(1 - f, alpha);
    }

    public int getNumberOfSlotLeaders() {
        int slotLeaders = 0;

        for (int i = 0; i < numberOfStakeholders; i++)
            if ( Math.random() <  phiDistribution[ i ] ) slotLeaders++;
//            if ( Math.random() < phi( stakeDistribution[ i ] ) ) slotLeaders++;

        if ( maxLeaders < slotLeaders ) maxLeaders = slotLeaders;

        return slotLeaders;
    }
}
