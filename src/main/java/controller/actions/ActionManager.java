package controller.actions;

import java.sql.ResultSet;

public class ActionManager {
    private PrettyAction prettyAction;
    private BulkImportAction bulkImportAction;
    private ResultSetAction resultSetAction;
    private RunAction runAction;

    public ActionManager(){
        initialiseActions();
    }

    private void initialiseActions() {
        prettyAction = new PrettyAction();
        bulkImportAction= new BulkImportAction();
        resultSetAction= new ResultSetAction();
        runAction = new RunAction();
    }


    public ResultSetAction getResultSetAction() {
        return resultSetAction;
    }

    public void setResultSetAction(ResultSetAction resultSetAction) {
        this.resultSetAction = resultSetAction;
    }

    public BulkImportAction getBulkImportAction() {
        return bulkImportAction;
    }

    public void setBulkImportAction(BulkImportAction bulkImportAction) {
        this.bulkImportAction = bulkImportAction;
    }

    public PrettyAction getPrettyAction() {
        return prettyAction;
    }

    public void setPrettyAction(PrettyAction prettyAction) {
        this.prettyAction = prettyAction;
    }

    public RunAction getRunAction() {
        return runAction;
    }

    public void setRunAction(RunAction runAction) {
        this.runAction = runAction;
    }
}
