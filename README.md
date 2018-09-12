### Simple Course Management App

#### Configuration (Optional)
1. Review the values of the config at [src/general/Util.java](/src/general/Util.java)
```
    public final static String BASE_URL = "localhost";
    public final static Integer PORT = 8000;
    public final static String CREATE_FACULTY_ENDPOINT = "create-faculty";
    public final static String GET_FACULTY_ENDPOINT = "get-faculty";
    public final static String DAT_FILE = "faculty.dat";
```

#### Running the server
1. Run the java class at [src/server/Main.java](/src/server/Main.java)
2. Watch out for the messages below on your console.
    ```
      >  Initializing .dat store
      >  .dat store initialized
      >  Server started successfully
    ````


#### Running the client
1. Run the java class at [src/client/Main.java](/src/client/Main.java)

#### Client
https://www.codementor.io/adamschock