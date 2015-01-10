# MeetupMiner

This repository collects a number of algorithms to find interesting events on [Meetup](http://www.meetup.com/).
The algorithms are developed as part of the [Social Media Mining](http://hpi.de/studium/lehrveranstaltungen/it-systems-engineering/lehrveranstaltung/course/2014/social_media_mining.html) seminar at the Hasso-Plattner Institute Potsdam.

## Development

### Setup

#### Libs

- You'll need to add a `com.sap.db.jdbc.hana_1.0.0.SNAPSHOT.jar` to the `lib` folder that should contain the HANA jDBC driver. Project members can find a copy [here](https://docs.google.com/file/d/0B4gX4c6gHS2VSHQxaFE2ZG5LSk0).
- Install the libs to your local repository by running `./setup_libs.py`.

#### Secrets.java

You'll need to add a `Secrets.java` file that contains passwords and connection info. It should look like that:

```java
package de.hpi.smm.meetup_miner.config;

public class Secrets {

  public static String HANA_IP_INTERN = "<IP>";
  public static String HANA_IP_VPN = "<IP>";
  public static int HANA_PORT = <PORT>;
  public static String HANA_USER = "<USERNAME>";
  public static String HANA_PASSWORD = "<PASSWORD>";
  
  
  public static String[] MEETUP_API_KEYS = new String[]{...};
}
```

Project members can find an up-to-date version [here](https://docs.google.com/document/d/1ha4e_3WAdv7tzxFIKzCpIkE3jDdZSrEo4z_hMgFqMkA/edit).

### Installing new JAR-Dependencies

In the case that a dependency is not hosted by any Maven repository, it can be added to the `lib` directory.
It has to comply to a certain naming scheme, please refer to the readme of the [install-to-project-repo](https://github.com/nikita-volkov/install-to-project-repo) for details.
It can then be installed by running `./setup_libs.py`.

### Connecting to the Database

Use the `DatabaseConnector`, see [this example](https://github.com/georgwiese/MeetupMiner/blob/master/src/main/java/de/hpi/smm/meetup_miner/db/DbExample.java).
