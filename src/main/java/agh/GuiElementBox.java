package agh;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox
{
    public VBox vbox = new VBox();
    public GuiElementBox(IMapElement element) throws FileNotFoundException
    {
        Image image = new Image(new FileInputStream(element.getPath()));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        vbox.getChildren().addAll(imageView);
        vbox.setAlignment(Pos.CENTER);
    }
}
