package com.scalefocus.view;

import java.util.Scanner;

public abstract class View {
    protected static Scanner sc = new Scanner(System.in);
    public boolean exit = false;

    public abstract void menu(boolean bool) ;
}
