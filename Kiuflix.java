import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.*;
import java.nio.file.*;


public class Kiuflix<V extends Video> {
    private final Stream<V> stream;
    private List<V> downloads;
    private int budget;
    private final int cost;

    public Kiuflix(Stream<V> stream, int budget, int cost) {
        this.stream = stream;
        this.budget = budget;
        this.cost = cost;
    }

    public void bulkView(Predicate<V> predicate){
        downloads = stream.filter(predicate).toList();
        downloads.stream().takeWhile(Objects::nonNull).forEach(e -> {
                if(budget >= cost) {
                    e.view();
                    budget -= cost;
                }
        });

        System.out.println("\n remaining budget:\t" + budget);
    }

    public static void main(String[] args) throws IOException {
        class MyVideo implements Video{
            private String title;

            public MyVideo(String title) {
                this.title = title;
            }

            @Override
            public String title() {
                return title;
            }

            @Override
            public void view() {
                System.out.println(title);
            }

            @Override
            public void skip() {
            }
        }

        Kiuflix<MyVideo> kiuflix= new Kiuflix<>(Files.lines(Path.of(args[0])).
                map(MyVideo::new), 100, 15);
        kiuflix.bulkView(myVideo -> myVideo.title.length() % 4 == 1);
    }
}
