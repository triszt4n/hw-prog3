* Models communicate with Controllers via exceptions.
* Controllers get data through a model, that validates and represents the used entities in the db app.
* Controllers are de facto singleton classes, instantiated once, passed only if needed.
* Database is a de jure singleton class, realized its singletonness through the static fields and methods, like the linkedlists it stores during a session.

QUESTIONS:
* Okoz-e problémát, hogy ennyire ráerőltettem-e a dologra az MVC megközelítést és a rétegezést? (sok view fieldje van UserControllernek) --> cél: bővíthetőség
* Database egy singleton osztály, ezért mindene static basically, ez gond-e?
* Hogy oldjam akkor meg, hogy maradjon a UserModel, és a leszármazottai? Mmint hogy ne legyen type-check, amikor arra vagyok valójában a program fejlesztése során, hogy a user az valójában instructor-e?