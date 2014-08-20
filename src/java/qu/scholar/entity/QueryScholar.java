package qu.scholar.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QueryScholar {

    public static String SCHOLAR_PROGRAM = "/home/aeldous/NetBeansProjects/HumanitarianScholarSearch/scholarCkreibich.py";
    public static int NUMBER_OF_RESULTS = 5;
    public static final int min = 3000;
    public static final int max = 40000;

    public static ScholarSearchResult searchCitationListURL(String title, List<ScholarSearchResult> searchResultList) throws Exception {
        // step # 2
        // Title | URL | Year | Citations | Versions| Cluster ID | PDF Link |
        // Citations List | Versions List | None

        title = title.replace(" ", "");
        System.out.println("With no spaces:" + title);
        int foundIndex = 0;
        for (int k = 0; k < searchResultList.size(); k++) {
            String fromFiletitle = searchResultList.get(k).getTitle().replace(" ", "");
            System.out.println("fromFiletitle :" + fromFiletitle);
            System.out.println("original title:" + title);

            if (fromFiletitle.equalsIgnoreCase(title)) {
                if (!searchResultList.get(k).getCitationsList().equals("None")) {
                    System.out.print("citations URL Found!, and oroginal title=%s " + searchResultList.get(k).getTitle());
                    return searchResultList.get(foundIndex);
                }
            }
        }

        throw new Exception();
    }

    public static String quote(String unquoted) {
        return "\"" + unquoted + "\"";
    }

    public static List<ScholarSearchResult> queryGoogleScholar(String newTitle) throws IOException {
        int counter = 0;
        String title = quote(newTitle);
        String format = "--csv";
        Random random = new Random();
        int randomInt = random.nextInt(max - min) + min;
        System.out.println("waiting time? " + randomInt);

        String[] commandLine = {"python", SCHOLAR_PROGRAM, "-c", Integer.toString(NUMBER_OF_RESULTS), "format", format,
            "--phrase", title};

        Process p;

        try {
            p = Runtime.getRuntime().exec(commandLine);
        } catch (IOException e) {
            System.out.println("Error when executing the script command" + e.getMessage());
            throw e;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String ret;
        String queryResult;
        List<ScholarSearchResult> scholarSearchResult = new ArrayList<>();
        try {
            if ((queryResult = in.readLine()) == null) {
                System.out.println("no result returned from google scholar inputstream");
                System.out.println("Command line: '" + String.join(" ", commandLine) + "'");
                System.out.println("Exit value=" + p.exitValue());

                System.out.println("Trying to see error stream");
                BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line;
                while ((line = err.readLine()) != null) {
                    System.out.println(" ERR=" + line);
                }
                err.close();

                throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("Error while reading from the process" + e.getMessage());
            throw e;
        }
        try {
            while ((ret = in.readLine()) != null) {
                counter++;
                queryResult = queryResult + "\n" + ret;
            }
            queryResult = queryResult.trim();

            System.out.println("Total Query Result:" + queryResult);
            String[] unformattedQueryResult = queryResult.split("\\n");
            System.out.println("Total Query Result:" + queryResult);

            // adding to DS
            System.out.println("searchResult array result");

            for (int r = 0; r < unformattedQueryResult.length; r++) {

                System.out.printf("index: %s >> searchResult:%s \n", r, unformattedQueryResult[r]);
                String[] titleTuple = unformattedQueryResult[r].split("\\|");
                ScholarSearchResult scholarResult = new ScholarSearchResult(
                        titleTuple[0],
                        titleTuple[1],
                        titleTuple[2],
                        titleTuple[3],
                        titleTuple[4],
                        titleTuple[5],
                        titleTuple[6],
                        titleTuple[7],
                        titleTuple[8],
                        titleTuple[9]);
                scholarSearchResult.add(scholarResult);
            }
            System.out.println("Total stream Read times: " + counter);
            p.waitFor();
            System.out.println("waiting random 40-60 seconds Seconds ...");
            Thread.sleep(randomInt);
        } catch (IOException e) {
            System.out.println("Error while reading from the process" + e.getMessage());
            throw new IOException();
        } catch (InterruptedException ex) {
            System.out.println("Error while writing semiQueryResult to file" + ex.getMessage());
            throw new IOException();
        }
        int exitValue = p.exitValue();
        if (exitValue != 0) {
            System.out.println("ERROR, program did not exit correctly: " + exitValue);
            if (exitValue == 2) {
                System.out.println("Probably, '" + SCHOLAR_PROGRAM
                        + "' was not found.");
            }
            throw new IOException();
        }
        p.destroy();
        return scholarSearchResult;
    }
}
