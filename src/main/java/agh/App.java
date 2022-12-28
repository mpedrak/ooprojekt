package agh;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileNotFoundException;


public class App  extends Application
{
    final int width = 25;
    final int height = 25;
    Vector2d bottomLeft;
    Vector2d upperRight;
    public void start(Stage primaryStage)
    {
        renderujStart(primaryStage);
    }

    public void renderuj(AbstractWorldMap map, GridPane grid, SimulationEngine engine, Animal zwierzeDoSledzenia)
    {
        grid.setGridLinesVisible(false);
        grid.getChildren().clear();
        grid.setGridLinesVisible(true);

        bottomLeft = map.getPoczatekMapy();
        upperRight = map.getKraniecMapy();

        Rectangle r = new Rectangle(width * Math.max(30, upperRight.x - bottomLeft.x  + 2) + 5, height * 5 + 2);
        r.setFill(Color.rgb(244, 244, 244,1));
        grid.add(r, 0, 0, 1, 1);

        Rectangle r2 = new Rectangle(width * (30 - upperRight.x - bottomLeft.x  + 2), height * (upperRight.y - bottomLeft.y + 4));
        r2.setFill(Color.rgb(244, 244, 244,1));
        grid.add(r2,  upperRight.x + 2 , (int)((upperRight.y - bottomLeft.y) / 2) + 3, 1, 1);

        Button button = new Button("Pauza ");
        grid.add(button, 0, 0, 5, 1);

        if(zwierzeDoSledzenia != null)
        {
            Label label = new Label(zwierzeDoSledzenia.toString());
            grid.add(label, 0, 1, 29, 1);
        }

        button.setOnAction(actionEvent ->
        {
            engine.pauza();
        });

        Label label7 = new Label(engine.toString());
        grid.add(label7, 0, 2, 29, 1);

        Button button2 = new Button("Zatrzymaj sledzenie");
        grid.add(button2, 3, 0, 5, 1);

        button2.setOnAction(actionEvent ->
        {
            engine.zmienZwierzeDoSledzenia(null);
        });

        VBox vbox = new VBox();
        Label s = new Label( "y\\x");
        vbox.getChildren().addAll(s);
        vbox.setAlignment(Pos.CENTER);
        grid.add(vbox, 0, 3, 1, 1);
        GridPane.setHalignment(vbox, HPos.CENTER);

        for (int i = bottomLeft.x ; i <= upperRight.x; ++i)
        {
            vbox = new VBox();
            s = new Label(Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.setAlignment(Pos.CENTER);
            grid.add(vbox, i - bottomLeft.x + 1, 3, 1, 1);
        }

        int j = 1;
        for (int i = upperRight.y; i >= bottomLeft.y; --i)
        {
            vbox = new VBox();
            s = new Label(Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.setAlignment(Pos.CENTER);
            grid.add(vbox, 0, j + 3, 1, 1);
            ++j;
        }

        for (int x = bottomLeft.x ; x <= upperRight.x ; ++x)
        {
            j = 1;
            for (int y = upperRight.y; y >= bottomLeft.y; --y)
            {
                Object z = map.objectAt(new Vector2d(x, y));
                if (z != null)
                {
                    GuiElementBox element = null;
                    try
                    {
                        element = new GuiElementBox((IMapElement)z);
                    }
                    catch (FileNotFoundException ex)
                    {
                        System.out.println(ex);
                    }
                    if (z instanceof Animal)
                    {
                        element.vbox.setOnMouseClicked(new EventHandler<Event>() {
                            @Override
                            public void handle(Event event)
                            {
                                Rectangle r = new Rectangle(width * Math.max(30, upperRight.x - bottomLeft.x  + 2) + 5, height);
                                r.setFill(Color.rgb(244, 244, 244,1));
                                grid.add(r, 0, 1, 1, 1);

                                engine.zmienZwierzeDoSledzenia((Animal)z);
                                Label label = new Label(z.toString());
                                grid.add(label, 0, 1, 29, 1);
                            }
                        });
                    }
                    grid.add(element.vbox, x - bottomLeft.x + 1, j + 3, 1, 1);
                    GridPane.setHalignment(element.vbox, HPos.CENTER);
                }
                ++j;
            }
        }

    }

    public GridPane renderujPierwszyRaz(AbstractWorldMap map)
    {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);

        bottomLeft = map.getPoczatekMapy();
        upperRight = map.getKraniecMapy();
        int pom = Math.max((upperRight.x - bottomLeft.x  + 2), 30);
        int w =  pom * width;
        int h = (upperRight.y - bottomLeft.y  + 5) * height;
        Scene scene = new Scene(grid, w, h);

        for (int i = 0; i <= pom - 1; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= upperRight.y - bottomLeft.y + 4; i++)
            grid.getRowConstraints().add(new RowConstraints(height));

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        return grid;
    }


    private void renderujStart(Stage primaryStage)
    {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);

        for (int i = 0; i <= 22; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= 25; i++)
            grid.getRowConstraints().add(new RowConstraints(height));

        Scene scene = new Scene(grid, 23 * width, 26 * height);

        primaryStage.setScene(scene);

        // Elementy GUI

        // # Wybór typu inputa

        final ToggleGroup settingsType= new ToggleGroup();
        RadioButton settingsRB1= new RadioButton("Gotowy template");
        RadioButton settingsRB2= new RadioButton("Własne ustawienia");
        grid.add(settingsRB1, 1, 1, 7, 1);
        grid.add(settingsRB2, 1, 4, 7, 1);
        settingsRB1.setToggleGroup(settingsType);
        settingsRB2.setToggleGroup(settingsType);
        settingsRB1.setSelected(true);

        // # Wybór customowych ustawień

        final ToggleGroup worldType= new ToggleGroup();
        Label worldTypeText= new Label("Typ świata");
        grid.add(worldTypeText, 2, 6, 10, 1);
        RadioButton earthGlobeRB= new RadioButton("Kula ziemska");
        RadioButton netherPortalRB= new RadioButton("Piekielny portal");
        grid.add(earthGlobeRB, 3, 7, 5, 1);
        grid.add(netherPortalRB, 9, 7, 5, 1);
        earthGlobeRB.setToggleGroup(worldType);
        netherPortalRB.setToggleGroup(worldType);
        earthGlobeRB.setUserData(true);
        netherPortalRB.setUserData(false);

        final ToggleGroup growthType= new ToggleGroup();
        Label growthTypeText= new Label("Preferencje wzrostu roślin");
        grid.add(growthTypeText, 2, 8, 10, 1);
        RadioButton equatorialForestRB= new RadioButton("Zalesione równiki");
        RadioButton toxicCorpsesRB= new RadioButton("Toksyczne trupy");
        grid.add(equatorialForestRB, 3, 9, 5, 1);
        grid.add(toxicCorpsesRB, 9, 9, 5, 1);
        equatorialForestRB.setToggleGroup(growthType);
        toxicCorpsesRB.setToggleGroup(growthType);
        equatorialForestRB.setUserData(true);
        toxicCorpsesRB.setUserData(false);

        final ToggleGroup mutationType= new ToggleGroup();
        Label mutationTypeText= new Label("Przebieg mutacji");
        grid.add(mutationTypeText, 2, 10, 10, 1);
        RadioButton fullRandomRB= new RadioButton("Pełna losowość");
        RadioButton smallCorrectionRB= new RadioButton("Lekka korekta");
        grid.add(fullRandomRB, 3, 11, 5, 1);
        grid.add(smallCorrectionRB, 9, 11, 5, 1);
        fullRandomRB.setToggleGroup(mutationType);
        smallCorrectionRB.setToggleGroup(mutationType);
        fullRandomRB.setUserData(true);
        smallCorrectionRB.setUserData(false);

        final ToggleGroup behaviorType= new ToggleGroup();
        Label behaviorTypeText= new Label("Zachowanie zwierząt");
        grid.add(behaviorTypeText, 2, 12, 10, 1);
        RadioButton fullDestinyRB= new RadioButton("Pełna predestynacja");
        RadioButton littleCrazinessRB= new RadioButton("Nieco szaleństwa");
        grid.add(fullDestinyRB, 3, 13, 5, 1);
        grid.add(littleCrazinessRB, 9, 13, 5, 1);
        fullDestinyRB.setToggleGroup(behaviorType);
        littleCrazinessRB.setToggleGroup(behaviorType);
        fullDestinyRB.setUserData(false);
        littleCrazinessRB.setUserData(true);

        Label dimensionsText= new Label("Wymiary planszy:");
        TextField widthInput= new TextField();
        TextField heightInput= new TextField();
        grid.add(dimensionsText, 2, 14, 5, 1);
        grid.add(widthInput, 7, 14, 2, 1);
        grid.add(heightInput, 10, 14, 2, 1);
        widthInput.setPromptText("x");
        heightInput.setPromptText("y");
        Label letterXText= new Label("   x");
        grid.add(letterXText, 9, 14, 1, 1);

        Label grassText= new Label("Rośliny:");
        grid.add(grassText, 12, 16, 5, 1);

        Label numOfGrassText= new Label("Początkowa ilość:");
        grid.add(numOfGrassText, 13, 17, 5, 1);
        TextField numOfGrassInput= new TextField();
        grid.add(numOfGrassInput, 18, 17, 2, 1);
        Label dailyGrowthText= new Label("Dzienny przyrost:");
        grid.add(dailyGrowthText, 13, 18, 5, 1);
        TextField dailyGrowthInput= new TextField();
        grid.add(dailyGrowthInput, 18, 18, 2, 1);
        Label energyValueText= new Label("Wartość odżywcza:");
        grid.add(energyValueText, 13, 19, 5, 1);
        TextField energyValueInput= new TextField();
        grid.add(energyValueInput, 18, 19, 2, 1);

        Label animalText= new Label("Zwierzęta:");
        grid.add(animalText, 2, 16, 5, 1);

        Label numOfAnimalsText= new Label("Początkowa ilość:");
        grid.add(numOfAnimalsText, 3, 17, 5, 1);
        TextField numOfAnimalsInput= new TextField();
        grid.add(numOfAnimalsInput, 8, 17, 2, 1);
        Label startEnergyText= new Label("Początkowa energia:");
        grid.add(startEnergyText, 3, 18, 5, 1);
        TextField startEnergyInput= new TextField();
        grid.add(startEnergyInput, 8, 18, 2, 1);
        Label reproductionEnergyLossText= new Label("Energia zużyta przy rozmnażaniu:");
        grid.add(reproductionEnergyLossText, 3, 19, 5, 1);
        TextField reproductionEnergyLossInput= new TextField();
        grid.add(reproductionEnergyLossInput, 8, 19, 2, 1);
        Label reproductionEnergyThresholdText= new Label("Minimalna energia do rozmnażania:");
        grid.add(reproductionEnergyThresholdText, 3, 20, 5, 1);
        TextField reproductionEnergyThresholdInput= new TextField();
        grid.add(reproductionEnergyThresholdInput, 8, 20, 2, 1);

        Label numOfGenesText= new Label("Długość genomu:");
        grid.add(numOfGenesText, 3, 21, 5, 1);
        TextField numOfGenesInput= new TextField();
        grid.add(numOfGenesInput, 8, 21, 2, 1);
        Label scopeMutationsText= new Label("Zakres liczby mutacji:");
        grid.add(scopeMutationsText, 3, 22, 8, 1);
        TextField scopeFromInput= new TextField();
        grid.add(scopeFromInput, 12, 22, 2, 1);
        Label dashText= new Label("   -");
        grid.add(dashText, 14, 22, 1, 1);
        TextField scopeToInput= new TextField();
        grid.add(scopeToInput, 15, 22, 2, 1);
        scopeFromInput.setPromptText("min");
        scopeToInput.setPromptText("max");

        BooleanBinding buttonShouldBeDisabled = new BooleanBinding() {
            {
                super.bind(settingsType.selectedToggleProperty(), worldType.selectedToggleProperty(),
                        growthType.selectedToggleProperty(), mutationType.selectedToggleProperty(),
                        behaviorType.selectedToggleProperty(), widthInput.textProperty(), heightInput.textProperty(),
                        numOfGrassInput.textProperty(), dailyGrowthInput.textProperty(),
                        energyValueInput.textProperty(), numOfAnimalsInput.textProperty(),
                        startEnergyInput.textProperty(), reproductionEnergyLossInput.textProperty(),
                        reproductionEnergyThresholdInput.textProperty(), numOfGenesInput.textProperty(),
                        scopeFromInput.textProperty(), scopeToInput.textProperty()
                );
            }
            @Override
            protected boolean computeValue() {
                return settingsType.getSelectedToggle()==settingsRB2 && (worldType.getSelectedToggle()==null ||
                        growthType.getSelectedToggle()==null || mutationType.getSelectedToggle()==null ||
                        behaviorType.getSelectedToggle()==null || !isInputValid(widthInput.getText(),
                        heightInput.getText(), numOfGrassInput.getText(), dailyGrowthInput.getText(), energyValueInput.getText(),
                        numOfAnimalsInput.getText(), startEnergyInput.getText(), reproductionEnergyLossInput.getText(),
                        reproductionEnergyThresholdInput.getText(), numOfGenesInput.getText(),
                        scopeFromInput.getText(), scopeToInput.getText()));
            }
        };

        // # Przycisk startowy

        Button button = new Button("Start symulacji");
        grid.add(button, 11, 2, 5, 1);
        button.disableProperty().bind(buttonShouldBeDisabled);

        button.setOnAction(actionEvent ->
        {
            InputConfiguration data;
//            InputConfiguration data= setConfiguration(worldType, growthType, mutationType, behaviorType);

            // TODO: będzie robione
//            if (settingsType.getSelectedToggle() == settingsRB2) {    // jeśli wybrano ustawienia customowe
//                System.out.println("custom!");
//                //data= setConfiguration(worldType, growthType, mutationType, behaviorType);
//            }
//            else
                data= new InputConfiguration(13, 13, 10, 10,
                        50, 0, 10, 20, 10,
                        17, new int[]{3, 3}, false, true, true, false);

            System.out.println("Czy kula ziemska: " + data.earthGlobe);
            System.out.println("Czy równik: " + data.equatorialForests);
            System.out.println("Czy losowa mutacja: " + data.fullRandom);
            System.out.println("Szaleństwo: " + data.craziness);

            startSimulation(data);

//            AbstractWorldMap map = new KulaZiemska(13, 13,  10, 1, true);
//            int e = -5; // energiaTraconaPrzyTeleportacji = naRozmnażanie !
//            // AbstractWorldMap map = new PiekielnyPortal(10, 10,  10, 20, false, e);
//            int moveDelay = 500;
//            Runnable engine = new SimulationEngine(map, this, moveDelay, 10, 25,
//                    e, -2 * e, 10, new int[]{3, 3},
//                    true, true, 1);
//
//            Thread engineThread = new Thread(engine);
//            engineThread.start();
        });

        primaryStage.show();
    }

    private final boolean isInputValid(String widthInput,
                                       String heightInput, String numOfGrassInput, String dailyGrowthInput, String energyValueInput,
                                       String numOfAnimalsInput, String startEnergyInput, String reproductionEnergyLossInput,
                                       String reproductionEnergyThresholdInput, String numOfGenesInput,
                                       String scopeFromInput, String scopeToInput)
                                                        // function returns true if all input is valid, otherwise false
    {
        boolean allFilled= !( widthInput.isEmpty() || heightInput.isEmpty() || numOfGrassInput.isEmpty() ||
                            dailyGrowthInput.isEmpty() || energyValueInput.isEmpty() || numOfAnimalsInput.isEmpty() ||
                            startEnergyInput.isEmpty() || reproductionEnergyLossInput.isEmpty() ||
                            reproductionEnergyThresholdInput.isEmpty() || numOfGenesInput.isEmpty() ||
                            scopeFromInput.isEmpty() || scopeToInput.isEmpty() );

        if (!allFilled)
            return false;

        int num;

        return false;

    }


    private void startSimulation (InputConfiguration x) {
        AbstractWorldMap map;
        //int e = -5; // energiaTraconaPrzyTeleportacji = naRozmnażanie !

        if (x.earthGlobe)
            map= new KulaZiemska(x.mapWidth, x.mapHeight,  x.numOfGrass, x.grassEnergy, x.equatorialForests);


        else
            map= new PiekielnyPortal(x.mapWidth, x.mapHeight,  x.numOfGrass, x.grassEnergy, x.equatorialForests, x.reproductionEnergyLoss);

        int moveDelay = 500;
        Runnable engine = new SimulationEngine(map, this, moveDelay, x.numOfAnimals, x.startEnergy,
                -x.reproductionEnergyLoss, x.reproductionEnergyThreshold, x.numOfGenes, x.scopeOfMutations,
                x.fullRandom, x.craziness, x.dailyGrass);

        Thread engineThread = new Thread(engine);
        engineThread.start();
    }



}
