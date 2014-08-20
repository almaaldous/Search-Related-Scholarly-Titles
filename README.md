Search-Related-Scholarly-Titles
===============================

The purpose of this web service is to help expanding the Humanitarian Computing Library: http://humanitariancomp.referata.com/wiki/Welcome, which is a (wiki-based) site dedicated for up to date publications in the feild of Humanitarian Computing. 

Search-Related-Scholarly-Titles (SRST) is inspired by the python module created by Christian Kreibich project which is a module that implements a querier and parser for Google Scholar's output. 
Find the original python script @ https://github.com/ckreibich/scholar.py



Features:

1. The first web service allows the user to post research titles, then using the python module at the server side to   query google scholar. Then, the citation List URL is extracted for every titles which has citations, then another    URLConnection is performed to get the html source of that url. Using Jsoup Java Library to extract the titles whcih    cite the original title. 
2. The resulted related titles List is merged together and then finding their frequency and elemintating the         dublicates.
3. Query the Humanitarian Computing Library to retrieve the existing titles, then perform title comparasion and  remove title from the related titles list if they already exist in the library.
4. The second web service allows the user to search related titles beased on a keyword which exist in the Humanitarian Library. This search is perfomred by typing a keyword, then a query is performed to the Library to get the titles related to this keyword. Then the web service performs search by title (5 titles in each request) as in the first webservice. The user is asked to search for the next 5 titles and so on, till the list of keyword related titles exuhsted.
5. The output of both web services is a table which displays :
  a. Related Title
  b. Frequency [how many times the title appeared in each list of the original titles search]
  c. Brief description [some abreviations for Author names and paper site or conference]
  d. Title URL [the original URL for the title, this is clickable and opens the paper site for easy adding of     publications into the library ]
  e. Add this title? [this opens a form in a new page to add the title in the library]


Notes:

In order to be able to improve/modify/enhance this webservice, you need the following:

1. Install scholar.py file [download beautifulsoup4 python library to run the script]
2. rate limit the duration to query Google scholar[ 15-20 titles/minute]
3. Add Jsoup Library [1.7.3.jar]
4. Add GSON Library [2.2.4.jar]
5. Add jersey-media-json-jackson Library[2.11.jar]
6. Add ommons-io [2.4.jar]
4. This version was tested on Netbeans 8.0 with Glassfish4 server
5. No files were used to save intermediate results of the program execution




Example 1:
using the first web service:
Type these sample titles:
1. Classifying Text Messages for the Haiti Earthquake
2. Crowdsourcing for Crisis Mapping in Haiti

The issue the click the search scholar button


Example 2: 
part 1: If you are redirected from the Humanitarian Computing Library, then the keyword will be appened to URL and the search will be initiated directly without clicking any buttons.
part 2: If you are in the search by keyword page select one of keywords from the dropdown datalist and hit search scholar. 

In both parts, you will see the titles related with the inserted keyword showed in a table.



I've posted this project here so I can more easily get any comments bug fixes. Also, I would love to recieve suggestions on the performnace and the response time for the web service.

Thanks to all who will show interest to improve this web service. This web service will be made available online and people can use it to add new publication in the Library. 


If you'd like to get in touch, email me at almaaldous@gmail.com or tweet me on Twitter @Ali__Khalil

Regards, 
Ali El Dous
