package agh;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;


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
        // stage.setResizable(false);

        return grid;
    }


    private void renderujStart(Stage primaryStage)
    {
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(false);

        for (int i = 0; i <= 22; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= 31; i++)
            grid.getRowConstraints().add(new RowConstraints(height));

        Scene scene = new Scene(grid, 22 * width, 32 * height);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

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
        Collection<Label> customLabels= new LinkedList<Label>();
        Collection<TextField> customTextFields= new LinkedList<TextField>();
        Collection<ToggleButton> customToggleButtons= new LinkedList<ToggleButton>();

        final ToggleGroup worldType= new ToggleGroup();
        Label worldTypeText= new Label("Typ świata");
        grid.add(worldTypeText, 2, 6, 6, 1);
        ToggleButton earthGlobeRB= new ToggleButton("Kula ziemska");
        ToggleButton netherPortalRB= new ToggleButton("Piekielny portal");
        grid.add(earthGlobeRB, 9, 6, 4, 1);
        grid.add(netherPortalRB, 13, 6, 4, 1);
        earthGlobeRB.setToggleGroup(worldType);
        netherPortalRB.setToggleGroup(worldType);
        earthGlobeRB.setUserData(true);
        netherPortalRB.setUserData(false);
        customLabels.add(worldTypeText);
        customToggleButtons.add(earthGlobeRB);
        customToggleButtons.add(netherPortalRB);

        final ToggleGroup growthType= new ToggleGroup();
        Label growthTypeText= new Label("Preferencje wzrostu roślin");
        grid.add(growthTypeText, 2, 8, 6, 1);
        ToggleButton equatorialForestRB= new ToggleButton("Zalesione równiki");
        ToggleButton toxicCorpsesRB= new ToggleButton("Toksyczne trupy");
        grid.add(equatorialForestRB, 9, 8, 5, 1);
        grid.add(toxicCorpsesRB, 14, 8, 5, 1);
        equatorialForestRB.setToggleGroup(growthType);
        toxicCorpsesRB.setToggleGroup(growthType);
        equatorialForestRB.setUserData(true);
        toxicCorpsesRB.setUserData(false);
        customLabels.add(growthTypeText);
        customToggleButtons.add(equatorialForestRB);
        customToggleButtons.add(toxicCorpsesRB);

        final ToggleGroup mutationType= new ToggleGroup();
        Label mutationTypeText= new Label("Przebieg mutacji");
        grid.add(mutationTypeText, 2, 10, 6, 1);
        ToggleButton fullRandomRB= new ToggleButton("Pełna losowość");
        ToggleButton smallCorrectionRB= new ToggleButton("Lekka korekta");
        grid.add(fullRandomRB, 9, 10, 5, 1);
        grid.add(smallCorrectionRB, 14, 10, 5, 1);
        fullRandomRB.setToggleGroup(mutationType);
        smallCorrectionRB.setToggleGroup(mutationType);
        fullRandomRB.setUserData(true);
        smallCorrectionRB.setUserData(false);
        customLabels.add(mutationTypeText);
        customToggleButtons.add(fullRandomRB);
        customToggleButtons.add(smallCorrectionRB);

        final ToggleGroup behaviorType= new ToggleGroup();
        Label behaviorTypeText= new Label("Zachowanie zwierząt");
        grid.add(behaviorTypeText, 2, 12, 6, 1);
        ToggleButton fullDestinyRB= new ToggleButton("Pełna predestynacja");
        ToggleButton littleCrazinessRB= new ToggleButton("Nieco szaleństwa");
        grid.add(fullDestinyRB, 9, 12, 6, 1);
        grid.add(littleCrazinessRB, 15, 12, 6, 1);
        fullDestinyRB.setToggleGroup(behaviorType);
        littleCrazinessRB.setToggleGroup(behaviorType);
        fullDestinyRB.setUserData(false);
        littleCrazinessRB.setUserData(true);
        customLabels.add(behaviorTypeText);
        customToggleButtons.add(fullDestinyRB);
        customToggleButtons.add(littleCrazinessRB);

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
        customLabels.add(dimensionsText);
        customLabels.add(letterXText);
        customTextFields.add(widthInput);
        customTextFields.add(heightInput);

        Label grassText= new Label("Rośliny:");
        grid.add(grassText, 13, 16, 5, 1);
        customLabels.add(grassText);

        Label numOfGrassText= new Label("Początkowa ilość:");
        grid.add(numOfGrassText, 14, 17, 5, 1);
        TextField numOfGrassInput= new TextField();
        grid.add(numOfGrassInput, 19, 17, 2, 1);
        Label dailyGrowthText= new Label("Dzienny przyrost:");
        grid.add(dailyGrowthText, 14, 18, 5, 1);
        TextField dailyGrowthInput= new TextField();
        grid.add(dailyGrowthInput, 19, 18, 2, 1);
        Label energyValueText= new Label("Wartość odżywcza:");
        grid.add(energyValueText, 14, 19, 5, 1);
        TextField energyValueInput= new TextField();
        grid.add(energyValueInput, 19, 19, 2, 1);
        customLabels.add(numOfGrassText);
        customLabels.add(dailyGrowthText);
        customLabels.add(energyValueText);
        customTextFields.add(numOfGrassInput);
        customTextFields.add(dailyGrowthInput);
        customTextFields.add(energyValueInput);

        Label animalText= new Label("Zwierzęta:");
        grid.add(animalText, 2, 16, 5, 1);
        customLabels.add(animalText);

        Label numOfAnimalsText= new Label("Początkowa ilość:");
        grid.add(numOfAnimalsText, 3, 17, 6, 1);
        TextField numOfAnimalsInput= new TextField();
        grid.add(numOfAnimalsInput, 9, 17, 2, 1);
        Label startEnergyText= new Label("Początkowa energia:");
        grid.add(startEnergyText, 3, 18, 6, 1);
        TextField startEnergyInput= new TextField();
        grid.add(startEnergyInput, 9, 18, 2, 1);
        Label reproductionEnergyLossText= new Label("Energia zużyta przy rozmnażaniu:");
        grid.add(reproductionEnergyLossText, 3, 19, 8, 1);
        TextField reproductionEnergyLossInput= new TextField();
        grid.add(reproductionEnergyLossInput, 11, 19, 2, 1);
        Label reproductionEnergyThresholdText= new Label("Minimalna energia do rozmnażania:");
        grid.add(reproductionEnergyThresholdText, 3, 20, 8, 1);
        TextField reproductionEnergyThresholdInput= new TextField();
        grid.add(reproductionEnergyThresholdInput, 11, 20, 2, 1);
        customLabels.add(numOfAnimalsText);
        customLabels.add(startEnergyText);
        customLabels.add(reproductionEnergyLossText);
        customLabels.add(reproductionEnergyThresholdText);
        customTextFields.add(numOfAnimalsInput);
        customTextFields.add(startEnergyInput);
        customTextFields.add(reproductionEnergyLossInput);
        customTextFields.add(reproductionEnergyThresholdInput);

        Label numOfGenesText= new Label("Długość genomu:");
        grid.add(numOfGenesText, 3, 21, 6, 1);
        TextField numOfGenesInput= new TextField();
        grid.add(numOfGenesInput, 9, 21, 2, 1);
        Label scopeMutationsText= new Label("Zakres liczby mutacji:");
        grid.add(scopeMutationsText, 3, 22, 6, 1);
        TextField scopeFromInput= new TextField();
        grid.add(scopeFromInput, 9, 22, 2, 1);
        Label dashText= new Label("   -");
        grid.add(dashText, 11, 22, 1, 1);
        TextField scopeToInput= new TextField();
        grid.add(scopeToInput, 12, 22, 2, 1);
        scopeFromInput.setPromptText("min");
        scopeToInput.setPromptText("max");
        customLabels.add(numOfGenesText);
        customLabels.add(scopeMutationsText);
        customLabels.add(dashText);
        customTextFields.add(numOfGenesInput);
        customTextFields.add(scopeFromInput);
        customTextFields.add(scopeToInput);

        Label moveDelayText= new Label("Długość dnia [ms]:");
        grid.add(moveDelayText, 1, 24, 5, 1);
        TextField moveDelayInput= new TextField("500");
        grid.add(moveDelayInput, 6, 24, 2, 1);

        for (Label label: customLabels)
            label.disableProperty().bind(settingsRB2.selectedProperty().not());
        for (TextField textField: customTextFields)
            textField.disableProperty().bind(settingsRB2.selectedProperty().not());
        for (ToggleButton toggleButton: customToggleButtons)
            toggleButton.disableProperty().bind(settingsRB2.selectedProperty().not());

        BooleanBinding buttonShouldBeDisabled = new BooleanBinding() {
            {
                super.bind( settingsType.selectedToggleProperty(), worldType.selectedToggleProperty(),
                        growthType.selectedToggleProperty(), mutationType.selectedToggleProperty(),
                        behaviorType.selectedToggleProperty(), widthInput.textProperty(), heightInput.textProperty(),
                        numOfGrassInput.textProperty(), dailyGrowthInput.textProperty(),
                        energyValueInput.textProperty(), numOfAnimalsInput.textProperty(),
                        startEnergyInput.textProperty(), reproductionEnergyLossInput.textProperty(),
                        reproductionEnergyThresholdInput.textProperty(), numOfGenesInput.textProperty(),
                        scopeFromInput.textProperty(), scopeToInput.textProperty(), moveDelayInput.textProperty() );
            }
            @Override
            protected boolean computeValue() {
                return ( settingsType.getSelectedToggle()==settingsRB2 && ( worldType.getSelectedToggle()==null ||
                        growthType.getSelectedToggle()==null || mutationType.getSelectedToggle()==null ||
                        behaviorType.getSelectedToggle()==null || !isInputInteger(widthInput.getText()) ||
                        !isInputInteger(heightInput.getText()) || !isInputInteger(numOfGrassInput.getText()) || !isInputInteger(dailyGrowthInput.getText()) || !isInputInteger(energyValueInput.getText()) ||
                                !isInputInteger(numOfAnimalsInput.getText()) || !isInputInteger(startEnergyInput.getText()) || !isInputInteger(reproductionEnergyLossInput.getText()) ||
                                !isInputInteger(reproductionEnergyThresholdInput.getText()) || !isInputInteger(numOfGenesInput.getText()) ||
                        !isInputInteger(scopeFromInput.getText()) || !isInputInteger(scopeToInput.getText()) ) ) ||
                        !isInputInteger(moveDelayInput.getText());
            }
        };

        // # Przycisk startowy

        Button button = new Button("Start symulacji");
        grid.add(button, 17, 24, 5, 1);
        button.disableProperty().bind(buttonShouldBeDisabled);

        button.setOnAction(actionEvent ->
        {
            InputConfiguration data;

            // TODO: będzie robione
            if (settingsType.getSelectedToggle() == settingsRB2) {    // jeśli wybrano ustawienia customowe
                System.out.println("custom!");
                try {
                    data= new InputConfiguration ( Integer.parseInt(widthInput.getText()),
                            Integer.parseInt(heightInput.getText()),
                            Integer.parseInt(numOfAnimalsInput.getText()),
                            Integer.parseInt(numOfGrassInput.getText()),
                            Integer.parseInt(startEnergyInput.getText()),
                            Integer.parseInt(energyValueInput.getText()),
                            Integer.parseInt(reproductionEnergyLossInput.getText()),
                            Integer.parseInt(reproductionEnergyThresholdInput.getText()),
                            Integer.parseInt(numOfGenesInput.getText()),
                            Integer.parseInt(dailyGrowthInput.getText()),
                            new int[] {Integer.parseInt(scopeFromInput.getText()), Integer.parseInt(scopeToInput.getText())},
                            (Boolean)worldType.getSelectedToggle().getUserData(),
                            (Boolean)growthType.getSelectedToggle().getUserData(),
                            (Boolean)mutationType.getSelectedToggle().getUserData(),
                            (Boolean)behaviorType.getSelectedToggle().getUserData(),
                            Integer.parseInt(moveDelayInput.getText()) );
                }
                catch (InputConfiguration.InvalidConfigurationException ex) {
                    // System.out.println(ex.toString());
                    new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.NO).showAndWait();
                    return;
                }
            }
            else
                data= new InputConfiguration(13, 13, 10, 10, 50,
                        0, 10, 20, 10, 17,
                        new int[]{3, 3}, false, true, true, false, 500);

            System.out.println("Czy kula ziemska: " + data.earthGlobe);
            System.out.println("Czy równik: " + data.equatorialForests);
            System.out.println("Czy losowa mutacja: " + data.fullRandom);
            System.out.println("Szaleństwo: " + data.craziness);

            startSimulation(data);
        });


        Label lineText= new Label("   " + new String(new char[77]).replace("\0", "_"));
        grid.add(lineText, 0, 25, 23, 1);

        Label colorText= new Label(" Kolor zwierzęcia określać będzie posiadaną przez niego energię:");
        grid.add(colorText, 0, 26, 15, 1);

        Image colors;
        try {
            colors= new Image(new FileInputStream("src/main/resources/hue-scale.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView imgView= new ImageView(colors);
        imgView.setFitWidth(550);
        grid.add(imgView, 0, 29, 1, 1);

        primaryStage.show();
    }

    private final boolean isInputInteger (String input) {
        if (input.isEmpty())
            return false;

        int temp;
        try {
            temp= Integer.parseInt(input);
        }
        catch (Exception ex) {
            return false;
        }

        return true;
    }


    private void startSimulation (InputConfiguration x) {
        AbstractWorldMap map;

        if (x.earthGlobe)
            map= new KulaZiemska(x.mapWidth, x.mapHeight,  x.numOfGrass, x.grassEnergy, x.equatorialForests);
        else
            map= new PiekielnyPortal(x.mapWidth, x.mapHeight,  x.numOfGrass, x.grassEnergy, x.equatorialForests, x.reproductionEnergyLoss);

//        int moveDelay = 500;
        Runnable engine = new SimulationEngine(map, this, x.moveDelay, x.numOfAnimals, x.startEnergy,
                -x.reproductionEnergyLoss, x.reproductionEnergyThreshold, x.numOfGenes, x.scopeOfMutations,
                x.fullRandom, x.craziness, x.dailyGrass);

        Thread engineThread = new Thread(engine);
        engineThread.start();
    }



}
