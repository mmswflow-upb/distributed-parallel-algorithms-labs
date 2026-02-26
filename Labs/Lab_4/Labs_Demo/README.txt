RPC Lab 4 (from DS Lab4.PDF)

Files
- Client.java     (UDP-based RPC client)
- RPCServer.java  (UDP-based RPC server supporting add)

How to run (two terminals)
1) Compile:
   javac Client.java RPCServer.java

2) Terminal A (server):
   java RPCServer

3) Terminal B (client):
   java Client

Then type requests like:
   add 3 4

To quit the server, send:
   q
