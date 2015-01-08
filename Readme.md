# MeetupMiner

This repository collects a number of algorithms to find interesting events on [Meetup](http://www.meetup.com/).
The algorithms are developed as part of the [Social Media Mining](http://hpi.de/studium/lehrveranstaltungen/it-systems-engineering/lehrveranstaltung/course/2014/social_media_mining.html) seminar at the Hasso-Plattner Institute Potsdam.

## Development

### Installing new JAR-Dependencies

In the case that a dependency is not hosted by any Maven repository, it should be added by the following command, as recommended in [this post](http://stackoverflow.com/a/7623805):

```bash
mvn install:install-file -DlocalRepositoryPath=libs -DcreateChecksum=true -Dpackaging=jar -Dfile=<JAR file> -DgroupId=<packageName> -DartifactId=<artefactId> -Dversion=<version>
```

You can then commit any changes to the `libs/` directory and add the dependency to the `pom.xml`.
