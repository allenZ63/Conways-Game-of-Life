package gameoflife;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class UserInterface {

    JPanel UIpanel = new JPanel();
    JFrame UIframe = new JFrame();

    public static void main(String[] args) throws InterruptedException {
        UserInterface UI = new UserInterface();
        UI.UIsetup();
    }

    private void UIsetup() {
        UIframe.setSize(600, 400);
        UIframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UIframe.setExtendedState(Frame.MAXIMIZED_BOTH);
        UIpanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        UIframe.add(UIpanel);

        UIframe.setVisible(true);
    }
}


