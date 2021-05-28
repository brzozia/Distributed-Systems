package actors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import messages.*;

import java.sql.*;

public class Database extends AbstractBehavior<Message> {
    private Connection connection;
    private Statement statement;

    public Database(ActorContext<Message> context, Connection connection) throws SQLException, ClassNotFoundException {
        super(context);
        this.connection = connection;
        this.statement = connection.createStatement();
    }

    public static Behavior<Message> create(Connection connection) {
        return Behaviors.setup((context) -> new Database(context,connection));
    }

    @Override
    public Receive<Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(DatabaseMessage.class, this::onUpdate)
                .onMessage(GetDatabaseInfoMessage.class, this::onGetInfo)
                .build();
    }

    private Behavior<Message> onUpdate(DatabaseMessage request) throws SQLException {
        int toAdd = statement.executeQuery("SELECT errors FROM errorsTable WHERE satellite_id="+request.satelliteId).getInt("errors");
        statement.execute("UPDATE errorsTable set errors = "+(request.errorsNumber +toAdd)+" WHERE satellite_id = "+request.satelliteId);
        return this;
    }

    private Behavior<Message> onGetInfo(GetDatabaseInfoMessage request) throws SQLException {
        ResultSet errors = statement.executeQuery("SELECT errors FROM errorsTable WHERE satellite_id="+request.satelliteId);
        int errorsNo = errors.getInt("errors");
        request.replyTo.tell(new DatabaseMessage(request.satelliteId,errorsNo));
        return this;
    }
}
