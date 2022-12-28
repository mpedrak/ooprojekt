package agh;

import java.util.List;

public class InputConfiguration {
    static class InvalidConfigurationException extends IllegalArgumentException {}

    final int mapWidth, mapHeight, numOfAnimals, numOfGrass, startEnergy, grassEnergy,
            reproductionEnergyLoss, reproductionEnergyThreshold, numOfGenes, dailyGrass;
    final int[] scopeOfMutations;
    final boolean earthGlobe, equatorialForests, fullRandom, craziness;

    public InputConfiguration (int mapWidth, int mapHeight, int numOfAnimals, int numOfGrass, int startEnergy,
                               int grassEnergy, int reproductionEnergyLoss, int reproductionEnergyThreshold,
                               int numOfGenes, int dailyGrass, int[] scopeOfMutations, boolean earthGlobe,
                               boolean equatorialForests, boolean fullRandom, boolean craziness)
    {
        this.mapWidth= mapWidth;
        this.mapHeight= mapHeight;
        this.numOfAnimals= numOfAnimals;
        this.numOfGrass= numOfGrass;
        this.startEnergy= startEnergy;
        this.grassEnergy= grassEnergy;
        this.reproductionEnergyLoss= reproductionEnergyLoss;
        this.reproductionEnergyThreshold= reproductionEnergyThreshold;
        this.numOfGenes= numOfGenes;
        this.dailyGrass= dailyGrass;
        this.scopeOfMutations= scopeOfMutations;
        this.earthGlobe= earthGlobe;
        this.equatorialForests= equatorialForests;
        this.fullRandom= fullRandom;
        this.craziness= craziness;

        if (mapWidth < 3 || mapWidth > 50 || mapHeight < 3 || mapHeight > 50)
            throw new InvalidConfigurationException();

        if (numOfAnimals < 0 || numOfAnimals > mapHeight * mapWidth)
            throw new InvalidConfigurationException();

        if (numOfGrass < 0 || numOfGrass > mapHeight * mapWidth)
            throw new InvalidConfigurationException();

        if (startEnergy < 1 || startEnergy > 2000000000)
            throw new InvalidConfigurationException();

        if (grassEnergy < 0 || grassEnergy > 2000000000)
            throw new InvalidConfigurationException();

        //if (reproductionEnergyLoss < 1)
            //throw new InvalidConfigurationException();

        // TODO: Reszta checków



    }
}
