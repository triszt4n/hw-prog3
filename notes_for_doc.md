* Models communicate with Controllers via exceptions.
* Controllers get data through a model, that validates and represents the used entities in the db app.
* Controllers are de facto singleton classes, instantiated once, passed only if needed.
* Database is a de jure singleton class, realized its singletonness through the static fields and methods, like the linkedlists it stores during a session.
* A jelszó titkosítása valójában egy vicc, mert a program eltárolja a nyers jelszót nagyon sok helyen is, így prone to memory extraction attacks.

Uses:
* (dotenv-java)[https://github.com/cdimascio/dotenv-java] *requires Java 8 or greater*
* (javax.json)[https://mvnrepository.com/artifact/org.glassfish/javax.json/1.1.4]
* (JUnit 5.4)[https://junit.org/junit5/docs/current/user-guide/]

QUESTIONS:
* Okoz-e problémát, hogy ennyire ráerőltettem-e a dologra az MVC megközelítést és a rétegezést? (sok view fieldje van UserControllernek) --> cél: bővíthetőség
* Database egy singleton osztály, ezért mindene static basically, ez gond-e?
* Hogy oldjam akkor meg, hogy maradjon a UserModel, és a leszármazottai? Mmint hogy ne legyen type-check, amikor arra vagyok valójában a program fejlesztése során, hogy a user az valójában instructor-e?

Futtatás ezzel:
Windows
```bash
javac @.sources -d out\production\pluto -cp lib\junit-jupiter-5.4.2.jar;lib\junit-jupiter-api-5.4.2.jar;lib\apiguardian-api-1.0.0.jar;lib\opentest4j-1.1.1.jar;lib\junit-platform-commons-1.4.2.jar;lib\junit-jupiter-params-5.4.2.jar;lib\junit-jupiter-engine-5.4.2.jar;lib\junit-platform-engine-1.4.2.jar;lib\javax.json-1.1.4.jar;lib\dotenv-java-2.2.0.jar
java -Dfile.encoding=UTF-8 -classpath out\production\pluto;lib\junit-jupiter-5.4.2.jar;lib\junit-jupiter-api-5.4.2.jar;lib\apiguardian-api-1.0.0.jar;lib\opentest4j-1.1.1.jar;lib\junit-platform-commons-1.4.2.jar;lib\junit-jupiter-params-5.4.2.jar;lib\junit-jupiter-engine-5.4.2.jar;lib\junit-platform-engine-1.4.2.jar;lib\javax.json-1.1.4.jar;lib\dotenv-java-2.2.0.jar pluto.app.Application
```

Unix
```bash
javac @.sources -d out\production\pluto -cp lib/junit-jupiter-5.4.2.jar;lib/junit-jupiter-api-5.4.2.jar;lib/apiguardian-api-1.0.0.jar;lib/opentest4j-1.1.1.jar;lib/junit-platform-commons-1.4.2.jar;lib/junit-jupiter-params-5.4.2.jar;lib/junit-jupiter-engine-5.4.2.jar;lib/junit-platform-engine-1.4.2.jar;lib/javax.json-1.1.4.jar;lib/dotenv-java-2.2.0.jar pluto.app.Application
java -Dfile.encoding=UTF-8 -classpath out/production/pluto;lib/junit-jupiter-5.4.2.jar;lib/junit-jupiter-api-5.4.2.jar;lib/apiguardian-api-1.0.0.jar;lib/opentest4j-1.1.1.jar;lib/junit-platform-commons-1.4.2.jar;lib/junit-jupiter-params-5.4.2.jar;lib/junit-jupiter-engine-5.4.2.jar;lib/junit-platform-engine-1.4.2.jar;lib/javax.json-1.1.4.jar;lib/dotenv-java-2.2.0.jar pluto.app.Application
```

IntelliJ-ben ha szeretnéd, állítsd át a PLUTO_CONSOLE_COLORFUL environment variable-t TRUE-ra, hogy színesben lásd a console kiírásait (IntelliJ és Unix shell érti az ANSI színkódolást).

