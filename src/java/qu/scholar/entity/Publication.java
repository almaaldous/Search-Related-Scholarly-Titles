package qu.scholar.entity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Publication {

    final static String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";
    final static String URL_TITLES_QUERY = "http://humanitariancomp.referata.com/w/index.php?title=Special%3AAsk&q=%5B%5BCategory%3APublications%5D%5D&po=&eq=yes&p%5Bformat%5D=csv&sort_num=&order_num=ASC&p%5Blimit%5D=1000&p%5Boffset%5D=&p%5Blink%5D=all&p%5Bsort%5D=&p%5Bheaders%5D=show&p%5Bmainlabel%5D=Title&p%5Bintro%5D=&p%5Boutro%5D=&p%5Bsearchlabel%5D=%E2%80%A6+further+results&p%5Bdefault%5D=&p%5Bsep%5D=%2C&eq=yes";
    final static String URL_TITLES_BY_KEYWORD_QUERY_PART_1 = "http://humanitariancomp.referata.com/w/index.php?title=Special%3AAsk&q=%5B%5BHas+keywords%3A%3A";
    final static String URL_TITLES_BY_KEYWORD_QUERY_PART_2 = "%5D%5D&po=&eq=yes&p%5Bformat%5D=csv&sort_num=&order_num=ASC&p%5Blimit%5D=1000&p%5Boffset%5D=&p%5Blink%5D=all&p%5Bsort%5D=&p%5Bheaders%5D=show&p%5Bmainlabel%5D=Title&p%5Bintro%5D=&p%5Boutro%5D=&p%5Bsearchlabel%5D=%E2%80%A6+further+results&p%5Bdefault%5D=&p%5Bsep%5D=%2C&eq=yes";
    final static String URL_LIBRARY_KEYWORDS="http://humanitariancomp.referata.com/w/index.php?title=Special%3AAsk&q=%5B%5BCategory%3AKeywords%5D%5D&po=&eq=yes&p%5Bformat%5D=csv&sort_num=&order_num=ASC&p%5Blimit%5D=1000&p%5Boffset%5D=&p%5Blink%5D=all&p%5Bsort%5D=&p%5Bheaders%5D=show&p%5Bmainlabel%5D=&p%5Bintro%5D=&p%5Boutro%5D=&p%5Bsearchlabel%5D=%E2%80%A6+further+results&p%5Bdefault%5D=&p%5Bsep%5D=%2C&eq=yes";
    final static String ERROR_MESSAGE="No search result found";
    final static int TIMEOUT = 15000;
    final static Pattern CLEAN_WORDS = Pattern.compile("\\b \\[BOOK\\]|\\[BOOK\\]\\[B\\]|\\[BOOK\\]\\[BOOK\\]|\\[PDF\\]\\[PDF\\]|\\[PDF\\]|\\[CITATION\\]\\[C\\]|\\[CITATION\\]|\\[C\\]|\\[C\\]\\[C\\]|\\[HTML\\]\\[HTML\\]|\\[DOC\\]|\\[DOC\\]\\[DOC\\]\n\\b\\s*", Pattern.CASE_INSENSITIVE);
    //final String title;
    //final String url;
    final ArrayList<String> relatedTitles;
    final static HashMap<String, Integer> frequencyRelatedTitles = new HashMap<>();

    public Publication(String title, String url, ArrayList<String> relatedTitles) {
//        this.title = title;
        //this.url = url;
        this.relatedTitles = relatedTitles;
    }

    public ArrayList<String> getRelatedTitles() {
        return relatedTitles;
    }

    public HashMap<String, Integer> getFrequencyRelatedTitles() {
        return frequencyRelatedTitles;
    }

    public static List<RelatedTitles> searchPublication(String[] inTitles) throws IOException {

        List<RelatedTitles> related_Titles = new ArrayList<>();

        System.out.printf("Array Length:" + inTitles.length);

        for (int i = 0; i < inTitles.length; i++) {
            System.out.println("title format" + inTitles[i]);
            ScholarSearchResult scholarSearchResult = null;
            try {
                scholarSearchResult = findCitationListURL(inTitles[i]);
            } catch (Exception ex) {
                if (scholarSearchResult == null) {
            System.out.println("related_Titles list size:" +related_Titles.size());
                    continue;
                }
            }

            String url = scholarSearchResult.getCitationsList();
            System.out.println("sfter finding citation list url is:" + url);

            System.out.printf("Title:%d , URL is:%s ", i, url);

            if (url == null) {
                System.out.println("URL was null" + url);
                continue;
            }
            Connection con = Jsoup.connect(url).userAgent(USER_AGENT).timeout(TIMEOUT);

            Document doc;
            try {

                Connection.Response resp = con.execute();

                doc = con.get();
            } catch (IOException ex) {
                System.out.println("Error, getting html source, probably connection issue");
                continue;
            }

            if (doc != null) {

                Elements link = doc.select("h3");
                System.out.println("Size of link: " + link.size());
                
                for (Element relatedPublication : link) {
                    System.out.printf("Title: %s has related: %s", inTitles[i], relatedPublication.text());
                    Matcher matcher = CLEAN_WORDS.matcher(relatedPublication.text());
                    String temTitle = matcher.replaceAll("");
                    temTitle = temTitle.trim();
                    String titleUrl = relatedPublication.select("a[href]").attr("abs:href");
                    Element e = relatedPublication.nextElementSibling();
                    String titleDescription = e.text();
                    RelatedTitles rt = new RelatedTitles(temTitle, titleDescription, titleUrl);
                    related_Titles.add(rt);
                    e.nextElementSibling();
                    System.out.printf("Cleaned One: %s is: %s", inTitles[i], temTitle);
                }

            } else {
                System.err.println("Connection was null");
                //continue;
                throw new IOException();
            }
        }

        return related_Titles;
    }

    public static List<RelatedTitles> findFrequenceyTitles(List<RelatedTitles> publication) {

        System.out.println("Adding titles to the Hash Table \n\n");

        Integer frequency = 1;
        System.out.println("frequency= " + frequency);

        for (RelatedTitles pub : publication) {

            Integer n = frequencyRelatedTitles.get(pub.getTitle());
            if (n != null) {
                frequencyRelatedTitles.put(pub.getTitle(), n + 1);
                pub.setFrequency(pub.getFrequency() + 1);
                System.out.println("Last Related Titles = " + n);
            } else {
                frequencyRelatedTitles.put(pub.getTitle(), frequency);
                pub.setFrequency(frequency);
            }
        }
        return publication;
    }

    public static List<RelatedTitles> connectToLibrary(List<RelatedTitles> normalizedRelatedTitles) {

        HashMap<String, Integer> libraryTitlesMap = new HashMap<>();
        HashMap<String, Integer> relatedTitlestempMap = new HashMap<>();

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                URLConnection yc;
                try {
                    URL url = new URL(URL_TITLES_QUERY);
                    yc = url.openConnection();

                    BufferedReader in;
                    in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                    String inputLine = in.readLine();
                    while ((inputLine = in.readLine()) != null) {
                        String unQuoatedTitle = inputLine.substring(1, inputLine.length() - 1).trim();
                        libraryTitlesMap.put(unQuoatedTitle, 1);
                    }
                } catch (IOException ex) {
                    System.out.println("Error reading the stream");
                    ex.getMessage();
                    //return null;
                }
            }
        };
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                for (RelatedTitles relatedTitles : normalizedRelatedTitles) {
                    relatedTitlestempMap.put(relatedTitles.getTitle(), 1);
                    System.out.println("title is:" + relatedTitles.getTitle());
                }

            }

        };

        Thread thr1 = new Thread(r1);
        Thread thr2 = new Thread(r2);
        thr1.start();
        thr2.start();
        try {
            thr1.join();
            thr2.join();
        } catch (InterruptedException ex) {
            System.out.println("Error in thread execution:" + ex.getMessage());
            //return null;
            return normalizedRelatedTitles;
        }
        System.out.println("after threads, removing same titles:");

        Iterator it = libraryTitlesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String title = (String) pairs.getKey();
            Integer n = relatedTitlestempMap.get(title);

            if (n != null) {
                relatedTitlestempMap.remove(title);
                System.out.println("title removed?" + title);
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        List<RelatedTitles> temList = new ArrayList<>();
        for (RelatedTitles rt : normalizedRelatedTitles) {
            Integer n = relatedTitlestempMap.get(rt.getTitle());
            if (n == null) {
                temList.add(rt);
                System.out.println("Removing similarities from the relatedTitles list:" + rt.getTitle());
            }
        }
        boolean removedTitle = normalizedRelatedTitles.removeAll(temList);
        if (removedTitle) {
            System.out.println("success");
        } else {
            System.out.println("fail to remove any");
        }

        return normalizedRelatedTitles;

    }

    public static ScholarSearchResult findCitationListURL(String title) throws IOException, Exception {

        List<ScholarSearchResult> searchResultList = QueryScholar.queryGoogleScholar(title);
        if (searchResultList != null) {
            System.out.println("start scholar query request");
            ScholarSearchResult foundSearchResult = QueryScholar.searchCitationListURL(title, searchResultList);
            System.out.println("URL single query::: " + foundSearchResult);
            if (foundSearchResult != null) {
                System.out.println("citationListURL is not null" + foundSearchResult);

                return foundSearchResult;
            } else {
                System.out.println("citationListURL is null" + foundSearchResult);
                return null;
            }
        } else {
            System.out.println("title is not on google scholar" + title);
            return null;
        }
    }

    public static List<String> getTitlesByKeyword(String keyword) {
        List<String> keywordTitles = new ArrayList<>();
        //split keyword
        String UrlFormattedKeyword = keyword.replaceAll(" ", "+").trim();
        String keywordUrl = URL_TITLES_BY_KEYWORD_QUERY_PART_1 + UrlFormattedKeyword + URL_TITLES_BY_KEYWORD_QUERY_PART_2;
        System.out.println("UrlFormattedKeyword:" + UrlFormattedKeyword);
        System.out.println("keywordUrl: " + keywordUrl);

        URL queryUrl;
        try {
            queryUrl = new URL(keywordUrl);
        } catch (MalformedURLException ex) {
            System.out.println("Error connecting to Library. cause" + ex);
            return null;
        }

        try {
            URLConnection queryUrlConnection = queryUrl.openConnection();
            try (DataInputStream dis = new DataInputStream(queryUrlConnection.getInputStream())) {
                String inputLine = dis.readLine();
                while ((inputLine = dis.readLine()) != null) {
                    keywordTitles.add(inputLine);
                    System.out.println(inputLine);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error opening the connection to Library, cause" + ex);
            return null;
        }
        System.out.println("List titles , size: " + keywordTitles.size());
        for (String a : keywordTitles) {
            System.out.println(a);
        }
        // Sort the list by fixed permutation
        Collections.shuffle(keywordTitles);
        return keywordTitles;
    }

    public static List<RelatedTitles> processRelatedTitlesRESTService(String[] incomingTitles) throws Exception {
//Step # 1: Get related titles list for each title
        List<RelatedTitles> outRelatedTitles = Publication.searchPublication(incomingTitles);

        if (outRelatedTitles.isEmpty()) {
            System.out.println("First Step was null");
            throw new Exception();
        }
// Step # 2: Combine all the related titles Lists (while removing dublicates) and find related titles frequency
        List<RelatedTitles> titlesWithFrequency = Publication.findFrequenceyTitles(outRelatedTitles);
        if (titlesWithFrequency == null) {
            System.out.println("Second Step was null");

            throw new Exception();
        }
//Step # 3: Query the Library and remove the Dublicated related titles from step # 2
        List<RelatedTitles> normalizedRelatedTitles = Publication.connectToLibrary(titlesWithFrequency);

        return normalizedRelatedTitles;
    }
    
    public static List<String> getLibraryKeywords() throws IOException
    {
        List<String> KeywordsList=new ArrayList<>();
        URLConnection yc;
                try {
                    URL url = new URL(URL_LIBRARY_KEYWORDS);
                    yc = url.openConnection();
                    BufferedReader in;
                    in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                    String inputLine = in.readLine();
                    while ((inputLine = in.readLine()) != null) {
                        //String unQuoatedTitle = inputLine.substring(1, inputLine.length() - 1).trim();
                        KeywordsList.add(inputLine);
                        System.out.println("keyword:"+inputLine);
                    }
                } catch (IOException ex) {
                    System.out.println("Error reading the stream");
                    ex.getMessage();
                    throw new IOException();
                    //return null;
                }
         
        return KeywordsList;
    }
}
