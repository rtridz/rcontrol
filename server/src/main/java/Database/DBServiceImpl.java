package Database;

import Database.DBService;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.LinkedList;
import java.util.List;
/**
 * Created by vadim on 23.09.15.
 */
public class DBServiceImpl implements DBService {
    // TODO hard code
    private static final String DB_NAME = "test";
    private static final String COLLECTION_NAME = "units";
    private static final String MONGO_HOST = "localhost";
    private static final int MONGO_PORT = 27017;

    private MongoClient mongoClient;
    private MongoDatabase db;

    public DBServiceImpl() {
        mongoClient = new MongoClient(MONGO_HOST, MONGO_PORT);
        db = mongoClient.getDatabase(DB_NAME);
    }

    public List<Document> find() {
        List<Document> documents = new LinkedList<>();
        try (MongoCursor<Document> cursor = db.getCollection(COLLECTION_NAME).find().iterator()) {
            while (cursor.hasNext()) {
                documents.add(cursor.next());
            }
        }
        return documents;
    }
}
