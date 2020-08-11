package LogClasses;

public interface ITransform {
    public String toString();
    public String toJSON();
    public String retrieveClassName();


    public ITransform fromJSON(String json);


}
