package logic.task;

import javafx.concurrent.Task;

public class LoadXmlTask extends Task<Boolean> {

    private String xmlName;

    public LoadXmlTask(String xmlName) {
        this.xmlName = xmlName;
    }

    @Override
    protected Boolean call() throws Exception {
        return null;
    }
}
