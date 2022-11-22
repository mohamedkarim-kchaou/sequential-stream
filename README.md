## Parallel Stream Disadvantages

- Managing threads, sources, and results creates an overhead that can be more expensive than the operation itself
- Splitting an arrayList can be cheap and effective but using non contiguous data structures can be much more costly than using sequential streams
- Parallel streams use ForkedJoinPool that uses commonPool and if the tasks to run are blocking, therefore, it can block system threads that uses the commonPool

## Advantage of completable comparing to parallel streams

- with CompletableFuture, the thread-pool can be specified so you can ignore the threads from the commonPool, while you cannot in case of Parallel Streams.
