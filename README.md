Feedage is a basic "news reader" app that sorts articles from various RSS feeds into categories. This project was created as a final project for my Computer Science class in high school. 

## Screenshots

|Categories|Article|About|
|-----|-----|-----|
|![img](./screenshots/categories.png?raw=true)|![img](./screenshots/article.png?raw=true)|![img](./screenshots/about.png?raw=true)|

## Contributing

Pull requests are welcome. Please see [CONTRIBUTING.md](./.github/CONTRIBUTING.md) for more information on steps to take before submitting a pull request.

## Organization

This project is split into two sections, an "app" and a "library". The library handles all of the data processing, such as fetching articles from the server, parsing data, and organizing them into categories. The app module handles all of the UI, as well as bookmarks and caching.

### Sorting Algorithm

The library module sorts the articles into categories by looking for common pairs of words between them. This process can be split into multiple steps as follows:

#### 1. Getting a Threshold

The program takes the average of "difference values" resulting from the comparison of every possible combination of articles provided to it. The process of comparing one article to another is as follows:

##### A. String "Grouping"

The content of each article is split into groups of words and the amount they occur, accounting for duplicates and removing words deemed "useless". For example, the sentence 
```
“Pudgy orangutans are a sign of good health and peace in the orangutan culture”
```
would be split into 
```
("pudgy","orangutans") ("orangutans","are") ("are","a") ("a","sign") ("sign","of") ("of","good") ("good","health") ("health","and") ("and","peace") ("peace","in") ("in","the") ("the","orangutan") ("orangutan","culture")
```
but, after removing useless words (strings with a length less than 5, along with a few hardcoded strings to ignore), you are left with just `("pudgy","orangutans")` and `("orangutan","culture")`.

##### B. Comparing Occurrences

The grouped strings of the two articles are then compared to each other to determine the relative "difference" between them. If `("pudgy","orangutans")` occurs twice in one article and never in the second, the difference will increase by about two, accounting for the difference in length of the two articles.

#### 2. Getting Another Threshold

The program then iterates through every possible article, and compares it to every other article in order to get another threshold difference for only that article. If the resulting threshold is less than the threshold of all the articles obtained in the first step, it will continue to create a category based on that article.

#### 3. Adding Posts

In one final loop through all of the posts, the program will compare the current article to every other, adding it to a list if the difference is less than the current post's threshold. After checking that multiple articles are present and that they come from multiple sources, the category will be created.

#### 4. Creating the Description

This part only exists to provide the user with a reason that the category was created. All of the word groupings used in step 1B will be added to the category such that:

- if a grouping exists with the same first word, the new grouping will be added before it
- if a grouping exists with the same last word, the new grouping will be added after it
- if no such groupings exist, the new grouping will be added at the end of the list

These groupings will then be combined into fake "sentences" to display as the category description, so that `("have","smelly") ("orangutans","have") ("smelly,"hair")` becomes `"Orangutans have smelly hair."`.
