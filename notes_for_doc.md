* Models communicate with Controllers via exceptions
* Controllers get data through a model, that validates and represents the used entities in the db app
* Controllers are de facto singleton classes
* Database is a de jure singleton class, realized its singletonness through the static fields and methods, like the linkedlists it stores during a session.