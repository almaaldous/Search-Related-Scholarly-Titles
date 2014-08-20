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
public class RelatedTitles {

    final String title;
    private int frequency;
    final String briefDescription;
    final String titleUrl;

    // Add generate getters and setters.
    public RelatedTitles(String title, int frequency, String briefDescription, String titleUrl) {
        this.title = title;
        this.frequency = frequency;
        this.briefDescription = briefDescription;
        this.titleUrl = titleUrl;
    }

    public RelatedTitles(String title, String briefDescription, String titleUrl) {
        this.title = title;
        this.briefDescription = briefDescription;
        this.titleUrl = titleUrl;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getTitle() {
        return title;
    }

    public int getFrequency() {
        return frequency;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

}
