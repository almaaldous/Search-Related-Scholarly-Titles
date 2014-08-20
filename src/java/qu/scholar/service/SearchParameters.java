package qu.scholar.service;

public class SearchParameters {

     private String [] titles;
     private int count;

       public SearchParameters() {
       }
    public SearchParameters(String[] titles, int count) {
        this.titles = titles;
        this.count = count;
    }

  
    public String[] getTitles() {
        return titles;
    }

    public int getCount() {
        return count;
    }

}
