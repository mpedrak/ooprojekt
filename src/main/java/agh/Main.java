package agh;


import javafx.application.Application;

public class Main
{
    // https://docs.google.com/document/d/1lGtTzDSZbE9P6YoLCtefZ4HlLtQ6RlqtYzccdU4hiAc/edit
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
