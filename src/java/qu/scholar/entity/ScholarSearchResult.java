/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package qu.scholar.entity;

/**
 *
 * @author aeldous
 */
public class ScholarSearchResult {
     //Title | URL | Year | Citations | Versions| Cluster ID | PDF Link |Citations List | Versions List | None
    
    final String title,URL,year,citations,versions,clusterId,pdfLink,citationsList,versionsList,info;

    public ScholarSearchResult(String title, String URL, String year, String citations, String versions, String clusterId, String pdfLink, String citationsList, String versionsList, String info) {
       
        this.title = title; // 0
        this.URL = URL;
        this.year = year;
        this.citations = citations;
        this.versions = versions;
        this.clusterId = clusterId;
        this.pdfLink = pdfLink;
        this.citationsList = citationsList;
        this.versionsList = versionsList;
        this.info = info; //9
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return URL;
    }

    public String getYear() {
        return year;
    }

    public String getCitations() {
        return citations;
    }

    public String getVersions() {
        return versions;
    }

    public String getClusterId() {
        return clusterId;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public String getCitationsList() {
        return citationsList;
    }

    public String getVersionsList() {
        return versionsList;
    }

    public String getInfo() {
        return info;
    }
    
    
    
}
