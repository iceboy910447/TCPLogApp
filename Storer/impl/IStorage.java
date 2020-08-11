package Storer.impl;

import LogClasses.ITransform;

import java.util.List;

public interface IStorage {

    public int getID();
    public ITransform getLast(String classname);
    public boolean store(ITransform transform);
    public List<ITransform> retrieveAll(String type);
    public long getStartTime();
    public long getRunTime();

}
