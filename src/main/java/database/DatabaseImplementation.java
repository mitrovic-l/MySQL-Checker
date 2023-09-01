package database;

import lombok.AllArgsConstructor;
import lombok.Data;
import resource.DBNode;
import resource.data.Row;

import java.util.List;

@Data
@AllArgsConstructor
public class DatabaseImplementation implements Database {

    private Repository repository;

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    @Override
    public DBNode loadResource() {
        return repository.getSchema();
    }

    @Override
    public List<Row> readDataFromTable(String tableName) {
        return repository.get(tableName);
    }
    public List<Row>readQueryData(String query){
        return  repository.runQ(query);
    }
}
