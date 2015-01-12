package de.hpi.smm.meetup_miner;

import java.sql.SQLException;
import java.util.List;

import de.hpi.smm.meetup_miner.db.DatabaseConnector;
import de.hpi.smm.meetup_miner.db.EventLoader;
import de.hpi.smm.meetup_miner.rsvp_analysis.core.Event;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ClassNotFoundException, SQLException
    {
    	DatabaseConnector.setup(4);
    	EventLoader loader = new EventLoader(DatabaseConnector.getNewConnection());
    	List<Event> events = loader.load("16037582");
    	for (Event event : events) {
			System.out.println(event.getID() + " - " + event.getTitle() + " - " + event.getTime());
			System.out.println(event.getYesMemberIds());
		}
        System.out.println( "Hello World!" );
    }
}
