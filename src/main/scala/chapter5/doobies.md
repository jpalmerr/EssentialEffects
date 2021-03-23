# Doobies context

https://tpolecat.github.io/doobie/docs/14-Managing-Connections.html#about-transactors

1. Acquiring a database connection should use a separate thread pool from other kinds of effects, since they block.

    - a separate fixed thread pool should be used
    - since the underlying JDBC thread pool itself is fixed
    - using more threads than the underlying pool would only create additional blocked threads
    
2. JDBC operations themselves block, so the usual blocking context should be used

3. post query effects on the default context

