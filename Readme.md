# MeetupMiner

This repository collects a number of algorithms to find interesting events on [Meetup](http://www.meetup.com/).
The algorithms are developed as part of the [Social Media Mining](http://hpi.de/studium/lehrveranstaltungen/it-systems-engineering/lehrveranstaltung/course/2014/social_media_mining.html) seminar at the Hasso-Plattner Institute Potsdam.

## Development

### Set up your Secrets.java

You'll need to add a `Secrets.java` file that contains passwords and connection info. It should look like that:

```java
package de.hpi.smm.meetup_miner.config;

public class Secrets {

  public static String HANA_IP_INTERN = "<IP>";
  public static String HANA_IP_VPN = "<IP>";
  public static int HANA_PORT = <PORT>;
  public static String HANA_USER = "<USERNAME>";
  public static String HANA_PASSWORD = "<PASSWORD>";
  
  
  public static String[] MEETUP_API_KEYS = new String[]{
    "453d2b4087763127a61255d3d4132a",
    "4037552f6134271f2d5f4930bc2432",
    "21193e701e6363625f7c59315d77942",
    "55656a1e2d285b2b3b2b607e537bd3f"
  };
}
```

### Installing new JAR-Dependencies

In the case that a dependency is not hosted by any Maven repository, it should be added by the following command, as recommended in [this post](http://stackoverflow.com/a/7623805):

```bash
mvn install:install-file -DlocalRepositoryPath=libs -DcreateChecksum=true -Dpackaging=jar -Dfile=<JAR file> -DgroupId=<packageName> -DartifactId=<artefactId> -Dversion=<version>
```

You can then commit any changes to the `libs/` directory and add the dependency to the `pom.xml`.

### Connecting to the Database

Use the `DatabaseConnector`, see [this example](https://github.com/georgwiese/MeetupMiner/blob/master/src/main/java/de/hpi/smm/meetup_miner/db/DbExample.java).
