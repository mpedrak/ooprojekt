package agh;

import java.util.Map;
import java.util.NoSuchElementException;

public class InputConfiguration {
    static class InvalidConfigurationException extends IllegalArgumentException {
        InvalidConfigurationException (String message) {
            super(message);
        }
    }

    final int mapWidth, mapHeight, numOfAnimals, numOfGrass, startEnergy, grassEnergy,
            reproductionEnergyLoss, reproductionEnergyThreshold, numOfGenes, dailyGrass;
    final int[] scopeOfMutations;
    final boolean earthGlobe, equatorialForests, fullRandom, craziness;
    final int moveDelay;

    public InputConfiguration (int mapWidth, int mapHeight, int numOfAnimals, int numOfGrass, int startEnergy,
                               int grassEnergy, int reproductionEnergyLoss, int reproductionEnergyThreshold,
                               int numOfGenes, int dailyGrass, int[] scopeOfMutations, boolean earthGlobe,
                               boolean equatorialForests, boolean fullRandom, boolean craziness, int moveDelay)
    {
        if (mapWidth < 5 || mapWidth > 70 || mapHeight < 5 || mapHeight > 33)
            throw new InvalidConfigurationException("Bledne wymiary!");

        if (numOfAnimals < 0 || numOfAnimals > mapHeight * mapWidth)
            throw new InvalidConfigurationException("Bledna liczba zwierzat!");

        if (numOfGrass < 0 || numOfGrass > mapHeight * mapWidth)
            throw new InvalidConfigurationException("Bledna poczatkowa liczba roslin!");

        if (startEnergy < 1 || startEnergy > 1000000000)
            throw new InvalidConfigurationException("Bledna poczatkowa energia zwierzat!");

        if (grassEnergy < 0 || grassEnergy > 1000)
            throw new InvalidConfigurationException("Bledna wartosc kaloryczna roslin!");

        if (reproductionEnergyLoss < 1)
            throw new InvalidConfigurationException("Bledny koszt energetyczny rozmnazania!");

        if (reproductionEnergyThreshold < 0)
            throw new InvalidConfigurationException("Bledny prog energetyczny rozmnazania!");

        if (reproductionEnergyLoss >= reproductionEnergyThreshold)
            throw new InvalidConfigurationException("Koszt energii rozmnazania jest wiekszy od progu!");

        if (numOfGenes < 1 || numOfGenes > 1000)
            throw new InvalidConfigurationException("Bledna dlugosc genotypu!");

        if (dailyGrass < 0 || dailyGrass > mapHeight * mapWidth)
            throw new InvalidConfigurationException("Bledna wartosc przyrostu trawy!");

        if (scopeOfMutations.length != 2 || scopeOfMutations[0] < 0 || scopeOfMutations[1] < 0 ||
            scopeOfMutations[0] > numOfGenes || scopeOfMutations[1] > numOfGenes ||
                scopeOfMutations[0] > scopeOfMutations[1])
        {
            throw new InvalidConfigurationException("Bledny zakres ilosci mutacji w genotypie!");
        }

        if (moveDelay < 50 || moveDelay > 10000)
            throw new InvalidConfigurationException("Bledna dlugosc dnia!");


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
        this.moveDelay= moveDelay;
    }

    static InputConfiguration makeFromMap (Map<String, String> input, int moveDelay) {
        try {
            int mapWidth = Integer.parseInt(Utils.getOrThrow("mapWidth", input));
            int mapHeight = Integer.parseInt(Utils.getOrThrow("mapHeight", input));
            int numOfAnimals = Integer.parseInt(Utils.getOrThrow("numOfAnimals", input));
            int numOfGrass = Integer.parseInt(Utils.getOrThrow("numOfGrass", input));
            int startEnergy = Integer.parseInt(Utils.getOrThrow("startEnergy", input));
            int grassEnergy = Integer.parseInt(Utils.getOrThrow("grassEnergy", input));
            int reproductionEnergyLoss = Integer.parseInt(Utils.getOrThrow("reproductionEnergyLoss", input));
            int reproductionEnergyThreshold = Integer.parseInt(Utils.getOrThrow("reproductionEnergyThreshold", input));
            int numOfGenes = Integer.parseInt(Utils.getOrThrow("numOfGenes", input));
            int dailyGrass = Integer.parseInt(Utils.getOrThrow("dailyGrass", input));
            int[] scopeOfMutations = new int[]{Integer.parseInt(Utils.getOrThrow("scopeOfMutations[0]", input)),
                    Integer.parseInt(Utils.getOrThrow("scopeOfMutations[1]", input))};

            boolean earthGlobe = Utils.parseBoolean(Utils.getOrThrow("earthGlobe", input));
            boolean equatorialForests = Utils.parseBoolean(Utils.getOrThrow("equatorialForests", input));
            boolean fullRandom = Utils.parseBoolean(Utils.getOrThrow("fullRandom", input));
            boolean craziness = Utils.parseBoolean(Utils.getOrThrow("craziness", input));

            return new InputConfiguration(mapWidth, mapHeight, numOfAnimals, numOfGrass, startEnergy, grassEnergy,
                    reproductionEnergyLoss, reproductionEnergyThreshold, numOfGenes, dailyGrass, scopeOfMutations, earthGlobe,
                    equatorialForests, fullRandom, craziness, moveDelay);
        } catch (NumberFormatException ex) {
            throw new InvalidConfigurationException("Nieprawidlowa wartosc!");
        } catch (NoSuchElementException ex) {
            throw new InvalidConfigurationException("Brak wartosci dla " + ex.getMessage() + "!");
        }
    }

}
