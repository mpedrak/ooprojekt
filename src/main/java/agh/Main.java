package agh;

import javafx.application.Application;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
             Application.launch(App.class, args);
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }

}
