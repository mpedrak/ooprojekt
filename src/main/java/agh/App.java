package agh;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.function.Predicate;


public class App  extends Application
{
    final int width = 25;
    final int height = 25;
    Vector2d bottomLeft;
    Vector2d upperRight;
    String directoryPath= "src/main/statistics/";
    public void start(Stage primaryStage)
    {
        renderujStart(primaryStage);
    }

    public void renderuj(AbstractWorldMap map, GridPane grid, SimulationEngine engine, Animal zwierzeDoSledzenia)
    {
        bottomLeft = map.getPoczatekMapy();
        upperRight = map.getKraniecMapy();

        grid.getChildren().removeIf(x -> !(x instanceof Button));

        Label label7 = new Label(engine.toString());
        grid.add(label7, 0, 2, 29, 2);

        if(zwierzeDoSledzenia != null)
        {
            Label label = new Label(zwierzeDoSledzenia.toString());
            grid.add(label, 0, 1, 29, 1);
        }

        VBox vbox = new VBox();
        Label s = new Label( "y\\x");
        vbox.getChildren().addAll(s);
        vbox.setAlignment(Pos.CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        grid.add(vbox, 0, 4, 1, 1);
        GridPane.setHalignment(vbox, HPos.CENTER);

        for (int i = bottomLeft.x ; i <= upperRight.x; ++i)
        {
            vbox = new VBox();
            s = new Label(Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.setAlignment(Pos.CENTER);
            vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            grid.add(vbox, i - bottomLeft.x + 1, 4, 1, 1);
        }

        for (int i = bottomLeft.x ; i <= upperRight.x + 1; ++i)
        {
            vbox = new VBox();
            vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            grid.add(vbox, i - bottomLeft.x, upperRight.y + 6, 1, 1);
        }

        int j = 1;
        for (int i = upperRight.y; i >= bottomLeft.y; --i)
        {
            vbox = new VBox();
            s = new Label(Integer.toString(i));
            vbox.getChildren().addAll(s);
            vbox.setAlignment(Pos.CENTER);
            vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            grid.add(vbox, 0, j + 4, 1, 1);
            ++j;
        }

        for (int i = 0; i <= upperRight.y + 2; i++)
        {
            vbox = new VBox();
            vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            grid.add(vbox, upperRight.x + 2, i + 4, 1, 1);
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
                                if(engine.isPaused())
                                {
                                    Node n = getNodeByRowColumnIndex(1, 0, grid);
                                    grid.getChildren().remove(n);

                                    engine.zmienZwierzeDoSledzenia((Animal) z);
                                    Label label = new Label(z.toString());
                                    grid.add(label, 0, 1, 29, 1);
                                }
                            }
                        });
                    }
                    grid.add(element.vbox, x - bottomLeft.x + 1, j + 4, 1, 1);
                    GridPane.setHalignment(element.vbox, HPos.CENTER);
                }
                ++j;
            }
        }

    }

    private Node getNodeByRowColumnIndex (int row, int column, GridPane gridPane)
    {
        Node n = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node x : childrens)
            if(gridPane.getRowIndex(x) == row && gridPane.getColumnIndex(x) == column)
            {
                n = x;
                break;
            }
        return n;
    }

    public GridPane renderujPierwszyRaz(AbstractWorldMap map, SimulationEngine engine)
    {
        GridPane grid = new GridPane();

        bottomLeft = map.getPoczatekMapy();
        upperRight = map.getKraniecMapy();
        int pom = Math.max((upperRight.x - bottomLeft.x  + 2), 30);
        int w =  (pom + 1) * width;
        int h = (upperRight.y - bottomLeft.y  + 7) * height;

        for (int i = 0; i <= pom; i++)
            grid.getColumnConstraints().add(new ColumnConstraints(width));
        for (int i = 0; i <= upperRight.y - bottomLeft.y + 6; i++)
            grid.getRowConstraints().add(new RowConstraints(height));


        Button button = new Button("Pauza ");
        grid.add(button, 0, 0, 5, 1);

        button.setOnAction(actionEvent ->
        {
            engine.pauza();
        });

        Button button2 = new Button("Zatrzymaj sledzenie");
        grid.add(button2, 3, 0, 5, 1);

        button2.setOnAction(actionEvent ->
        {
            engine.zmienZwierzeDoSledzenia(null);
        });

        Button button3 = new Button("Pokaz zwierzeta o dominujacym genie");
        grid.add(button3, 9, 0, 9, 1);

        button3.setOnAction(actionEvent ->
        {
            if(engine.isPaused()) engine.wyroznijZwierzeta();
        });

        Scene scene = new Scene(grid, w, h);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        // stage.setResizable(false);

        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> engine.stopSimulation());

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
        grid.add(settingsRB2, 1, 3, 7, 1);
        settingsRB1.setToggleGroup(settingsType);
        settingsRB2.setToggleGroup(settingsType);
        settingsRB1.setSelected(true);
//        settingsRB1.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));
//        settingsRB2.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));


        // # Wybór preseta
        final ComboBox presetsCB = new ComboBox<>(FXCollections.observableArrayList(
                "Ogród Eden", "13x13 Nether Forest", "Soviet Union", "Balanced"));
        presetsCB.setPromptText("<wybierz wariant>");
        grid.add(presetsCB, 8, 1, 10, 1);
        presetsCB.setEditable(false);

        // # Wybór customowych ustawień
        Collection<Label> customLabels= new LinkedList<Label>();
        Collection<TextField> customTextFields= new LinkedList<TextField>();
        Collection<ToggleButton> customToggleButtons= new LinkedList<ToggleButton>();

        final ToggleGroup worldType= new ToggleGroup();
        Label worldTypeText= new Label("Typ świata");
        grid.add(worldTypeText, 2, 5, 6, 1);
        ToggleButton earthGlobeRB= new ToggleButton("Kula ziemska");
        ToggleButton netherPortalRB= new ToggleButton("Piekielny portal");
        grid.add(earthGlobeRB, 9, 5, 4, 1);
        grid.add(netherPortalRB, 13, 5, 4, 1);
        earthGlobeRB.setToggleGroup(worldType);
        netherPortalRB.setToggleGroup(worldType);
        earthGlobeRB.setUserData(true);
        netherPortalRB.setUserData(false);
        customLabels.add(worldTypeText);
        customToggleButtons.add(earthGlobeRB);
        customToggleButtons.add(netherPortalRB);

        final ToggleGroup growthType= new ToggleGroup();
        Label growthTypeText= new Label("Preferencje wzrostu roślin");
        grid.add(growthTypeText, 2, 7, 6, 1);
        ToggleButton equatorialForestRB= new ToggleButton("Zalesione równiki");
        ToggleButton toxicCorpsesRB= new ToggleButton("Toksyczne trupy");
        grid.add(equatorialForestRB, 9, 7, 5, 1);
        grid.add(toxicCorpsesRB, 14, 7, 5, 1);
        equatorialForestRB.setToggleGroup(growthType);
        toxicCorpsesRB.setToggleGroup(growthType);
        equatorialForestRB.setUserData(true);
        toxicCorpsesRB.setUserData(false);
        customLabels.add(growthTypeText);
        customToggleButtons.add(equatorialForestRB);
        customToggleButtons.add(toxicCorpsesRB);

        final ToggleGroup mutationType= new ToggleGroup();
        Label mutationTypeText= new Label("Przebieg mutacji");
        grid.add(mutationTypeText, 2, 9, 6, 1);
        ToggleButton fullRandomRB= new ToggleButton("Pełna losowość");
        ToggleButton smallCorrectionRB= new ToggleButton("Lekka korekta");
        grid.add(fullRandomRB, 9, 9, 5, 1);
        grid.add(smallCorrectionRB, 14, 9, 5, 1);
        fullRandomRB.setToggleGroup(mutationType);
        smallCorrectionRB.setToggleGroup(mutationType);
        fullRandomRB.setUserData(true);
        smallCorrectionRB.setUserData(false);
        customLabels.add(mutationTypeText);
        customToggleButtons.add(fullRandomRB);
        customToggleButtons.add(smallCorrectionRB);

        final ToggleGroup behaviorType= new ToggleGroup();
        Label behaviorTypeText= new Label("Zachowanie zwierząt");
        grid.add(behaviorTypeText, 2, 11, 6, 1);
        ToggleButton fullDestinyRB= new ToggleButton("Pełna predestynacja");
        ToggleButton littleCrazinessRB= new ToggleButton("Nieco szaleństwa");
        grid.add(fullDestinyRB, 9, 11, 6, 1);
        grid.add(littleCrazinessRB, 15, 11, 6, 1);
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
        grid.add(dimensionsText, 2, 13, 5, 1);
        grid.add(widthInput, 7, 13, 2, 1);
        grid.add(heightInput, 10, 13, 2, 1);
        widthInput.setPromptText("x");
        heightInput.setPromptText("y");
        Label letterXText= new Label("   x");
        grid.add(letterXText, 9, 13, 1, 1);
        customLabels.add(dimensionsText);
        customLabels.add(letterXText);
        customTextFields.add(widthInput);
        customTextFields.add(heightInput);

        Label grassText= new Label("ROŚLINY");
        grid.add(grassText, 16, 15, 5, 1);
        customLabels.add(grassText);

        Label numOfGrassText= new Label("Początkowa ilość:");
        grid.add(numOfGrassText, 14, 16, 5, 1);
        TextField numOfGrassInput= new TextField();
        grid.add(numOfGrassInput, 19, 16, 2, 1);
        Label dailyGrowthText= new Label("Dzienny przyrost:");
        grid.add(dailyGrowthText, 14, 17, 5, 1);
        TextField dailyGrowthInput= new TextField();
        grid.add(dailyGrowthInput, 19, 17, 2, 1);
        Label energyValueText= new Label("Wartość odżywcza:");
        grid.add(energyValueText, 14, 18, 5, 1);
        TextField energyValueInput= new TextField();
        grid.add(energyValueInput, 19, 18, 2, 1);
        customLabels.add(numOfGrassText);
        customLabels.add(dailyGrowthText);
        customLabels.add(energyValueText);
        customTextFields.add(numOfGrassInput);
        customTextFields.add(dailyGrowthInput);
        customTextFields.add(energyValueInput);

        Label animalText= new Label("ZWIERZĘTA");
        grid.add(animalText, 6, 15, 5, 1);
        customLabels.add(animalText);

        Label numOfAnimalsText= new Label("Początkowa ilość:");
        grid.add(numOfAnimalsText, 3, 16, 6, 1);
        TextField numOfAnimalsInput= new TextField();
        grid.add(numOfAnimalsInput, 11, 16, 2, 1);
        Label startEnergyText= new Label("Początkowa energia:");
        grid.add(startEnergyText, 3, 17, 6, 1);
        TextField startEnergyInput= new TextField();
        grid.add(startEnergyInput, 11, 17, 2, 1);
        Label reproductionEnergyLossText= new Label("Energia zużyta przy rozmnażaniu:");
        grid.add(reproductionEnergyLossText, 3, 18, 8, 1);
        TextField reproductionEnergyLossInput= new TextField();
        grid.add(reproductionEnergyLossInput, 11, 18, 2, 1);
        Label reproductionEnergyThresholdText= new Label("Minimalna energia do rozmnażania:");
        grid.add(reproductionEnergyThresholdText, 3, 19, 8, 1);
        TextField reproductionEnergyThresholdInput= new TextField();
        grid.add(reproductionEnergyThresholdInput, 11, 19, 2, 1);
        customLabels.add(numOfAnimalsText);
        customLabels.add(startEnergyText);
        customLabels.add(reproductionEnergyLossText);
        customLabels.add(reproductionEnergyThresholdText);
        customTextFields.add(numOfAnimalsInput);
        customTextFields.add(startEnergyInput);
        customTextFields.add(reproductionEnergyLossInput);
        customTextFields.add(reproductionEnergyThresholdInput);

        Label numOfGenesText= new Label("Długość genomu:");
        grid.add(numOfGenesText, 3, 20, 6, 1);
        TextField numOfGenesInput= new TextField();
        grid.add(numOfGenesInput, 11, 20, 2, 1);
        Label scopeMutationsText= new Label("Zakres liczby mutacji:");
        grid.add(scopeMutationsText, 3, 21, 6, 1);
        TextField scopeFromInput= new TextField();
        grid.add(scopeFromInput, 11, 21, 2, 1);
        Label dashText= new Label("   -");
        grid.add(dashText, 13, 21, 1, 1);
        TextField scopeToInput= new TextField();
        grid.add(scopeToInput, 14, 21, 2, 1);
        scopeFromInput.setPromptText("min");
        scopeToInput.setPromptText("max");
        customLabels.add(numOfGenesText);
        customLabels.add(scopeMutationsText);
        customLabels.add(dashText);
        customTextFields.add(numOfGenesInput);
        customTextFields.add(scopeFromInput);
        customTextFields.add(scopeToInput);

        Label moveDelayText= new Label("Długość dnia [ms]:");
        grid.add(moveDelayText, 1, 23, 5, 1);
        TextField moveDelayInput= new TextField("500");
        grid.add(moveDelayInput, 6, 23, 3, 1);

        CheckBox saveToFileCB= new CheckBox("Zapis statystyk");
        grid.add(saveToFileCB, 1, 24, 5, 1);
        TextField fileNameInput= new TextField("output");
        grid.add(fileNameInput, 6, 24, 3, 1);
        Label fileTypeText= new Label(".csv");
        grid.add(fileTypeText, 9, 24, 2, 1);
        fileNameInput.disableProperty().bind(saveToFileCB.selectedProperty().not());
        fileTypeText.disableProperty().bind(saveToFileCB.selectedProperty().not());
        fileNameInput.setAlignment(Pos.CENTER_RIGHT);
        fileNameInput.setPromptText("file_name");

        Button chooseDirectButton= new Button("📂");
        // chooseDirectButton.setFont(Font.font(15));
        grid.add(chooseDirectButton, 11, 24, 4, 1);


        chooseDirectButton.setOnAction(actionEvent -> {
            try {
                DirectoryChooser directoryChooser= new DirectoryChooser();
                File selectedDirectory= directoryChooser.showDialog(primaryStage);
                zmienSciezkeDoFolderu(selectedDirectory.getPath());
                System.out.println(selectedDirectory.getAbsolutePath());
            }
            catch (Exception ex) { return; }
        });


        for (Label label: customLabels)
            label.disableProperty().bind(settingsRB2.selectedProperty().not());
        for (TextField textField: customTextFields)
            textField.disableProperty().bind(settingsRB2.selectedProperty().not());
        for (ToggleButton toggleButton: customToggleButtons)
            toggleButton.disableProperty().bind(settingsRB2.selectedProperty().not());

        BooleanBinding buttonShouldBeDisabled= new BooleanBinding() {
            {
                super.bind( settingsType.selectedToggleProperty(), worldType.selectedToggleProperty(),
                        growthType.selectedToggleProperty(), mutationType.selectedToggleProperty(),
                        behaviorType.selectedToggleProperty(), widthInput.textProperty(), heightInput.textProperty(),
                        numOfGrassInput.textProperty(), dailyGrowthInput.textProperty(),
                        energyValueInput.textProperty(), numOfAnimalsInput.textProperty(),
                        startEnergyInput.textProperty(), reproductionEnergyLossInput.textProperty(),
                        reproductionEnergyThresholdInput.textProperty(), numOfGenesInput.textProperty(),
                        scopeFromInput.textProperty(), scopeToInput.textProperty(), moveDelayInput.textProperty(),
                        presetsCB.valueProperty(), saveToFileCB.selectedProperty(), fileNameInput.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return ( settingsType.getSelectedToggle()==settingsRB2 && ( worldType.getSelectedToggle()==null ||
                        growthType.getSelectedToggle()==null || mutationType.getSelectedToggle()==null ||
                        behaviorType.getSelectedToggle()==null || !Utils.isInputInteger(widthInput.getText()) ||
                        !Utils.isInputInteger(heightInput.getText()) || !Utils.isInputInteger(numOfGrassInput.getText()) || !Utils.isInputInteger(dailyGrowthInput.getText()) || !Utils.isInputInteger(energyValueInput.getText()) ||
                                !Utils.isInputInteger(numOfAnimalsInput.getText()) || !Utils.isInputInteger(startEnergyInput.getText()) || !Utils.isInputInteger(reproductionEnergyLossInput.getText()) ||
                                !Utils.isInputInteger(reproductionEnergyThresholdInput.getText()) || !Utils.isInputInteger(numOfGenesInput.getText()) ||
                        !Utils.isInputInteger(scopeFromInput.getText()) || !Utils.isInputInteger(scopeToInput.getText()) ) ) ||
                        (settingsType.getSelectedToggle()==settingsRB1 && presetsCB.getValue()==null) || !Utils.isInputInteger(moveDelayInput.getText()) ||
                        (saveToFileCB.isSelected() && fileNameInput.getText().isEmpty());
            }
        };

        // # Przycisk startowy

        Button button = new Button("Start symulacji");
        grid.add(button, 16, 24, 8, 1);
        button.disableProperty().bind(buttonShouldBeDisabled);
        button.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));

        button.setOnAction(actionEvent ->
        {
            InputConfiguration data;

            if (settingsType.getSelectedToggle() == settingsRB2) {    // jeśli wybrano ustawienia customowe
                // System.out.println("custom!");
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
                    new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE).showAndWait();
                    return;
                }
            }
            else {

                String filePath = switch (presetsCB.getValue().toString()) {
                    case "Ogród Eden" -> "src/main/templates/eden_garden.simconf";
                    case "13x13 Nether Forest" -> "src/main/templates/13_nether_forest.simconf";
                    case "Soviet Union" -> "src/main/templates/soviet.simconf";
                    case "Balanced" -> "src/main/templates/balanced.simconf";
                    default -> "";
                };

                Map<String, String> inp= new HashMap<String, String>();

                try {
                    String tempString;
                    BufferedReader reader= new BufferedReader(new FileReader(filePath));
                    while ((tempString= reader.readLine()) != null) {
                        String[] inputLine = tempString.split(": ");
                        inp.put(inputLine[0], inputLine[1]);
                    }
                }
                catch (IOException ex) {
                    new Alert(Alert.AlertType.ERROR, "Błąd wczytywania pliku!", ButtonType.CLOSE).showAndWait();
                    return;
                }

                try {
                    data= InputConfiguration.makeFromMap(inp, Integer.parseInt(moveDelayInput.getText()));
                }
                catch (InputConfiguration.InvalidConfigurationException ex) {
                    new Alert(Alert.AlertType.ERROR, "Błąd w pliku -> " + ex.getMessage(), ButtonType.CLOSE).showAndWait();
                    return;
                }
                catch (IllegalArgumentException ex) {
                    new Alert(Alert.AlertType.ERROR, "Niepoprawne wartość długości dnia!", ButtonType.CLOSE).showAndWait();
                    return;
                }

            }

            String exportFilePath= "";
            if (saveToFileCB.isSelected()) {
                File statsFile;
                try {
                    exportFilePath= this.directoryPath + "/" + fileNameInput.getText() + ".csv";
                    statsFile= new File(exportFilePath);

                    if (statsFile.createNewFile())
                        System.out.println("Utworzono plik: " + statsFile.getName());
                    else
                        System.out.println("Plik istnieje.");
                }
                catch (IOException ex) {
                    new Alert(Alert.AlertType.ERROR, "Błąd przy tworzeniu pliku!", ButtonType.CLOSE).showAndWait();
                    return;
                }
            }


            System.out.println("Czy kula ziemska: " + data.earthGlobe);
            System.out.println("Czy równik: " + data.equatorialForests);
            System.out.println("Czy losowa mutacja: " + data.fullRandom);
            System.out.println("Szaleństwo: " + data.craziness);

            try {
                startSimulation(data, exportFilePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });


        Label lineText= new Label("        " + new String(new char[74]).replace("\0", "_"));
        grid.add(lineText, 0, 25, 23, 1);

        Label colorText= new Label("Kolor zwierzęcia określać będzie posiadaną przez niego energię:");
        grid.add(colorText, 1, 26, 15, 1);

        Image colors;
        try {
            colors= new Image(new FileInputStream("src/main/resources/hue-scale.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ImageView imgView= new ImageView(colors);
        imgView.setFitWidth(500);
        grid.add(imgView, 1, 29, 1, 1);

        primaryStage.show();
    }

    private void startSimulation (InputConfiguration x, String exportFilePath) throws FileNotFoundException {
        AbstractWorldMap map;

        if (x.earthGlobe)
            map= new KulaZiemska(x.mapWidth, x.mapHeight,  x.numOfGrass, x.grassEnergy, x.equatorialForests);
        else
            map= new PiekielnyPortal(x.mapWidth, x.mapHeight,  x.numOfGrass, x.grassEnergy, x.equatorialForests, x.reproductionEnergyLoss);

        Runnable engine = new SimulationEngine(map, this, x.moveDelay, x.numOfAnimals, x.startEnergy,
                -x.reproductionEnergyLoss, x.reproductionEnergyThreshold, x.numOfGenes, x.scopeOfMutations,
                x.fullRandom, x.craziness, x.dailyGrass, exportFilePath);

        Thread engineThread = new Thread(engine);
        engineThread.start();
    }
    private void zmienSciezkeDoFolderu(String s)
    {
        this.directoryPath = s;
    }


}
