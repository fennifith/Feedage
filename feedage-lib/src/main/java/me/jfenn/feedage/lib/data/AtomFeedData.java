package me.jfenn.feedage.lib.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class AtomFeedData extends FeedData {

    public AtomFeedData(String url) {
        super(url);
    }

    public AtomFeedData(String url, int pageStart) {
        super(url, pageStart);
    }

    @Override
    List<PostData> parseContent(String content) {
        List<PostData> posts = new ArrayList<>();
        Document document = Jsoup.parse(content);
        Element root = document;
        while (root.children().size() == 1) {
            root = root.children().first();
            if (root.tagName().equals("html"))
                root = document.selectFirst("body");
        }

        Element nameElement = root.selectFirst(":root > title");
        if (nameElement != null)
            setName(nameElement.text());

        Element homepageElement = root.selectFirst(":root > id");
        if (homepageElement != null)
            setHomepage(homepageElement.text());

        for (Element element : root.select(":root > id")) {
            if (element.attr("rel").equals("alternate") && element.hasAttr("href")) {
                setHomepage(element.attr("href"));
                break;
            }
        }

        for (Element element : root.select(":root > entry, :root > item")) {
            PostData post = new PostData(this);

            Element titleElement = element.selectFirst(":root > title");
            if (titleElement != null)
                post.setTitle(titleElement.text());

            Element descriptionElement = element.selectFirst(":root > description");
            if (descriptionElement != null)
                post.setTitle(descriptionElement.text());

            Element imageElement = element.selectFirst(":root > media|content");
            if (imageElement != null)
                post.setImageUrl(imageElement.text());

            Element contentElement = element.selectFirst(":root > content|encoded");
            if (contentElement != null)
                post.setContent(contentElement.text());

            for (Element tagElement : element.select(":root > category"))
                post.addTag(tagElement.text());

            for (Element authorElement : element.select(":root > dc|creator"))
                post.addAuthor(new AuthorData(authorElement.text()));

            for (Element authorElement : element.select(":root > author")) {
                AuthorData author;

                Element authorNameElement = authorElement.selectFirst(":root > name");
                if (authorNameElement != null)
                    author = new AuthorData(authorNameElement.text());
                else break;

                post.addAuthor(author);
            }

            posts.add(post);
        }

        return posts;
    }
}
