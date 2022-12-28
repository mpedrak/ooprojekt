package agh;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GuiElementBox
{
    public VBox vbox = new VBox();
    public GuiElementBox(IMapElement element) throws FileNotFoundException
    {
        if(element instanceof Animal)
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
        else
        {
            Rectangle r =  new Rectangle(17, 17);
            r.setFill(Color.rgb(18, 103, 18,1));
            vbox.getChildren().addAll(r);
            vbox.setAlignment(Pos.CENTER);
        }
    }
}
