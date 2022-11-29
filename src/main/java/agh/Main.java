package agh;

import javafx.application.Application;

import java.util.Arrays;
import java.util.Map;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            // Application.launch(App.class, args);

            System.out.println(Arrays.toString(args));
            Runnable engine = null;
            engine.run();
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(ex);
        }
    }

}
