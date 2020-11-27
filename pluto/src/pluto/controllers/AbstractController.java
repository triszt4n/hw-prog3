package pluto.controllers;

public abstract class AbstractController {
    public abstract void index();
    public abstract void form();
    public abstract void create();
    public abstract void edit();
    public abstract void update(boolean doUpdate);
    public abstract void delete();
    public abstract void show();
}
