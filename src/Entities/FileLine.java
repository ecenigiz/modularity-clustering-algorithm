package Entities;

public class FileLine implements  Comparable<FileLine> {
    public String Name;
    public String ChildLib;
    public String ParentLib;
    public boolean IsClustered;

    @Override
    public int compareTo(FileLine f) {
        int result = this.Name.compareToIgnoreCase(f.Name);
        if(result != 0){
            return result;
        }else{
            return new String(this.Name).compareTo(new String(f.Name));
        }
    }
}
