package agh;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.Glow;
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
            ImageView imageView = ((Animal)element).getImageView();
            ColorAdjust c = new ColorAdjust();
            int g = ((Animal)element).getEnergy();
            if(((Animal)element).czyDoWyroznienia())
            {
                c.setSaturation(-0.5);
                imageView.setEffect(c);
                Bloom b = new Bloom(0);
                vbox.setEffect(b);
            }
            else if (g > 0)
            {
                g = Math.min(g, 20);
                if(g == 20) c.setHue(0);
                else if(g > 10) c.setHue((double)(20 - g) / 10.0 - 0.3);
                else c.setHue(-1 * (double)g / 10.0 - 0.4);
                imageView.setEffect(c);
            }
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
