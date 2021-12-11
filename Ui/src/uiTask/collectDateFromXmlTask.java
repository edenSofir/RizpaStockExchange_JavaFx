package uiTask;

import javafx.application.Platform;
import javafx.concurrent.Task;
import user.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class collectDateFromXmlTask extends Task<Boolean> {

    private String fileName;
    private Collection<User> totalUsers;
    private Map<String, Integer> totalStocks;
    private Consumer<User> userConsumer;
    private Consumer<Map.Entry<String, Integer>> entryConsumer;
    private final int SLEEP_TIME = 0;

    public collectDateFromXmlTask(String fileName, Map<String, Integer> stocks, Collection<User> users, Consumer<User> consumerUser, Consumer<Map.Entry<String, Integer>> consumerStock) {
        this.fileName = fileName;
        this.totalUsers = users;
        this.totalStocks = stocks;
        this.userConsumer = consumerUser;
        this.entryConsumer = consumerStock;
    }

    @Override
    protected Boolean call() throws Exception {
        int Max = checkAmountOfWork(totalStocks, totalUsers);
        int counter = 0;
        try {
            updateMessage("Fetching file...");
            BufferedReader reader = Files.newBufferedReader(
                    Paths.get(fileName),
                    StandardCharsets.UTF_8);

            Thread.sleep(SLEEP_TIME);
            updateMessage("Counting stocks...");
            for (Map.Entry<String, Integer> item : totalStocks.entrySet()) {
                Platform.runLater(
                        () -> entryConsumer.accept(item)
                );
                counter++;
                updateProgress(counter, Max);
            }
            Thread.sleep(SLEEP_TIME);
            updateMessage("Counting users...");
            for (User currUser : totalUsers) {
                Platform.runLater(
                        () -> userConsumer.accept(currUser)
                );
                counter++;
                updateProgress(counter, Max);
            }
            updateMessage("Done...");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.TRUE;
    }

    private int checkAmountOfWork(Map<String, Integer> totalStocks, Collection<User> totalUsers) {
        return totalUsers.size() + totalStocks.size();
    }


}
