package pluto;


import pluto.component.IComponent;
import pluto.component.LoginComponent;

public class Main {
    public static void main(String[] args) {
        IComponent loginPage = new LoginComponent();
        loginPage.open();
    }
}
