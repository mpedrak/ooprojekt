package agh;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
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
        if (element instanceof Animal)
        {
            ColorAdjust c = new ColorAdjust();
            int g = ((Animal)element).getEnergy();
            g = Math.min(g, 20);
            if (g != 20 && g > 0)
            {
                if(g > 10) c.setHue((double)(20 - g) / 10.0 - 0.3);
                else c.setHue(-1 * (double)g / 10.0 - 0.4);
                imageView.setEffect(c);
            }
        }
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        vbox.getChildren().addAll(imageView);
        vbox.setAlignment(Pos.CENTER);
    }
}
